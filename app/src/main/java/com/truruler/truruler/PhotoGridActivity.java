package com.truruler.truruler;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import photoGrid.PhotoGridAdapter;
import photoGrid.PhotoHolder;
import photoGrid.PhotoItem;

public class PhotoGridActivity extends ActionBarActivity {
    private GridView gridView;
    private PhotoGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new PhotoGridAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PhotoItem item = (PhotoItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(PhotoGridActivity.this, PhotoDetailsActivity.class);
                //set photoHolder to pass data
                PhotoHolder.setPhoto(item);

                //Start details activity
                startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<PhotoItem> getData() {
        final ArrayList<PhotoItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new PhotoItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }
}
