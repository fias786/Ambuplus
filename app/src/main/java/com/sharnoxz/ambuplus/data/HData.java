package com.sharnoxz.ambuplus.data;

public class HData {

    private int image,expand;
    private String text;

    public HData(int image,int expend, String text) {
        this.image = image;
        this.expand = expend;
        this.text = text;
    }

    public int getExpand() {
        return expand;
    }

    public void setExpand(int expand) {
        this.expand = expand;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
