package cn.ml.dao;

import cn.ml.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private Map<String, Object> asMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }

    public Blog selectBlogById(Integer id) {
        return sqlSession.selectOne("selectBlogById", asMap("id", id));
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        return sqlSession.selectList("getBlogs", asMap("offset", (page - 1) * pageSize, "limit", pageSize, "userId", userId));
    }

    public Blog insertBlog(Blog blog) {
        sqlSession.insert("insertBlog", blog);
        return sqlSession.selectOne("selectBlogById", blog.getId());
    }

    public Blog updateBlog(Blog blog) {
        sqlSession.update("updateBlog", blog);
        return sqlSession.selectOne("selectBlogById", blog.getId());
    }

    public Integer count(Integer userId){
        return sqlSession.selectOne("countBlog", asMap("userId", userId));
    }

    public void deleteBlog(Integer blogId) {
        sqlSession.delete("deleteBlog",asMap("blogId",blogId));
    }
}
