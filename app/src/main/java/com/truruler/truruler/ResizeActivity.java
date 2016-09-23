package com.truruler.truruler;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ResizeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        float storedYdpi = sharedPreferences.getFloat("ydpi", dm.ydpi);
        //create size of credit card
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, (int)( 2.125*storedYdpi));

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
                sharedPreferences.edit().putFloat("ydpi", dm.ydpi).commit();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, (int)( 2.125*dm.ydpi));

                View draggable = findViewById(R.id.draggable);
                draggable.setLayoutParams(params);
            }
        });
    }
    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        int orgX, orgY;
        int offsetX, offsetY;

        int orgWidth, orgHeight;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    orgX = (int) event.getRawX();
                    orgY = (int) event.getRawY();

                    orgWidth = v.getMeasuredWidth();
                    orgHeight = v.getMeasuredHeight();

                    break;
                case MotionEvent.ACTION_MOVE:
                    offsetX = (int)event.getRawX() - orgX;
                    offsetY = (int)event.getRawY() - orgY;

                    //resize PopWindow
                    v.setLayoutParams(new RelativeLayout.LayoutParams(
                            orgWidth,
                            orgHeight + offsetY));
                    break;
                case MotionEvent.ACTION_UP:
                    //get final width
                    int j = v.getMeasuredHeight();

                    float actualYdpi = j / 2.125F;
                    Toast.makeText(getApplicationContext(), "height: " + actualYdpi , Toast.LENGTH_SHORT).show();

                    sharedPreferences.edit().putFloat("ydpi", actualYdpi).commit();
                    break;
            }
            return true;
        }
    }
}
