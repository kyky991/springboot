package com.zing.boot.house.repository;

import java.util.List;

import com.zing.boot.house.entity.HousePicture;
import org.springframework.data.repository.CrudRepository;

public interface HousePictureRepository extends CrudRepository<HousePicture, Long> {
    List<HousePicture> findAllByHouseId(Long id);
}
