package com.example.lovers_hub;

public class Users {
    String img;
    String name;
    String status;
    String thumb_image;

    public Users() {
    }

    public Users(String img, String name, String status, String thumb_image) {
        this.img = img;
        this.name = name;
        this.status = status;
        this.thumb_image = thumb_image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
