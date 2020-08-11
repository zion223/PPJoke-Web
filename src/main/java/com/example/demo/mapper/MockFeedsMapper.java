package com.example.demo.mapper;

import com.example.demo.table.TableComment;
import com.example.demo.table.TableHotFeeds;
import com.example.demo.table.TableTagList;
import com.example.demo.table.TableUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MockFeedsMapper {

    int insertHotFeeds(TableHotFeeds feeds);


    int insertTag(TableTagList tagList);


    int insertComments(TableComment comment);


    int insertUser(TableUser user);
}
