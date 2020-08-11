package com.example.demo.controller;

import com.example.demo.ApiResponse;
import com.example.demo.mapper.TagListMapper;
import com.example.demo.mapper.UgcMapper;
import com.example.demo.table.TableTagList;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/tag")
@Api(value = "帖子标签列表")
public class TagListController {
    @Resource
    TagListMapper tagListMapper;
    @Resource
    UgcMapper ugcMapper;

    @ApiOperation(value = "查询tagList数据")
    @RequestMapping(value = "queryTagList", method = RequestMethod.GET)
    @JsonView(value = TableTagList.class)
    public ApiResponse queryTagList(@RequestParam(value = "tagId", required = false, defaultValue = "0") long tagId,
                                    @RequestParam(value = "pageCount", required = false, defaultValue = "10") int pageCount,
                                    @RequestParam(value = "userId", required = false, defaultValue = "0") long userId,
                                    @RequestParam(value = "offset", required = true, defaultValue = "0") int offset,
                                    @RequestParam(value = "tagType", required = false, defaultValue = "all") String tagType) {

        List<TableTagList> lists = tagListMapper.queryTagList(tagId == 0 ? -1 : tagId, pageCount, userId, tagType, offset);
        if (userId > 0) {
            for (TableTagList tag : lists) {
                Object followTag = ugcMapper.hasFollowTag(tag.tagId, userId);
                tag.hasFollow = followTag == null ? false : ((Boolean) followTag).booleanValue();
            }
        }

        ApiResponse response = new ApiResponse();
        response.setResult(ApiResponse.STATUS_SUCCESS, null, lists);
        return response;
    }


    @ApiOperation(value = "变更对某个标签的喜欢")
    @RequestMapping(value = "toggleTagFollow")
    @JsonView(value = Boolean.class)
    public ApiResponse toggleTagFollow(@RequestParam(value = "tagId", required = false, defaultValue = "0") long tagId,
                                       @RequestParam(value = "userId", required = false, defaultValue = "0") long userId) {

        ApiResponse response = new ApiResponse();
        if (tagId == 0 || userId == 0) {
            response.setResult(ApiResponse.STATUS_FAILED, "tagId 或 userId 不能为空");
            return response;
        }

        ugcMapper.toggleTagListFollow(tagId, userId);
        Object followTag = ugcMapper.hasFollowTag(tagId, userId);
        response.setData("hasFollow", followTag == null ? false : ((Boolean) followTag).booleanValue());
        return response;
    }
}
