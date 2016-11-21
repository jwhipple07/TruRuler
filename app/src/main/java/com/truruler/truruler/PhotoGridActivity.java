package com.truruler.truruler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import photoViewer.DetailActivity;
import photoViewer.GalleryAdapter;
import photoViewer.ImageModel;
import photoViewer.RecyclerItemClickListener;

public class PhotoGridActivity extends AppCompatActivity {

    private GalleryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<ImageModel> data = new ArrayList<>();
    public static File imgFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        imgFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        try {
            for (File img : imgFolder.listFiles()) {
                ImageModel imageModel = new ImageModel();
                imageModel.setName("Image " + img.getName());
                imageModel.setUrl(img.getAbsolutePath());
                data.add(imageModel);
            }
        } catch(Exception e){
            //no images found in folder.
            imgFolder.mkdir();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(PhotoGridActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(PhotoGridActivity.this, DetailActivity.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));

    }

}
