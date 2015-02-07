package com.yanky.iglu;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
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

    public final static String EXTRA_MESSAGE = "extra";

    /* --- Protected Methods --- */

    /**
     * Creates a card tag in the provided linear view, with the info stored in the open xml tag.
     *
     * @param inflater      The inflater used to inflate the card tag.
     * @param videoCardList The linear view in which to store the card.
     * @param videosXml     The xml from which to read the current tag.
     * @return An open layout to which sub text tags need to be written.
     */
    protected LinearLayout handleVideoTag(LayoutInflater inflater, LinearLayout videoCardList,
                                          XmlResourceParser videosXml) {
        Resources resources = this.getResources();
        // <Card>
        CardView cardView = (CardView) inflater.inflate(R.layout.video_card, videoCardList, false);
        final VideoListActivity activity = this;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VideoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "some message!");
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
        cardTitle.setText(resources.getString(videosXml.getAttributeResourceValue(null, "title", -1)));
        cardVerticalTextLayout.addView(cardTitle);
        // <Sub Text Layout>
        LinearLayout subTextLayout = (LinearLayout) inflater.inflate(
                R.layout.video_card_sub_text_layout, cardVerticalTextLayout, false);
        cardVerticalTextLayout.addView(subTextLayout);

        // <Image>
        int imageId = videosXml.getAttributeResourceValue(null, "image", -1);
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
     * @param videosXml         The xml from which to read the current tag.
     */
    protected void handleSubTextTag(LayoutInflater inflater, LinearLayout openSubTextLayout,
                                    XmlResourceParser videosXml) {
        Resources resources = this.getResources();
        TextView subText = (TextView) inflater.inflate(R.layout.video_card_sub_text,
                openSubTextLayout, false);
        subText.setText(resources.getString(videosXml.getAttributeResourceValue(null, "value", -1)));
        openSubTextLayout.addView(subText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        Resources resources = this.getResources();
        XmlResourceParser videosXml = resources.getXml(R.xml.config);


        final LinearLayout videoCardList = (LinearLayout) findViewById(R.id.videoCardList);
        LayoutInflater inflater = this.getLayoutInflater();

        LinearLayout openSubTextLayout = null;

        try {
            int event = videosXml.next();
            while (event != XmlResourceParser.END_DOCUMENT) {
                if (event == XmlResourceParser.START_TAG && videosXml.getName().equals("video")) {
                    openSubTextLayout = handleVideoTag(inflater, videoCardList, videosXml);
                } else if (event == XmlResourceParser.START_TAG && videosXml.getName().equals("subHeader")) {
                    handleSubTextTag(inflater, openSubTextLayout, videosXml);
                } else if (event == XmlResourceParser.END_TAG && videosXml.getName().equals("video")) {
                    openSubTextLayout = null;
                }
                event = videosXml.next();
            }
        } catch (XmlPullParserException | IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.failed_to_start)
                    .setTitle(R.string.failed_to_start_title);
            builder.create();
        }
    }
}