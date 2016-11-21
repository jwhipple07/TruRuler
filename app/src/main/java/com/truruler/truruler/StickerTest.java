package com.truruler.truruler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import Sticker.StickerImageView;
import Sticker.StickerTextView;
import Sticker.StickerView;

public class StickerTest extends AppCompatActivity {
    StickerImageView iv_sticker;
    StickerTextView tv_sticker;
    Button addNew;
    FrameLayout canvas;
    List<StickerView> allStickerViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        canvas = (FrameLayout) findViewById(R.id.canvasView);
        addNew = (Button) findViewById(R.id.addNewLengthView);


//// add a stickerImage to canvas
//        iv_sticker = new StickerImageView(StickerTest.this);
//        iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.arrow));

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_sticker = new StickerImageView(StickerTest.this);
                iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.arrow));
                allStickerViews.add(iv_sticker);
                canvas.addView(iv_sticker);
            }
        });

        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for(StickerView s : allStickerViews) {
                    if(s!= null)
                        s.setControlsVisibility(false);
                }
                //tv_sticker.setControlsVisibility(false);
                return true;
            }
        });
    }
}
