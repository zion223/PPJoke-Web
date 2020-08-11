package com.example.demo.mapper;

import com.example.demo.table.TableHotFeeds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedsMapper {

    List<TableHotFeeds> queryHotFeedsList(@Param("feedType") String feedType,
                                          @Param("inId") int id,
                                          @Param("pageCount") int pageCount);

    int addFeed(@Param("feed") TableHotFeeds feed);

    List<TableHotFeeds> queryProfileFeeds(@Param("userId") long userId,
                                          @Param("pageCount") int pageCount,
                                          @Param("profileType") String profileType,
                                          @Param("feedId") int feedId);

    int deleteFeed(@Param("itemId") long itemId);

    TableHotFeeds queryFeed(@Param("itemId") long itemId);

    List<TableHotFeeds> queryHistory(@Param("userId") Long userId, @Param("pageCount") int pageCount, @Param("feedId") int feedId);

    List<TableHotFeeds> queryFavorite(@Param("userId") Long userId, @Param("pageCount") int pageCount, @Param("feedId") int feedId);
}
