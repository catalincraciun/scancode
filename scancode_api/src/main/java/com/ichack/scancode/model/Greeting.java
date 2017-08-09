package com.ichack.scancode.model;

public class Greeting {

    private final boolean success;
    private final String image;

    public Greeting(boolean success, String image) {
        this.image = image;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getImage() {
        return image;
    }
}
