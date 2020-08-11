package com.example.demo.table;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "table_user")
public class TableUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(name = "user_id")
    public long userId;

    @Column(name = "name")
    public String name;

    @Column(name = "avatar", length = 1000)
    public String avatar;

    @Column(name = "description", length = 100)
    public String description;

    @Column(name = "like_count")
    public int likeCount;

    @Column(name = "top_count")
    public int topCommentCount;

    @Column(name = "follow_count")
    public int followCount;

    @Column(name = "follower_count")
    public int followerCount;

    @Column(name = "qq_openid")
    public String qqOpenId;

    @Column(name = "expires_time")
    public long expires_time;

    @Column(name = "score")
    public long score;

    @Column(name = "history_count")
    public int historyCount;

    @Column(name = "comment_count")
    public int commentCount;

    @Column(name = "favorite_count")
    public int favoriteCount;

    @Column(name = "feed_count")
    public int feedCount;

    @Transient
    public boolean hasFollow;

    public boolean isHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int historyCount) {
        this.historyCount = historyCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(int feedCount) {
        this.feedCount = feedCount;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public long getExpires_time() {
        return expires_time;
    }

    public void setExpires_time(long expires_time) {
        this.expires_time = expires_time;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getTopCommentCount() {
        return topCommentCount;
    }

    public void setTopCommentCount(int topCommentCount) {
        this.topCommentCount = topCommentCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }
}
