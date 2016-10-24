package com.truruler.truruler;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import photoGrid.PhotoHolder;
import photoGrid.PhotoItem;

/**
 * Created by JW043373 on 10/23/2016.
 */

public class PhotoDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        PhotoItem item = PhotoHolder.getPhoto();

        String title = item.getTitle();
        Bitmap bitmap = item.getImage();

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }
}
