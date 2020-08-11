package com.example.demo.mapper;

import com.example.demo.table.TableComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    TableComment queryTopComment(@Param("itemId") long itemId);

    TableComment queryComment(@Param("itemId") long itemId);

    List<TableComment> queryCommentList(@Param("itemId") long itemId,
                                        @Param("id") int id,
                                        @Param("pageCount") int pageCount);

    boolean addComment(@Param("comment") TableComment comment);

    TableComment queryCommentByUserId(@Param("itemId") long itemId, @Param("userId") long userId, @Param("offset") int offset);

    void addWatchHistory(@Param("userId") long userId, @Param("itemId") long itemId, @Param("time") long time);
}
