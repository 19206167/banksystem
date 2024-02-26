package com.nus.team4.mapper;
import com.nus.team4.pojo.User;
import org.apache.ibatis.annotations.Insert;
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

    @Select("SELECT * FROM t_user where card_id = #{cardId}")
    User findByCardId(Long cardId);

    @Insert("insert into t_user (card_id, username, password) values (#{cardId}, #{username}, #{password})")
    void createNewUser(Long cardId, String username, String password);
}
