package me.nuwan.seofficial.Model;

import android.graphics.drawable.Drawable;

public class Feed {

    private String feedTitle;
    private String feedDesc;
    private String feedAuthor;
    private int FeedImg;

    public Feed() {
    }

    public Feed(String feedTitle, String feedDesc, String feedAuthor, int feedImg) {
        this.feedTitle = feedTitle;
        this.feedDesc = feedDesc;
        this.feedAuthor = feedAuthor;
        FeedImg = feedImg;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedDesc() {
        return feedDesc;
    }

    public void setFeedDesc(String feedDesc) {
        this.feedDesc = feedDesc;
    }

    public String getFeedAuthor() {
        return "by " + feedAuthor;
    }

    public void setFeedAuthor(String feedAuthor) {
        this.feedAuthor = feedAuthor;
    }

    public int getFeedImg() {
        return FeedImg;
    }

    public void setFeedImg(int feedImg) {
        FeedImg = feedImg;
    }
}
