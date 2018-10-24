package com.zing.boot.house.repository;

import java.util.List;

import com.zing.boot.house.entity.HouseDetail;
import org.springframework.data.repository.CrudRepository;

public interface HouseDetailRepository extends CrudRepository<HouseDetail, Long> {

    HouseDetail findByHouseId(Long houseId);

    List<HouseDetail> findAllByHouseIdIn(List<Long> houseIds);
}
