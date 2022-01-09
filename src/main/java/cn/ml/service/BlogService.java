package cn.ml.service;

import cn.ml.dao.BlogDao;
import cn.ml.entity.Blog;
import cn.ml.entity.BlogListResult;
import cn.ml.entity.BlogResult;
import cn.ml.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogResult getBlogByBlogId(Integer blogId) {
        try {
            return BlogResult.success("获取成功", blogDao.selectBlogById(blogId));
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure(e.toString());
        }
    }

    public BlogListResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);
            Integer count = blogDao.count(userId);
            Integer pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogListResult.success(blogs, count, page, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult insertBlog(Blog blog) {
        try {
            return BlogResult.success("创建成功", blogDao.insertBlog(blog));
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure(e.toString());
        }
    }

    public BlogResult updateBlog(Integer blogId, Blog blog) {
        Blog targetBlog = blogDao.selectBlogById(blogId);
        if (targetBlog == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!blog.getUser().getId().equals(targetBlog.getUserId())) {
            return BlogResult.failure("该文章您无权修改");
        }
        try {
            blog.setId(blogId);
            return BlogResult.success("更新成功", blogDao.updateBlog(blog));
        } catch (Exception e) {
            return BlogResult.failure(e.toString());
        }
    }

    public BlogResult deleteBlog(Integer blogId, User user) {
        Blog targetBlog = blogDao.selectBlogById(blogId);
        if (targetBlog == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!user.getId().equals(targetBlog.getUserId())) {
            return BlogResult.failure("该文章您无权删除");
        }
        try{
            blogDao.deleteBlog(blogId);
            return BlogResult.success("删除成功");
        }catch(Exception e){
            return BlogResult.success(e.toString());
        }
    }
}
