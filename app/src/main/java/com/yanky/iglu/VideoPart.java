package com.yanky.iglu;

/**
 * Pojo for video parts.
 * Created by Yanky on 08/02/2015.
 */
public class VideoPart implements IVideoPart {

    private float seekValue;
    private String name;

    public VideoPart(String name, float seekValue) {
        this.seekValue = seekValue;
        this.name = name;
    }

    @Override
    public float getSeekValue() {
        return seekValue;
    }

    @Override
    public String getName() {
        return name;
    }
}
