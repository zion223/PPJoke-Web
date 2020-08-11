package com.example.demo.mapper;

import com.example.demo.table.TableUser;
import com.example.demo.table.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    TableUser queryUser(@Param("userId") long userId);

    TableUser queryUserByQQOpenId(@Param("qqOpenId") String qqOpenId);

    int delete(@Param("userId") long userId);

    int Update(@Param("user") TableUser user);

    int insertUser(@Param("user") TableUser user);

    List<User> queryFans(@Param("userId") long userId, @Param("offset") int offset, @Param("pageCount") int pageCount);

    List<User> queryFollows(@Param("userId") long userId, @Param("offset") int offset, @Param("pageCount") int pageCount);

}
