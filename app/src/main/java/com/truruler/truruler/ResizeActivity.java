package com.truruler.truruler;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import junit.framework.Assert;

public class ResizeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(AssertSettings.PRIORITY2_ASSERTIONS){
            Assert.assertNotNull(dm);
        }

        float storedYdpi = sharedPreferences.getFloat("ydpi", dm.ydpi);
        //create size of credit card
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, (int) (2.125 * storedYdpi));
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        View draggable = findViewById(R.id.draggable);
        draggable.setLayoutParams(params);
        // Assign the touch listener to your view which you want to move
        draggable.setOnTouchListener(new MyTouchListener());


        Button resetToDefault = (Button) findViewById(R.id.resetToDefault);
        resetToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                sharedPreferences.edit().putFloat("ydpi", dm.ydpi).apply();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, (int) (2.125 * dm.ydpi));
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                View draggable = findViewById(R.id.draggable);
                draggable.setLayoutParams(params);
            }
        });
    }

    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        int orgY, offsetY, orgYView;

        int orgWidth, orgHeight;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    orgY = (int) event.getRawY();
                    orgYView = (int) event.getY();

                    orgWidth = v.getMeasuredWidth();
                    orgHeight = v.getMeasuredHeight();

                    break;
                case MotionEvent.ACTION_MOVE:
                    if(orgYView > (orgHeight/2)) {
                        offsetY = (int) event.getRawY() - orgY;
                    } else {
                        offsetY = orgY - (int) event.getRawY();
                    }

                    //resize PopWindow
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            orgWidth, orgHeight + offsetY);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);

                    v.setLayoutParams(params);

                    break;
                case MotionEvent.ACTION_UP:
                    //get final width
                    int j = v.getMeasuredHeight();

                    if(AssertSettings.PRIORITY1_ASSERTIONS) {
                        Assert.assertTrue(j > 0);
                    }

                    float actualYdpi = j / 2.125F;
                    sharedPreferences.edit().putFloat("ydpi", actualYdpi).apply();
                    break;
            }
            return true;
        }
    }
}
