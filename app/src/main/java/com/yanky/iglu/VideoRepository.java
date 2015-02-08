package com.yanky.iglu;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton for getting the app's video configuration.
 * Created by Yanky on 08/02/2015.
 */
public class VideoRepository {

    private static VideoRepository instance = new VideoRepository();

    protected Map<String, IVideo> videoIdToVideo = new HashMap<>();
    protected ArrayList<IVideo> videoOrder = new ArrayList<>();
    protected boolean isParsed = false;

    private VideoRepository() {
        super();
    }

    protected IVideo handleVideoTag(XmlResourceParser videosXml, Resources resources) {
        int videoSrc = videosXml.getAttributeResourceValue(null, "video", -1);
        int image = videosXml.getAttributeResourceValue(null, "image", -1);
        String title = resources.getString(videosXml.getAttributeResourceValue(null, "title", -1));
        String videoId = videosXml.getAttributeValue(null, "id");
        Video video = new Video(videoId, title, image, videoSrc);
        videoIdToVideo.put(videoId, video);
        videoOrder.add(video);
        return video;
    }

    protected void handleSubTextTag(XmlResourceParser videosXml, IVideo currentVideo,
                                    Resources resources) {
        String name = resources.getString(videosXml.getAttributeResourceValue(null, "value", -1));
        float time = videosXml.getAttributeFloatValue(null, "time", -1);

        VideoPart part = new VideoPart(name, time);
        currentVideo.addPart(name, part);
    }

    protected void parseConfiguration(Resources resources) throws IOException,
            XmlPullParserException {
        XmlResourceParser videosXml = resources.getXml(R.xml.config);

        IVideo currentVideo = null;
        int event = videosXml.next();
        while (event != XmlResourceParser.END_DOCUMENT) {
            if (event == XmlResourceParser.START_TAG && videosXml.getName().equals("video")) {
                currentVideo = handleVideoTag(videosXml, resources);
            } else if (event == XmlResourceParser.START_TAG && videosXml.getName().
                    equals("part")) {
                handleSubTextTag(videosXml, currentVideo, resources);
            } else if (event == XmlResourceParser.END_TAG && videosXml.getName().
                    equals("video")) {
                currentVideo = null;
            }
            event = videosXml.next();
        }
        isParsed = true;
    }

    public static VideoRepository getInstance(Resources resources) throws IOException,
            XmlPullParserException {
        instance.parseConfiguration(resources);
        return instance;
    }

    public List<IVideo> getAllVideosInOrder() {
        return videoOrder;
    }

    public IVideo getVideo(String videoId) {
        return videoIdToVideo.get(videoId);
    }


}
