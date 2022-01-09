package cn.ml.controller;

import cn.ml.entity.*;
import cn.ml.service.AuthService;
import cn.ml.service.BlogService;
import cn.ml.utils.AssertUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BlogController {
    private final AuthService authService;
    private final BlogService blogService;

    @Inject
    public BlogController(AuthService authService, BlogService blogService) {
        this.authService = authService;
        this.blogService = blogService;
    }

    @GetMapping("/blog/{blogId}")
    public BlogResult getBlog(@PathVariable Integer blogId) {
        return blogService.getBlogByBlogId(blogId);
    }

    @GetMapping("/blog")
    public BlogListResult getBlogs(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "userId", required = false) Integer userId) {
        if (page == null || page < 1) {
            page = 1;
        }
        return blogService.getBlogs(page, pageSize, userId);
    }

    @PostMapping("/blog")
    public BlogResult blog(@RequestBody HashMap<String, String> params) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.insertBlog(fromParams(params, user)))
                    .orElse(BlogResult.failure("请先登陆"));
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure(e.toString());
        }
    }

    @PatchMapping("/blog/{blogId}")
    public BlogResult updateBlog(@PathVariable("blogId") Integer blogId, @RequestBody HashMap<String, String> params) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlog(blogId, fromParams(params, user)))
                    .orElse(BlogResult.failure("请先登录"));
        } catch (Exception e) {
            return BlogResult.failure(e.toString());
        }
    }

    @DeleteMapping("/blog/{blogId}")
    public BlogResult deleteBlog(@PathVariable("blogId") Integer blogId) {
        try {
            return authService.getCurrentUser().
                    map(user -> blogService.deleteBlog(blogId, user))
                    .orElse(BlogResult.failure("请先登录"));
        } catch (Exception e) {
            return BlogResult.failure(e.toString());
        }
    }

    private Blog fromParams(Map<String, String> params, User user) {
        Blog blog = new Blog();
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");
        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid!");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");

        if (StringUtils.isNotBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }

        blog.setTitle(title);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setUser(user);
        return blog;
    }
}
