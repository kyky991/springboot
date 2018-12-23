package com.zing.boot.house.biz.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.zing.boot.house.biz.mapper.HouseMapper;
import com.zing.boot.house.biz.service.IAgencyService;
import com.zing.boot.house.biz.service.IHouseService;
import com.zing.boot.house.biz.service.IMailService;
import com.zing.boot.house.common.constants.HouseUserType;
import com.zing.boot.house.common.model.*;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;
import com.zing.boot.house.common.utils.BeanHelper;
import com.zing.boot.house.common.utils.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Value("${file.prefix}")
    private String IMG_PREFIX;

    @Autowired
    private IAgencyService agencyService;

    @Autowired
    private IMailService mailService;

    /**
     * 1.查询小区
     * 2.添加图片服务器地址前缀
     * 3.构建分页结果
     *
     * @param query
     * @param pageParams
     */
    @Override
    public PageData<House> queryHouse(House query, PageParams pageParams) {
        if (!Strings.isNullOrEmpty(query.getName())) {
            Community community = new Community();
            community.setName(query.getName());
            List<Community> communities = houseMapper.selectCommunity(community);
            if (!communities.isEmpty()) {
                query.setCommunityId(communities.get(0).getId());
            }
        }
        List<House> houses = queryAndSetImg(query, pageParams);
        Long count = houseMapper.selectPageCount(query);
        return PageData.buildPage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
    }

    @Override
    public List<House> queryAndSetImg(House query, PageParams pageParams) {
        List<House> houses = houseMapper.selectPageHouses(query, pageParams);
        houses.forEach(house -> {
            house.setFirstImg(IMG_PREFIX + house.getFirstImg());
            house.setImageList(house.getImageList().stream().map(img -> IMG_PREFIX + img).collect(Collectors.toList()));
        });
        return houses;
    }

    @Override
    public List<Community> getAllCommunities() {
        Community community = new Community();
        return houseMapper.selectCommunity(community);
    }

    /**
     * 添加房屋图片
     * 添加户型图片
     * 插入房产信息
     * 绑定用户到房产的关系
     *
     * @param house
     * @param user
     */
    @Override
    public void addHouse(House house, User user) {
        if (CollectionUtils.isNotEmpty(house.getHouseFiles())) {
            String images = Joiner.on(",").join(FileUtils.getImgPaths(IMG_PREFIX, house.getHouseFiles()));
            house.setImages(images);
        }
        if (CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
            String images = Joiner.on(",").join(FileUtils.getImgPaths(IMG_PREFIX, house.getFloorPlanFiles()));
            house.setFloorPlan(images);
        }
        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        bindUser2House(house.getId(), user.getId(), false);
    }

    @Override
    public void bindUser2House(Long houseId, Long userId, boolean collect) {
        HouseUser existHouseUser = houseMapper.selectHouseUser(userId, houseId, collect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
        if (existHouseUser != null) {
            return;
        }
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(collect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
        BeanHelper.setDefaultProp(houseUser, HouseUser.class);
        BeanHelper.onInsert(houseUser);
        houseMapper.insertHouseUser(houseUser);
    }

    @Override
    public HouseUser getHouseUser(Long houseId) {
        HouseUser houseUser = houseMapper.selectSaleHouseUser(houseId);
        return houseUser;
    }

    @Override
    public House queryOneHouse(Long id) {
        House query = new House();
        query.setId(id);
        List<House> houses = queryAndSetImg(query, PageParams.build(1, 1));
        if (!houses.isEmpty()) {
            return houses.get(0);
        }
        return null;
    }

    @Override
    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMapper.insertUserMsg(userMsg);
        User agent = agencyService.getAgentDetail(userMsg.getAgentId());
        mailService.sendMail("来自用户" + userMsg.getEmail() + "的留言", userMsg.getMsg(), agent.getEmail());
    }

    @Override
    public void updateRating(Long id, Double rating) {
        House house = queryOneHouse(id);
        Double oldRating = house.getRating();
        Double newRating = oldRating.equals(0D) ? rating : Math.min((oldRating + rating) / 2, 5);
        House updateHouse = new House();
        updateHouse.setId(id);
        updateHouse.setRating(newRating);
        BeanHelper.onUpdate(updateHouse);
        houseMapper.updateHouse(updateHouse);
    }

    @Override
    public void unbindUser2House(Long id, Long userId, HouseUserType type) {
        if (type.equals(HouseUserType.SALE)) {
            houseMapper.downHouse(id);
        } else {
            houseMapper.deleteHouseUser(id, userId, type.value);
        }
    }
}
