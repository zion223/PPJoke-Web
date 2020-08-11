package com.example.demo.mapper;

import com.example.demo.table.TableFeedUgc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UgcCommentMapper {
    TableFeedUgc queryUgcByCommentId(@Param("commentId") long commentId);

    void setCommentUgc(@Param("ugc") TableFeedUgc ugc);

    int increaseCommentCount(@Param("commentId") long commentId, @Param("increaseCount") int increaseCount);

    int increaseLikeCount(@Param("commentId") long commentId, @Param("increaseCount") int increaseCount);

    int increaseShareCount(@Param("commentId") long commentId);

    int toggleCommentLike(@Param("commentId") long commentId, @Param("userId") long userId);

    Object isCommentLike(@Param("commentId") long commentId, @Param("userId") long userId);

    int deleteComment(@Param("itemId") long itemId, @Param("commentId") long commentId);

    int deleteAllComments(@Param("itemId") long itemId);
}
