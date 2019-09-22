package com.example.rent.PoJo;

public class FeedBackPoJo {

    String id;
    String feedBackerId;
    String addId;

    String comment;
    String ratting;

    public FeedBackPoJo(String id, String feedBackerId, String addId, String comment, String ratting) {
        this.id = id;
        this.feedBackerId = feedBackerId;
        this.addId = addId;
        this.comment = comment;
        this.ratting = ratting;
    }

    public FeedBackPoJo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedBackerId() {
        return feedBackerId;
    }

    public void setFeedBackerId(String feedBackerId) {
        this.feedBackerId = feedBackerId;
    }

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }
}
