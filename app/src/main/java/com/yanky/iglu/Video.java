package com.yanky.iglu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Pojo for videos.
 * Created by Yanky on 08/02/2015.
 */
public class Video implements IVideo {

    private String title, id;
    private int image, video;
    private Map<String, IVideoPart> partNamesToParts;
    private ArrayList<IVideoPart> partsOrder;

    public Video(String id, String title, int image, int video) {
        this.title = title;
        this.image = image;
        this.video = video;
        this.partNamesToParts = new HashMap<>();
        this.partsOrder = new ArrayList<>();
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getImage() {
        return image;
    }

    @Override
    public int getVideo() {
        return video;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public IVideoPart getVideoPart(String name) {
        return partNamesToParts.get(name);
    }

    @Override
    public void addPart(String name, IVideoPart part) {
        partNamesToParts.put(name, part);
        partsOrder.add(part);
    }

    @Override
    public ArrayList<IVideoPart> getAllPartsInOrder() {
        return partsOrder;
    }
}
