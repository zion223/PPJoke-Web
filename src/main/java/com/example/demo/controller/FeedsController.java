package com.example.demo.controller;


import com.example.demo.ApiResponse;
import com.example.demo.mapper.*;
import com.example.demo.table.TableComment;
import com.example.demo.table.TableFeedUgc;
import com.example.demo.table.TableHotFeeds;
import com.example.demo.table.TableUser;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/feeds")
@Api(value = "帖子列表流控制器")
public class FeedsController {
    @Resource
    FeedsMapper feedsMapper;

    @Resource
    UserMapper userMapper;
    @Resource
    CommentMapper commentMapper;

    @Resource
    UgcMapper ugcMapper;

    @Resource
    UgcCommentMapper ugcCommentMapper;

    @ApiOperation(value = "查询帖子列表数据")
    @RequestMapping(value = "queryHotFeedsList", method = RequestMethod.GET)
    @JsonView(value = TableHotFeeds.class)
    public ApiResponse<List<TableHotFeeds>> queryHotFeedsList(@RequestParam(value = "feedType", required = false, defaultValue = "") String feedType,
                                         @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                         @RequestParam(value = "feedId", required = false, defaultValue = "0") Integer feedId,
                                         @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount) {

        List<TableHotFeeds> feeds = feedsMapper.queryHotFeedsList(feedType, feedId, pageCount);
        if (feeds != null && feeds.size() > 0) {
            fillFeedList(userId, feeds);
        }
        ApiResponse<List<TableHotFeeds>> response = new ApiResponse<>();
        response.setResult(ApiResponse.STATUS_SUCCESS, null, feeds);
        return response;
    }


    @RequestMapping(value = "/queryProfileFeeds", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户的动态帖子", notes = "根据id来查询")
    @JsonView(value = TableHotFeeds.class)
    public ApiResponse<Object> queryProfileFeeds(@RequestParam(value = "userId", required = true) long userId,
                                         @RequestParam(value = "pageCount", required = false, defaultValue = "10") int pageCount,
                                         @RequestParam(value = "profileType", required = true) String profileType,
                                         @RequestParam(value = "feedId", required = true, defaultValue = "0") int feedId) {

        ApiResponse<Object> response = new ApiResponse<>();
        List<TableHotFeeds> tableHotFeeds = feedsMapper.queryProfileFeeds(userId, pageCount, profileType, feedId <= 0 ? Integer.MAX_VALUE : feedId);
        HashMap<Long, List<Long>> tempMap = new HashMap<>();
        if (tableHotFeeds != null) {
            for (TableHotFeeds feed : tableHotFeeds) {
                List<Long> comments = tempMap.get(feed.itemId);
                if (comments == null) {
                    comments = new ArrayList<>();
                    tempMap.put(feed.itemId, comments);
                }
                int offset = comments.size();
                TableComment comment = commentMapper.queryCommentByUserId(feed.itemId, userId, offset);
                if (comment != null) {
                    comment.author = userMapper.queryUser(comment.userId);
                    comment.ugc = ugcCommentMapper.queryUgcByCommentId(comment.commentId);
                    if (comment.ugc != null) {
                        Object commentLike = ugcCommentMapper.isCommentLike(comment.commentId, userId);
                        comment.ugc.hasLiked = commentLike == null ? false : (Boolean) commentLike;
                    }
                    feed.topComment = comment;

                    comments.add(comment.commentId);
                }
                feed.author = userMapper.queryUser(feed.authorId);
                feed.ugc = ugcMapper.queryUgcByItemId(feed.itemId);
                if (feed.ugc != null && userId > 0) {
                    Object liked = ugcMapper.isLiked(feed.itemId, userId);
                    Object favorite = ugcMapper.hasFavorite(feed.itemId, userId);
                    feed.ugc.hasLiked = liked != null && (Boolean) liked;
                    feed.ugc.hasFavorite = favorite != null && (Boolean) favorite;
                }
            }
        }

        tempMap.clear();
        response.setResult(ApiResponse.STATUS_SUCCESS, null, tableHotFeeds);
        return response;
    }


    @ApiOperation(value = "发布一条新的帖子")
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    @JsonView(value = TableHotFeeds.class)
    public ApiResponse<String> publishFeed(@RequestParam(value = "feedType", required = false, defaultValue = "1") int feedType,
                                   @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                   @RequestParam(value = "coverUrl", required = false, defaultValue = "") String coverUrl,
                                   @RequestParam(value = "fileUrl", required = false, defaultValue = "") String fileUrl,
                                   @RequestParam(value = "fileWidth", required = false, defaultValue = "0") int fileWidth,
                                   @RequestParam(value = "fileHeight", required = false, defaultValue = "0") int fileHeight,
                                   @RequestParam(value = "tagId", required = false, defaultValue = "0") long tagId,
                                   @RequestParam(value = "tagTitle", required = false, defaultValue = "") String tagTitle,
                                   @RequestParam(value = "feedText", required = false, defaultValue = "") String feedText) {

        TableUser tableUser = userMapper.queryUser(userId);
        if (tableUser == null) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setResult(ApiResponse.STATUS_FAILED, "用户未登录,请先登陆");
            return response;
        }

        TableHotFeeds feed = new TableHotFeeds();
        feed.itemId = System.currentTimeMillis();
        feed.itemType = feedType;
        feed.authorId = userId;
        feed.cover = feedType == 1 ? StringUtils.isEmpty(fileUrl) ? null : fileUrl : coverUrl;
        feed.url = feedType == 1 ? null : fileUrl;
        feed.width = fileWidth;
        feed.height = fileHeight;
        feed.activityIcon = null;
        feed.activityText = StringUtils.isEmpty(tagTitle) ? null : tagTitle;
        feed.feeds_text = StringUtils.isEmpty(feedText) ? null : feedText;
        feed.createTime = System.currentTimeMillis();


        TableFeedUgc ugc = new TableFeedUgc();
        feed.ugc = ugc;
        ugc.itemId = feed.itemId;
        ugcMapper.setUgc(ugc);
        int result = feedsMapper.addFeed(feed);

        ApiResponse<String> response = new ApiResponse<>();
        if (result != -1) {
            response.setData("result", "success");
        } else {
            response.setData("result", "failed");
        }
        return response;
    }

