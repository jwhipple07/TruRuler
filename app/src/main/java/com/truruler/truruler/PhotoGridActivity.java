package com.truruler.truruler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import photoviewer.DetailActivity;
import photoviewer.GalleryAdapter;
import photoviewer.ImageModel;
import photoviewer.RecyclerItemClickListener;

public class PhotoGridActivity extends AppCompatActivity {

    private GalleryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<ImageModel> data = new ArrayList<>();
    private static File imgFolder;

    public static File getImgFolder() {
        return imgFolder;
    }

    public static void setImgFolder(File imgFolder) {
        PhotoGridActivity.imgFolder = imgFolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        setImgFolder(new File(Environment.getExternalStorageDirectory(), "Pictures"));
        try {
            for (File img : getImgFolder().listFiles()) {
                ImageModel imageModel = new ImageModel();
                imageModel.setName("Image " + img.getName());
                imageModel.setUrl(img.getAbsolutePath());
                data.add(imageModel);
            }
        } catch(Exception e){
            //no images found in folder.
            getImgFolder().mkdir();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(PhotoGridActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);

        registerForContextMenu(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(PhotoGridActivity.this, DetailActivity.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                    @Override
                    public void onItemLongClick(View view, int position){
                        final int pos = position;
                        View v = findViewById(R.id.list);
                        PopupMenu popup = new PopupMenu(PhotoGridActivity.this, v);

                        popup.getMenuInflater().inflate(R.menu.delete, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(getBaseContext(), , Toast.LENGTH_SHORT).show();
                                File fdelete = new File(data.get(pos).getUrl());
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        System.out.println("file Deleted :" + data.get(pos).getUrl());
                                    } else {
                                        System.out.println("file not Deleted :" + data.get(pos).getUrl());
                                    }
                                }
                                finish();
                                startActivity(getIntent());
                                return true;
                            }
                        });

                        popup.show();
                    }
                }));
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
