package com.yanky.iglu;

/**
 * Interface for a video part.
 * Created by Yanky on 08/02/2015.
 */
public interface IVideoPart {

    /**
     * Get the part's place in the video stream.
     * @return The seek value to set.
     */
    public float getSeekValue();

    public String getName();

}
