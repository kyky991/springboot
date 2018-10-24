package com.zing.boot.blog.service.impl;

import com.zing.boot.blog.dao.IBlogDao;
import com.zing.boot.blog.pojo.*;
import com.zing.boot.blog.service.IBlogService;
import com.zing.boot.blog.service.IEsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private IBlogDao blogDao;

    @Autowired
    private IEsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId() == null);
        EsBlog esBlog = null;

        Blog returnBlog = blogDao.save(blog);

        if (isNew) {
            esBlog = new EsBlog(returnBlog);
        } else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
        esBlogService.updateEsBlog(esBlog);

        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogDao.delete(id);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogDao.findOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";

        String tags = title;
        Page<Blog> blogs = blogDao.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, tags, user, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogDao.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogDao.findByCatalog(catalog, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogDao.findOne(id);
        blog.setReadSize(blog.getCommentSize() + 1);
        this.saveBlog(blog);
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog originalBlog = blogDao.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user, commentContent);
        originalBlog.addComment(comment);
        return saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = blogDao.findOne(blogId);
        originalBlog.removeComment(commentId);
        saveBlog(originalBlog);
    }

    @Override
    public Blog createVote(Long blogId) {
        Blog originalBlog = blogDao.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = originalBlog.addVote(vote);
        if (isExist) {
            throw new IllegalArgumentException("该用户已经点过赞了");
        }
        return saveBlog(originalBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogDao.findOne(blogId);
        originalBlog.removeVote(voteId);
        saveBlog(originalBlog);
    }
}
