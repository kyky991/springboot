package com.zing.boot.es;

import com.zing.boot.es.dao.EsBlogDao;
import com.zing.boot.es.pojo.EsBlog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestElastic {
    @Autowired
    private EsBlogDao esBlogDao;

    @Test
    public void testFindByTitleLikeOrContentLike() {
        // 清空所有
        esBlogDao.deleteAll();

        esBlogDao.save(new EsBlog("1","用大白话聊聊分布式系统",
                "一提起“分布式系统”，大家的第一感觉就是好高大上啊，深不可测"));
        esBlogDao.save(new EsBlog("2","Thymeleaf 3 引入了新的解析系统",
                "如果你的代码使用了 HTML5 的标准，而Thymeleaf 版本来停留在 2.x ，那么如果没有把闭合"));
        esBlogDao.save(new EsBlog("3","使用 GFM Eclipse 插件时，不在项目里面生成 HTML 文件",
                "GFM 是 GitHub Flavored Markdown Viewer 的简称，是一款对 GitHub 友好的 Markdown 编辑器 。"));

        Pageable pageable = new PageRequest(0, 20);
        Page<EsBlog> page = esBlogDao.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining("博客", "", "", pageable);
        System.out.println(page);
    }
}
