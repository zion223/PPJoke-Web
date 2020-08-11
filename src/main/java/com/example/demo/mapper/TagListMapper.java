package com.example.demo.mapper;

import com.example.demo.table.TableTagList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagListMapper {
    List<TableTagList> queryTagList(@Param("tagId") long tagId,
                                    @Param("pageCount") int pageCount,
                                    @Param("userId") long userId,
                                    @Param("tagType") String tagType,
                                    @Param("offset") int offset);
}
