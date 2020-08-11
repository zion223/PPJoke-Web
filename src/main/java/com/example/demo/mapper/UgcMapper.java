package com.example.demo.mapper;

import com.example.demo.table.TableFeedUgc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UgcMapper {
    TableFeedUgc queryUgcByItemId(@Param("itemId") long itemId);

    TableFeedUgc queryUgcByCommentId(@Param("commentId") long itemId);

    int increaseCommentCount(@Param("itemId") long itemId, @Param("increaseCount") int increaseCount);

    int queryCommentCount(@Param("itemId") long itemId);

    int increaseLikeCount(@Param("itemId") long itemId, @Param("increaseCount") int increaseCount);

    int queryLikeCount(@Param("itemId") long itemId);

    int increaseShareCount(@Param("itemId") long itemId);

    int queryShareCount(@Param("itemId") long itemId);

    int toggleLike(@Param("itemId") long itemId, @Param("userId") long userId);

    Object isLiked(@Param("itemId") long itemId, @Param("userId") long userId);

    void setUgc(TableFeedUgc ugc);

    void toggleTagListFollow(@Param("tagId") long tagId, @Param("userId") long userId);

    Object hasFollowTag(@Param("tagId") long tagId, @Param("userId") long userId);

    void toggleCommentLike(@Param("commentId") long commentId, @Param("userId") long userId);

    Object isCommentLike(@Param("commentId") long commentId, @Param("userId") long userId);

    void toggleUserFollow(@Param("followUserId") long followUserId, @Param("userId") long userId);

    Object isUserFollow(@Param("followUserId") long followUserId, @Param("userId") long userId);

    void dissFeed(@Param("itemId") long itemId, @Param("userId") long userId);

    Object hasDissFeed(@Param("itemId") long itemId, @Param("userId") long userId);

    int toggleFavorite(@Param("itemId") Long itemId, @Param("userId") Long userId);

    Object hasFavorite(@Param("itemId") Long itemId, @Param("userId") Long userId);

}
