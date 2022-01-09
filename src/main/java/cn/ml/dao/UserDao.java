package cn.ml.dao;

import cn.ml.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao {
    SqlSession sqlSession;

    @Inject
    public UserDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private Map<String, Object> asMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }

    public User findUserById(Integer userId) {
        return sqlSession.selectOne("selectUserById", asMap("userId", userId));
    }

    public User findUserByUsername(String username) {
        return sqlSession.selectOne("selectUserByUsername", asMap("username", username));
    }

    public User updateUser(User user) {
        sqlSession.update("updateUser", user);
        return findUserById(user.getId());
    }

    public void saveUser(String username, String encryptedPassword) {
        sqlSession.insert("saveUser", asMap("username", username, "encryptedPassword", encryptedPassword));
    }
}
