package com.example.demo.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "table_feeds_comment")
@JsonIgnoreProperties({"TEXT", "IMAGE", "VIDEO"})
public class TableComment {

    public static final int TEXT = 1;
    public static final int IMAGE = 2;
    public static final int VIDEO = 3;

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "item_id")
    public long itemId;

    @Column(name = "comment_id")
    public long commentId;

    @Column(name = "user_id")
    public long userId;

    //1 文字 2图片 3视频
    @Column(name = "comment_type")
    public int commentType;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "comment_count")
    public int commentCount;

    @Column(name = "like_count")
    public int likeCount;

    @Column(name = "comment_text", length = 200)
    public String commentText;

    @Column(name = "image_url", length = 1000)
    public String imageUrl;

    @Column(name = "video_url", length = 1000)
    public String videoUrl;

    @Column(name = "width")
    public int width;

    @Column(name = "height")
    public int height;

    @Transient
    public boolean hasLiked;

    @Transient
    public TableUser author;

    @Transient
    public TableFeedUgc ugc;
}
