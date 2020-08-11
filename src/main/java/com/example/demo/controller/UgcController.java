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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/ugc")
@Api(value = "ugc相关接口")
public class UgcController {
    @Resource
    UgcMapper ugcMapper;

    @Resource
    UgcCommentMapper ugcCommentMapper;

    @Resource
    CommentMapper commentMapper;

    @Resource
    FeedsMapper feedsMapper;

    @Resource
    UserMapper userMapper;

    @RequestMapping(value = "/queryUgcByItemId", method = RequestMethod.GET)
    @ApiOperation(value = "根据itemId查询段子的ugc属性")
    public ApiResponse queryUgcByItemId(Long itemId) {
        ApiResponse response = new ApiResponse();
        if (itemId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "itemId 不能为空");
            return response;
        }
        TableFeedUgc feedUgc = ugcMapper.queryUgcByItemId(itemId);
        response.setResult(ApiResponse.STATUS_SUCCESS, null, feedUgc);
        return response;
    }


//    @RequestMapping(value = "/queryUgcByCommentId", method = RequestMethod.GET)
//    @ApiOperation(value = "根据commentId查询评论的ugc属性")
//    public ApiResponse queryUgcByCommentId(@RequestParam("commentId") Long commentId) {
//        ApiResponse response = new ApiResponse();
//        if (commentId == null) {
//            response.setResult(ApiResponse.STATUS_FAILED, "commentId 不能为空");
//            return response;
//        }
//        TableFeedUgc feedUgc = commentMapper.queryUgcByCommentId(commentId);
//        feedUgc.hasLiked = commentMapper.isCommentLike()
//        response.setResult(ApiResponse.STATUS_SUCCESS, null, feedUgc);
//        return response;
//    }


    @RequestMapping(value = "/increaseCommentCount", method = RequestMethod.GET)
    @ApiOperation(value = "增加段子评论的数量")
    public ApiResponse increaseCommentCount(Long itemId) {
        ApiResponse response = new ApiResponse();
        if (itemId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "itemId  不能为空");
            return response;
        }
        ugcMapper.increaseCommentCount(itemId, 1);
        int commentCount = ugcMapper.queryCommentCount(itemId);
        response.setData("count", commentCount);
        return response;
    }

    @RequestMapping(value = "toggleFavorite", method = RequestMethod.GET)
    @ApiOperation(value = "收藏一个帖子", notes = "根据itemId来收藏一个帖子")
    @JsonView(value = Boolean.class)
    public ApiResponse toggleFavorite(@RequestParam(value = "itemId", defaultValue = "0") Long itemId, @RequestParam(value = "userId", defaultValue = "0") Long userId) {
        ApiResponse response = new ApiResponse();
        if (itemId == 0 || userId == 0) {
            response.setData("result", "itemId|userId 不能为空");
            return response;
        }
        TableUser user = userMapper.queryUser(userId);
        ugcMapper.toggleFavorite(itemId, userId);
        Object hasFavorite = ugcMapper.hasFavorite(itemId, userId);
        boolean result = hasFavorite == null ? false : (boolean) hasFavorite;
        if (result) {
            user.favoriteCount = user.favoriteCount + 1;
            response.setData("hasFavorite", true);
        } else {
            user.favoriteCount = user.favoriteCount - 1;
            response.setData("hasFavorite", false);
        }
        userMapper.insertUser(user);
        return response;

    }

    @RequestMapping(value = "/increaseLikeCount", method = RequestMethod.GET)
    @ApiOperation(value = "增加一条段子喜欢的数量")
    public ApiResponse increaseLikeCount(Long itemId) {
        ApiResponse response = new ApiResponse();
        if (itemId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "itemId 不能为空");
            return response;
        }
        ugcMapper.increaseLikeCount(itemId, 1);
        int likeCount = ugcMapper.queryLikeCount(itemId);
        response.setData("count", likeCount);
        return response;
    }

    @RequestMapping(value = "/increaseShareCount", method = RequestMethod.GET)
    @ApiOperation(value = "增加一条段子分享的数量")
    public ApiResponse increaseShareCount(Long itemId) {
        ApiResponse response = new ApiResponse();
        if (itemId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "itemId 不能为空");
            return response;
        }
        ugcMapper.increaseShareCount(itemId);
        int shareCount = ugcMapper.queryShareCount(itemId);
        response.setData("count", shareCount);
        return response;
    }

    @RequestMapping(value = "/toggleFeedLike", method = RequestMethod.GET)
    @ApiOperation(value = "变换用户对该条段子的喜欢结果")
    public ApiResponse toggleLike(Long itemId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (itemId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "itemId或者userId不能为空");
            return apiResponse;
        }

        TableHotFeeds feed = feedsMapper.queryFeed(itemId);
        if (feed != null) {
            TableUser user = userMapper.queryUser(userId);
            if (user != null) {
                Object liked = ugcMapper.isLiked(itemId, userId);
                boolean hasLiked = liked == null ? false : ((Boolean) liked).booleanValue();
                if (hasLiked) {
                    ugcMapper.dissFeed(itemId, userId);
                } else {
                    ugcMapper.toggleLike(itemId, userId);
                }
                ugcMapper.increaseLikeCount(itemId, hasLiked ? -1 : 1);
                apiResponse.setData("hasLiked", !hasLiked);
                user.likeCount = !hasLiked ? user.likeCount + 1 : user.likeCount - 1;
                userMapper.insertUser(user);
            } else {
                apiResponse.setResult(ApiResponse.STATUS_FAILED, String.format("不存在userId=%s 的用户", userId));
            }
        } else {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, String.format("不存在itemId=%s的帖子", itemId));
        }

        return apiResponse;
    }

    @RequestMapping(value = "/isLiked", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户对该条段子的喜欢结果")
    public ApiResponse isLiked(Long itemId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (itemId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "itemId或者userId不能为空");
            return apiResponse;
        }
        Object liked = ugcMapper.isLiked(itemId, userId);
        boolean hasLiked = liked == null ? false : ((Boolean) liked).booleanValue();
        apiResponse.setData("hasLiked", hasLiked);
        return apiResponse;
    }


    @RequestMapping(value = "/toggleTagListFollow", method = RequestMethod.GET)
    @ApiOperation(value = "变换用户对该标签类型的喜欢结果")
    public ApiResponse toggleTagListFollow(Long tagId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (tagId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "tagId或者userId不能为空");
            return apiResponse;
        }

        ugcMapper.toggleTagListFollow(tagId, userId);
        Object liked = ugcMapper.hasFollowTag(tagId, userId);
        boolean hasLiked = liked == null ? false : ((Boolean) liked).booleanValue();
        apiResponse.setData("hasLiked", hasLiked);
        return apiResponse;
    }


    @RequestMapping(value = "/hasFollowTag", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户对该标签类型的喜欢结果")
    public ApiResponse hasFollowTag(Long tagId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (tagId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "tagId或者userId不能为空");
            return apiResponse;
        }

        Object liked = ugcMapper.hasFollowTag(tagId, userId);
        boolean hasLiked = liked == null ? false : ((Boolean) liked).booleanValue();
        apiResponse.setData("hasLiked", hasLiked);
        return apiResponse;
    }


    @RequestMapping(value = "/toggleCommentLike", method = RequestMethod.GET)
    @ApiOperation(value = "变更用户对一条评论的喜欢状态")
    public ApiResponse toggleCommentLike(Long commentId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (commentId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "commentId或者userId不能为空");
            return apiResponse;
        }


        ugcCommentMapper.toggleCommentLike(commentId, userId);
        Object commentLike = ugcMapper.isCommentLike(commentId, userId);
        boolean hasLiked = commentLike == null ? false : ((Boolean) commentLike).booleanValue();
        ugcCommentMapper.increaseLikeCount(commentId, hasLiked ? 1 : -1);
        apiResponse.setData("hasLiked", hasLiked);

        TableComment comment = commentMapper.queryComment(commentId);
        if (comment != null && comment.author != null) {
            TableUser user = userMapper.queryUser(comment.userId);
            if (user != null) {
                if (hasLiked) {
                    user.likeCount = user.likeCount + 1;
                } else {
                    user.likeCount = user.likeCount - 1;
                }
                userMapper.insertUser(user);
            }
        }

        return apiResponse;
    }

    @RequestMapping(value = "/isCommentLike", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户对一条评论的喜欢状态")
    public ApiResponse isCommentLike(Long commentId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (commentId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "commentId或者userId不能为空");
            return apiResponse;
        }

        Object commentLike = ugcCommentMapper.isCommentLike(commentId, userId);
        boolean hasLiked = commentLike == null ? false : ((Boolean) commentLike).booleanValue();
        apiResponse.setData("hasLiked", hasLiked);
        return apiResponse;
    }


    @RequestMapping(value = "/toggleUserFollow", method = RequestMethod.GET)
    @ApiOperation(value = "变更用户对另一个用户的喜欢状态")
    public ApiResponse toggleUserFollow(@RequestParam(value = "followUserId") Long followUserId, @RequestParam(value = "userId") Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (followUserId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "followUserId或者userId不能为空");
            return apiResponse;
        }

        ugcMapper.toggleUserFollow(followUserId, userId);
        Boolean hasFollow = (boolean) ugcMapper.isUserFollow(followUserId, userId);
        TableUser followUser = userMapper.queryUser(followUserId);
        if (followUser != null) {
            if (hasFollow) {
                followUser.followCount = followUser.followCount + 1;
            } else {
                followUser.followCount = followUser.followCount - 1;
            }
        }

        TableUser user = userMapper.queryUser(userId);
        if (user != null) {
            if (hasFollow) {
                user.followerCount = user.followerCount + 1;
            } else {
                user.followerCount = user.followerCount - 1;
            }
        }

        userMapper.insertUser(followUser);
        userMapper.insertUser(user);
        apiResponse.setData("hasLiked", hasFollow);
        return apiResponse;
    }

    @RequestMapping(value = "/isUserFollow", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户对另一个用户的喜欢状态")
    public ApiResponse isUserFollow(@RequestParam(value = "followUserId") Long followUserId, @RequestParam(value = "userId") Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (followUserId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "commentId或者userId不能为空");
            return apiResponse;
        }

        Object commentLike = ugcMapper.isUserFollow(followUserId, userId);
        boolean hasLiked = commentLike == null ? false : ((Boolean) commentLike).booleanValue();
        apiResponse.setData("hasLiked", hasLiked);
        return apiResponse;
    }


    @RequestMapping(value = "/dissFeed", method = RequestMethod.GET)
    @ApiOperation(value = "变更用户对一个帖子的diss状态")
    public ApiResponse dissFeed(Long itemId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (itemId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "itemId或者userId不能为空");
            return apiResponse;
        }

        TableHotFeeds feed = feedsMapper.queryFeed(itemId);
        if (feed != null) {
            TableUser user = userMapper.queryUser(userId);
            if (user != null) {
                user.likeCount = user.likeCount - 1;
                userMapper.insertUser(user);
            }
        }
        ugcMapper.dissFeed(itemId, userId);
        ugcMapper.increaseLikeCount(itemId, -1);
        return hasDissFeed(itemId, userId);
    }

    @RequestMapping(value = "/hasDissFeed", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户对一个帖子的diss状态")
    public ApiResponse hasDissFeed(Long itemId, Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        if (itemId == null || userId == null) {
            apiResponse.setResult(ApiResponse.STATUS_FAILED, "itemId或者userId不能为空");
            return apiResponse;
        }
        Object dissFeed = ugcMapper.hasDissFeed(itemId, userId);
        boolean hasLiked = dissFeed == null ? false : ((Boolean) dissFeed).booleanValue();
        apiResponse.setData("hasLiked", hasLiked);
        return apiResponse;
    }
}
