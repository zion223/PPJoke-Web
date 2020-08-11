package com.example.demo.table;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "table_hot_feeds")
public class TableHotFeeds implements Serializable {
    @Id
    @GeneratedValue
    public int id;

    @Column(name = "item_id")
    public long itemId;

    @Column(name = "item_type")
    public int itemType;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "duration", length = 10)
    public double duration;

    @Column(name = "feeds_text", length = 1000)
    public String feeds_text;

    @Column(name = "author_id")
    public long authorId;

    @Column(name = "activity_icon", length = 1000)
    public String activityIcon;

    @Column(name = "activity_text")
    public String activityText;

    @Column(name = "video_width")
    public int width;

    @Column(name = "video_height")
    public int height;

    @Column(name = "video_url", length = 1000)
    public String url;

    @Column(name = "video_cover", length = 1000)
    public String cover;

    @Transient
    public TableUser author;

    @Transient
    public TableComment topComment;

    @Transient
    public TableFeedUgc ugc;
}
