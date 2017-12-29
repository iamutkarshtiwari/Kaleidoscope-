package com.github.iamutkarshtiwari.kaleidoscope.models;


public class Movies {

    private String id;
    private String productName;
    private String isSoldOut;
    private String photoURL;
    private long likesCount;
    private long commentsCount;
    private long price;

    public Movies(String id, String productName, String isSoldOut, long likesCount, long commentsCount, long price, String photoURL) {
        this.id = id;
        this.productName = productName;
        this.isSoldOut = isSoldOut;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.price = price;
        this.photoURL = photoURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return productName;
    }

    public void setSoldStatus(String status) {
        this.isSoldOut = status;
    }

    public String getStatus() {
        return isSoldOut;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}