package com.example.demo.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Table(name = "table_ugc")
@Entity
@JsonIgnoreProperties({"id", "itemId", "commentId"})
public class TableFeedUgc {
    @Id
    @GeneratedValue
    public int id;

    @Column(name = "item_id")
    public long itemId;

    @Column(name = "like_count")
    public int likeCount;

    @Column(name = "share_count")
    public int shareCount;

    @Column(name = "comment_count")
    public int commentCount;

    @Transient
    public boolean hasFavorite;

    @Transient
    public boolean hasLiked;

    @Transient
    public boolean hasdiss;

    public boolean isHasDissed() {
        return hasdiss;
    }

    public void setHasDissed(boolean hasDissed) {
        this.hasdiss = hasDissed;
    }

    public boolean isHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

}
