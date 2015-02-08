package com.yanky.iglu;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class VideoListActivity extends ActionBarActivity {

    public final static String VIDEO_ID = "videoId";

    /* --- Protected Methods --- */

    /**
     * Creates a card tag in the provided linear view, with the info stored in the open xml tag.
     *
     * @param inflater      The inflater used to inflate the card tag.
     * @param videoCardList The linear view in which to store the card.
     * @return An open layout to which sub text tags need to be written.
     */
    protected LinearLayout handleVideo(IVideo video, LinearLayout videoCardList,
                                       LayoutInflater inflater) {
        // <Card>
        CardView cardView = (CardView) inflater.inflate(R.layout.video_card, videoCardList, false);

        // Button listener
        final VideoListActivity activity = this;
        final String videoId = video.getId();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VideoActivity.class);
                intent.putExtra(VIDEO_ID, videoId);
                startActivity(intent);
            }
        });

        videoCardList.addView(cardView);
        // <Horizontal Layout>
        LinearLayout cardHorizontalLayout = (LinearLayout)
                inflater.inflate(R.layout.video_card_horizontal, cardView, false);
        cardView.addView(cardHorizontalLayout);
        // <Vertical Layout>
        LinearLayout cardVerticalTextLayout = (LinearLayout)
                inflater.inflate(R.layout.video_card_vertical_text, cardHorizontalLayout, false);
        cardHorizontalLayout.addView(cardVerticalTextLayout);
        // <Text>
        TextView cardTitle = (TextView) inflater.inflate(R.layout.video_card_title_text,
                cardVerticalTextLayout, false);
        cardTitle.setText(video.getTitle());
        cardVerticalTextLayout.addView(cardTitle);
        // <Sub Text Layout>
        LinearLayout subTextLayout = (LinearLayout) inflater.inflate(
                R.layout.video_card_sub_text_layout, cardVerticalTextLayout, false);
        cardVerticalTextLayout.addView(subTextLayout);

        for (IVideoPart part : video.getAllPartsInOrder()) {
            handleVideoPart(part, subTextLayout, inflater);
        }

        // <Image>
        int imageId = video.getImage();
        ImageView cardImage = (ImageView) inflater.inflate(R.layout.video_card_image,
                cardHorizontalLayout, false);
        cardImage.setImageResource(imageId);
        cardHorizontalLayout.addView(cardImage);
        // </Image>

        return subTextLayout;
    }

    /**
     * Creates A text view in the provided linear layout with the data stored in the open xml tag.
     *
     * @param inflater          The inflater used to inflate the card tag.
     * @param openSubTextLayout The linear view in which to store the card.
     */
    protected void handleVideoPart(IVideoPart part, LinearLayout openSubTextLayout,
                                   LayoutInflater inflater) {
        TextView subText = (TextView) inflater.inflate(R.layout.video_card_sub_text,
                openSubTextLayout, false);
        subText.setText(part.getName());
        openSubTextLayout.addView(subText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        Resources resources = this.getResources();
        final LinearLayout videoCardList = (LinearLayout) findViewById(R.id.videoCardList);
        LayoutInflater inflater = this.getLayoutInflater();
        try {
            VideoRepository repository = VideoRepository.getInstance(resources);
            for (IVideo video : repository.getAllVideosInOrder()) {
                handleVideo(video, videoCardList, inflater);
            }
        } catch (IOException | XmlPullParserException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.failed_to_start)
                    .setTitle(R.string.failed_to_start_title);
            builder.create();
        }
    }
}