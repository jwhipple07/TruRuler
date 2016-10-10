package com.truruler.truruler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GestureDetector mGestureDetector;
    private android.support.design.widget.CoordinatorLayout layout;
    private draggableMeasureView draggable;
    private draggableMeasureView draggableVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Bind the gestureDetector to GestureListener
        mGestureDetector = new GestureDetector(this, new GestureListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // draw(R.id.imageviewTest);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        com.truruler.truruler.DrawView main = (com.truruler.truruler.DrawView) findViewById(R.id.DrawView);
        main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    if (getSupportActionBar().isShowing()) {
                        getSupportActionBar().hide();
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        getSupportActionBar().show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                } catch (NullPointerException ne){
                    Toast.makeText(getBaseContext(), "There is no action bar", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        layout = (android.support.design.widget.CoordinatorLayout)findViewById(R.id.appBarMain);
        draggable = (draggableMeasureView) findViewById(R.id.DragView);
        draggable.setOnTouchListener(new MyTouchListener());
        draggableVertical = (draggableMeasureView) findViewById(R.id.DragViewVertical);
        draggableVertical.verticalFlag = true;
        draggableVertical.setOnTouchListener(new MyTouchListener2());
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        ((TextView)findViewById(R.id.todaysdate)).setText(currentDateTimeString);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Toast.makeText(getBaseContext(),"This is empty for now", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_calibrate) {
            Intent intent = new Intent(this, ResizeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_new_measurement) {
            Intent intent = new Intent(this, MeasurementsOverviewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(getBaseContext(),"This is empty for now", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(getBaseContext(),"This is empty for now", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(getBaseContext(),"This is empty for now", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private final class MyTouchListener implements View.OnTouchListener {
        int orgX, orgY;
        int offsetX, offsetY;
        int orgYView;
        int orgWidth, orgHeight;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    orgX = (int) event.getRawX();
                    orgY = (int) event.getRawY();

                    orgWidth = v.getMeasuredWidth();
                    orgHeight = v.getMeasuredHeight();

                    orgYView = (int)v.getY();

                    break;
                case MotionEvent.ACTION_MOVE:
                    offsetX = (int)event.getRawX() - orgX;
                    offsetY = (int)event.getRawY() - orgY;
                    android.support.design.widget.CoordinatorLayout.LayoutParams layoutParams = (android.support.design.widget.CoordinatorLayout.LayoutParams) v
                            .getLayoutParams();
                    Integer finalPlace = orgYView + offsetY;
                    if(finalPlace < 0){
                        finalPlace = 0;
                    } else if(finalPlace > dm.heightPixels - 150){
                        finalPlace = dm.heightPixels - 150;
                    }
                    layoutParams.topMargin = finalPlace;
                    v.setLayoutParams(layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            layout.invalidate();
            return true;
        }
    }

    private final class MyTouchListener2 implements View.OnTouchListener {
        int orgX, orgY;
        int offsetX, offsetY;
        int orgXView;
        int orgWidth, orgHeight;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    orgX = (int) event.getRawX();
                    orgY = (int) event.getRawY();

                    orgWidth = v.getMeasuredWidth();
                    orgHeight = v.getMeasuredHeight();

                    orgXView = (int)v.getX();

                    break;
                case MotionEvent.ACTION_MOVE:
                    offsetX = (int)event.getRawX() - orgX;
                    offsetY = (int)event.getRawY() - orgY;
                    android.support.design.widget.CoordinatorLayout.LayoutParams layoutParams = (android.support.design.widget.CoordinatorLayout.LayoutParams) v
                            .getLayoutParams();
                    Integer finalPlace = orgXView + offsetX;
                    if(finalPlace < 0){
                        finalPlace = 0;
                    } else if(finalPlace > dm.widthPixels - 150){
                        finalPlace = dm.widthPixels - 150;
                    }
                    layoutParams.leftMargin = finalPlace;
                    v.setLayoutParams(layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            layout.invalidate();
            return true;
        }
    }
}
