package com.nus.team4.mapper;
import com.nus.team4.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM t_user where username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM t_user where username = #{username} and password = #{password}")
    User findByUsernameAndPassword(String username, String password);
}
