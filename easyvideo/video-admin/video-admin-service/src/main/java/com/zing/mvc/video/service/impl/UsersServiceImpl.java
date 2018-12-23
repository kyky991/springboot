package com.zing.mvc.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zing.mvc.video.mapper.UserMapper;
import com.zing.mvc.video.pojo.User;
import com.zing.mvc.video.pojo.UserExample;
import com.zing.mvc.video.service.IUsersService;
import com.zing.mvc.video.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
    private UserMapper usersMapper;

    @Override
    public PagedResult queryUsers(User user, Integer page, Integer pageSize) {

        String username = "";
        String nickname = "";
        if (user != null) {
            username = user.getUsername();
            nickname = user.getNickname();
        }

        PageHelper.startPage(page, pageSize);

        UserExample userExample = new UserExample();
        UserExample.Criteria userCriteria = userExample.createCriteria();
        if (StringUtils.isNotBlank(username)) {
            userCriteria.andUsernameLike("%" + username + "%");
        }
        if (StringUtils.isNotBlank(nickname)) {
            userCriteria.andNicknameLike("%" + nickname + "%");
        }

        List<User> userList = usersMapper.selectByExample(userExample);

        PageInfo<User> pageList = new PageInfo<User>(userList);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(userList);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }
}
