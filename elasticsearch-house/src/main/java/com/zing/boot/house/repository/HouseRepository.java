package com.zing.boot.house.repository;

import com.zing.boot.house.entity.House;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface HouseRepository extends PagingAndSortingRepository<House, Long>, JpaSpecificationExecutor<House> {

    @Modifying
    @Query("UPDATE House AS house SET house.cover = :cover WHERE house.id = :id")
    void updateCover(@Param(value = "id") Long id, @Param(value = "cover") String cover);

    @Modifying
    @Query("UPDATE House AS house SET house.status = :status WHERE house.id = :id")
    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") int status);

    @Modifying
    @Query("UPDATE House AS house SET house.watchTimes = house.watchTimes + 1 WHERE house.id = :id")
    void updateWatchTimes(@Param(value = "id") Long houseId);

}
