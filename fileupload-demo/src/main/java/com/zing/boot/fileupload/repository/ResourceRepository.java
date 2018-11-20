package com.zing.boot.fileupload.repository;

import com.zing.boot.fileupload.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource getById(long lid);

    Page<Resource> findAll(Pageable pageable);
}
