package com.zing.springboot.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zing.springboot.es.dao.EsBlogDao;
import com.zing.springboot.es.pojo.EsBlog;

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
