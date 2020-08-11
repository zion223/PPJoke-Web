package com.example.demo.table;


import javax.persistence.*;

@Table(name = "table_tag_list")
@Entity
public class TableTagList {
    @Id
    @GeneratedValue
    public int id;

    @Column(name = "icon", length = 1000)
    public String icon;

    @Column(name = "background", length = 1000)
    public String background;

    @Column(name = "activity_icon", length = 1000)
    public String activityIcon;

    @Column(name = "title")
    public String title;

    @Column(name = "intro")
    public String intro;

    @Column(name = "feed_num")
    public int feedNum;

    @Column(name = "tag_id")
    public long tagId;

    @Column(name = "enter_num")
    public int enterNum;

    @Column(name = "follow_num")
    public int followNum;

    @Transient
    public boolean hasFollow;
}