    @RequestMapping(value = "deleteFeed", method = RequestMethod.GET)
    @ApiOperation(value = "删除一条帖子")
    public ApiResponse<Object> deleteFeed(@RequestParam("itemId") Long itemId) {
        ApiResponse<Object> response = new ApiResponse<>();
        if (itemId == null) {
            response.setData("result", "itemId不能为空");
            return response;
        }
        int result1 = feedsMapper.deleteFeed(itemId);
        int result2 = ugcCommentMapper.deleteAllComments(itemId);
        response.setData("result", result1 >= 0 && result2 >= 0);
        return response;
    }

    @RequestMapping(value = "queryUserBehaviorList", method = RequestMethod.GET)
    @ApiOperation(value = "查询历史观看记录，或者收藏的记录")
    public ApiResponse<List<TableHotFeeds>> queryUserBehaviorList(@RequestParam("userId") Long userId,
                                             @RequestParam(value = "pageCount", defaultValue = "10", required = false) int pageCount,
                                             @RequestParam(value = "feedId", defaultValue = "0") int feedId,
                                             @RequestParam(value = "behavior", defaultValue = "0") int behavior) {
        if (behavior == 0) {
            return queryFavorite(userId, pageCount, feedId);
        } else {
            return queryHistory(userId, pageCount, feedId);
        }
    }


    @RequestMapping(value = "queryHistory", method = RequestMethod.GET)
    @ApiOperation(value = "查询历史观看记录")
    public ApiResponse<List<TableHotFeeds>> queryHistory(@RequestParam("userId") Long userId,
                                    @RequestParam(value = "pageCount", defaultValue = "10", required = false) int pageCount,
                                    @RequestParam(value = "feedId", defaultValue = "0") int feedId) {
        ApiResponse<List<TableHotFeeds>> response = new ApiResponse<>();
        if (userId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "userId不能为空", null);
            return response;
        }
        List<TableHotFeeds> history = feedsMapper.queryHistory(userId, pageCount, feedId <= 0 ? Integer.MAX_VALUE : feedId);
        if (history != null) {
            fillFeedList(userId, history);
        }
        response.setResult(ApiResponse.STATUS_FAILED, null, history);
        return response;
    }


    @RequestMapping(value = "queryFavorite", method = RequestMethod.GET)
    @ApiOperation(value = "查询收藏记录")
    public ApiResponse<List<TableHotFeeds>> queryFavorite(@RequestParam("userId") Long userId,
                                     @RequestParam(value = "pageCount", defaultValue = "10", required = false) int pageCount,
                                     @RequestParam("feedId") int feedId) {
        ApiResponse<List<TableHotFeeds>> response = new ApiResponse<>();
        if (userId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "userId不能为空", null);
            return response;
        }
        List<TableHotFeeds> favorite = feedsMapper.queryFavorite(userId, pageCount, feedId <= 0 ? Integer.MAX_VALUE : feedId);
        if (favorite != null) {
            fillFeedList(userId, favorite);
        }
        response.setResult(ApiResponse.STATUS_FAILED, null, favorite);
        return response;
    }

    private void fillFeedList(@RequestParam("userId") Long userId, List<TableHotFeeds> list) {
        for (TableHotFeeds feed : list) {
            feed.author = userMapper.queryUser(feed.authorId);
            feed.ugc = ugcMapper.queryUgcByItemId(feed.itemId);
            Object userFollow = ugcMapper.isUserFollow(userId, feed.authorId);
            feed.author.hasFollow = userFollow == null ? false : ((Boolean) userFollow);
            if (feed.ugc != null && userId > 0) {
                Object liked = ugcMapper.isLiked(feed.itemId, userId);
                Object favorite = ugcMapper.hasFavorite(feed.itemId, userId);
                Object hasdiss = ugcMapper.hasDissFeed(feed.itemId, userId);
                feed.ugc.hasdiss = hasdiss == null ? false : (Boolean) hasdiss;
                feed.ugc.hasLiked = liked == null ? false : (Boolean) liked;
                feed.ugc.hasFavorite = favorite != null && (Boolean) favorite;
            }
            TableComment topComment = commentMapper.queryTopComment(feed.itemId);
            if (topComment != null) {
                TableFeedUgc feedUgc = ugcCommentMapper.queryUgcByCommentId(topComment.commentId);
                if (feedUgc != null) {
                    Object commentLike = ugcCommentMapper.isCommentLike(topComment.commentId, userId);
                    feedUgc.hasLiked = commentLike == null ? false : ((Boolean) commentLike);
                    topComment.ugc = feedUgc;
                }
                topComment.author = userMapper.queryUser(topComment.userId);
                feed.topComment = topComment;
            }
        }
    }
}
