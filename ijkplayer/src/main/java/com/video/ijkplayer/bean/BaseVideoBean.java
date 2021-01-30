package com.video.ijkplayer.bean;

/**
 * 历史视频和收藏视频通用
 */
public class BaseVideoBean {
    private String id;
    private String title;
    private String coverImage;
    private String url;
    private String duration;  //时长
    private int commentNums;  //评论次数
    private int watchMode;
    private long watchNums; //观看次数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(int commentNums) {
        this.commentNums = commentNums;
    }

    public int getWatchMode() {
        return watchMode;
    }

    public void setWatchMode(int watchMode) {
        this.watchMode = watchMode;
    }

    public long getWatchNums() {
        return watchNums;
    }

    public void setWatchNums(long watchNums) {
        this.watchNums = watchNums;
    }
}
