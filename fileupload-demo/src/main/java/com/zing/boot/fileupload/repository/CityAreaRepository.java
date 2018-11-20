package com.zing.boot.fileupload.repository;

import com.zing.boot.fileupload.entity.CityArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityAreaRepository extends JpaRepository<CityArea,Long> {
    List<CityArea> findByPrevCodeOrderByCode(String prevCode);

    CityArea getByCode(String code);

    CityArea getById(Long id);
}
