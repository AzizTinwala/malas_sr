package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 24-Oct-18.
 */

public class ImageActivityBean {
    String imgUrl;
    String imgId;

    public ImageActivityBean(String imgId, String imgUrl) {
        this.imgUrl = imgUrl;
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
