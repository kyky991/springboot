package com.zing.springboot.blog.es.dao;

import com.zing.springboot.blog.es.pojo.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsBlogDao extends ElasticsearchRepository<EsBlog, String> {
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(String title, String summary, String content, Pageable pageable);
}
