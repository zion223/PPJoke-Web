package com.example.demo.controller;

import com.example.demo.ApiResponse;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.UgcCommentMapper;
import com.example.demo.mapper.UgcMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.table.TableComment;
import com.example.demo.table.TableUser;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/comment")
@Api(value = "帖子的评论列表查询")
public class CommentController {

    @Resource
    CommentMapper commentMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    UgcMapper ugcMapper;

    @Resource
    UgcCommentMapper ugcCommentMapper;

    @ApiOperation(value = "用于查询帖子的评论列表 支持分页")
    @RequestMapping(value = "queryFeedComments", method = RequestMethod.GET)
    @JsonView(value = TableComment.class)
    public ApiResponse queryFeedComments(@RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                         @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount, @RequestParam(value = "itemId", required = true) Long itemId,
                                         @RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {

        ApiResponse response = new ApiResponse();
        if (itemId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "itemId不能为空");
            return response;
        }

        List<TableComment> comments = commentMapper.queryCommentList(itemId, id <= 0 ? Integer.MAX_VALUE : id, pageCount);
        if (comments != null && comments.size() > 0) {
            for (TableComment comment : comments) {
                comment.author = userMapper.queryUser(comment.userId);
                comment.ugc = ugcCommentMapper.queryUgcByCommentId(comment.commentId);
                if (comment.ugc != null) {
                    Object ugcCommentLike = ugcCommentMapper.isCommentLike(comment.commentId, userId);
                    comment.ugc.hasLiked = ugcCommentLike == null ? false : (Boolean) ugcCommentLike;
                }
            }
        }
        response.setResult(ApiResponse.STATUS_SUCCESS, null, comments);

        if (userId != 0) {
            TableUser user = userMapper.queryUser(userId);
            if (user != null) {
                user.historyCount = user.historyCount + 1;
                userMapper.insertUser(user);
            }
        }

        if (userId != 0) {
            commentMapper.addWatchHistory(userId, itemId, System.currentTimeMillis());
        }
        return response;
    }


    @ApiOperation(value = "用于帖子增加一条评论")
    @PostMapping(value = "addComment")
    @JsonView(value = TableComment.class)
    public ApiResponse addComment(@RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                  @RequestParam(value = "itemId", required = true) Long itemId,
                                  @RequestParam(value = "commentText", required = false, defaultValue = "") String commentText,
                                  @RequestParam(value = "image_url", required = false, defaultValue = "") String image_url,
                                  @RequestParam(value = "video_url", required = false, defaultValue = "") String video_url,
                                  @RequestParam(value = "width", required = false, defaultValue = "") int width,
                                  @RequestParam(value = "height", required = false, defaultValue = "") int height) {

        ApiResponse response = new ApiResponse();
        if (itemId == null || userId == null || StringUtils.isEmpty(commentText)) {
            response.setResult(ApiResponse.STATUS_FAILED, "itemId|userId|commentText不能为空");
            return response;
        }

        int commentType = TableComment.TEXT;
        if (!StringUtils.isEmpty(video_url)) {
            commentType = TableComment.VIDEO;
        } else if (!StringUtils.isEmpty(image_url)) {
            commentType = TableComment.IMAGE;
        }

        TableComment comment = new TableComment();
        comment.userId = userId;
        comment.itemId = itemId;
        comment.commentText = commentText;
        comment.commentType = commentType;
        comment.commentId = System.currentTimeMillis() * 1000;
        comment.createTime = System.currentTimeMillis();
        comment.imageUrl = image_url;
        comment.videoUrl = video_url;
        comment.width = width;
        comment.height = height;

        commentMapper.addComment(comment);
        ugcMapper.increaseCommentCount(itemId, 1);

        comment.author = userMapper.queryUser(userId);
        response.setResult(ApiResponse.STATUS_SUCCESS, null, comment);


        TableUser user = userMapper.queryUser(userId);
        if (user != null) {
            user.commentCount = user.commentCount + 1;
            userMapper.insertUser(user);
        }

        return response;
    }


    @ApiOperation(value = "删除帖子的一条评论")
    @RequestMapping(value = "deleteComment", method = RequestMethod.GET)
    @JsonView(value = TableComment.class)
    public ApiResponse deleteComment(@RequestParam(value = "itemId", defaultValue = "0") Long itemId,
                                     @RequestParam(value = "userId", defaultValue = "0") Long userId,
                                     @RequestParam(value = "commentId", defaultValue = "0") Long commentId) {

        ApiResponse response = new ApiResponse();
        if (itemId == null || commentId == null) {
            response.setResult(ApiResponse.STATUS_FAILED, "commentId|itemId不能为空");
            return response;
        }
        int result = ugcCommentMapper.deleteComment(itemId, commentId);
        ugcMapper.increaseCommentCount(itemId, -1);

        TableUser user = userMapper.queryUser(userId);
        if (user != null) {
            user.commentCount = user.commentCount - 1;
            userMapper.insertUser(user);
        }

        if (result > 0) {
            response.setData("result", true);
        } else {
            response.setData("result", false);
        }
        return response;
    }
}
