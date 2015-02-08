package com.yanky.iglu;

import java.util.ArrayList;

/**
 * Interface for a playable video.
 * Created by Yanky on 08/02/2015.
 */
public interface IVideo {

    /**
     * Display text for the video.
     * @return The string to display.
     */
    public String getTitle();

    /**
     * Display image for the video.
     * @return Id for the image to display.
     */
    public int getImage();

    /**
     * Actual video to play.
     * @return Id for the video.
     */
    public int getVideo();

    public String getId();

    /**
     * Get a video part (for this video) object matching the provided string.
     * @param name The name of the part.
     * @return The video part object.
     */
    public IVideoPart getVideoPart(String name);

    public void addPart(String name, IVideoPart part);

    public ArrayList<IVideoPart> getAllPartsInOrder();

}