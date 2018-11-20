package com.zing.boot.fileupload.repository;

import com.zing.boot.fileupload.entity.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long> {
    ResourceFile findFirstByMd5Value(String md5Value);

    ResourceFile getById(Long id);
}
