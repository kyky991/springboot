package com.zing.boot.es.controller;

import com.zing.boot.es.dao.EsBlogDao;
import com.zing.boot.es.pojo.EsBlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EsBlogController {

    @Autowired
    private EsBlogDao esBlogDao;

    @RequestMapping("blog")
    @ResponseBody
    public List<EsBlog> blog(String title) {
        Pageable pageable = new PageRequest(0, 10);
        Page<EsBlog> blogs = esBlogDao.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, "", "", pageable);
        return blogs.getContent();
    }
}
