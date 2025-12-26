package com.example.mobilefinal;

public class Images {

    private int id;
    private byte[] image;

    public Images(int id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public Images() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
