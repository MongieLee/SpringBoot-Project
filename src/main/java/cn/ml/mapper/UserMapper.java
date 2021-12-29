package cn.ml.mapper;

import cn.ml.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * Find users by username
     * @param username
     * @return User
     */
    @Select("select * from user where username = #{username}")
    User findUserByUsername(@Param("username") String username);

    /**
     * Create new user
     * @param username
     * @param encryptedPassword
     */
    @Select("insert into user(username,encrypted_password,created_at,updated_at) " +
            "values(#{username},#{encryptedPassword},now(),now())")
    void save(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);
}
