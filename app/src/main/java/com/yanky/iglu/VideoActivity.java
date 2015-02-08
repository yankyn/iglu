package com.yanky.iglu;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class VideoActivity extends ActionBarActivity {

    /**
     * Returns a completely ready video view with the video sent in the intent..
     * @return The video view created.
     */
    protected VideoView setupVideo(VideoRepository repository) throws IOException, XmlPullParserException {
        Intent intent = getIntent();
        String videoId = intent.getStringExtra(VideoListActivity.VIDEO_ID);
        int videoSrc = repository.getVideo(videoId).getVideo();
        MediaController mediaControls = new MediaController(this);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        try {
            videoView.setMediaController(mediaControls);
            videoView.setVideoURI(
                    Uri.parse("android.resource://" + getPackageName() + "/" + videoSrc));
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.failed_to_find_video)
                    .setTitle(R.string.failed_to_find_video);
            builder.create();
        }
        return videoView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoView videoView = null;
        try {
            VideoRepository repository = VideoRepository.getInstance(this.getResources());
            videoView = setupVideo(repository);
            videoView.requestFocus();
        } catch (IOException | XmlPullParserException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.failed_to_start)
                    .setTitle(R.string.failed_to_start_title);
            builder.create();
        }
    }
}
