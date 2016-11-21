package com.truruler.truruler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import CustomViews.DrawView;
import CustomViews.draggableMeasureView;
import measurements.MeasurementsDetailActivity;
import measurements.MeasurementsOverviewActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.widget.FrameLayout layout;
    private draggableMeasureView draggable;
    private draggableMeasureView draggableVertical;
    private android.support.design.widget.FloatingActionButton floatButton;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //set layout
        layout = (android.widget.FrameLayout)findViewById(R.id.mainLayout);

        //The horizontal drag measurer
        draggable = (draggableMeasureView) findViewById(R.id.DragView);
        draggable.setOnTouchListener(new horizontalDragViewListener());
        draggable.bringToFront();

        //The vertical drag measurer
        draggableVertical = (draggableMeasureView) findViewById(R.id.DragViewVertical);
        draggableVertical.verticalFlag = true;
        draggableVertical.setOnTouchListener(new verticalDragViewListener());

        //custom toolbar for the drawer theme
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatButton = (FloatingActionButton) findViewById(R.id.fab);

        //Save measurement by clicking the + symbol
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MeasurementsDetailActivity.class);
                i.putExtra("width", draggable.currentLocation);
                i.putExtra("height", draggableVertical.currentLocation);
                startActivity(i);
            }
        });

        //Drawer layout stuff
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //automatically hide the menu bar and status bar on load
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //toggle between showing the drag bars or the menu bar by long click
        DrawView main = (DrawView) findViewById(R.id.DrawView);
        main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    if (getSupportActionBar().isShowing()) {
                        getSupportActionBar().hide();
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        draggableVertical.setVisibility(View.VISIBLE);
                        draggable.setVisibility(View.VISIBLE);
                        floatButton.setVisibility(View.VISIBLE);
                    } else {
                        getSupportActionBar().show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        draggableVertical.setVisibility(View.INVISIBLE);
                        draggable.setVisibility(View.INVISIBLE);
                        floatButton.setVisibility(View.INVISIBLE);
                    }
                } catch (NullPointerException ne){
                    Toast.makeText(getBaseContext(), "There is no action bar", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, PhotoGridActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calibrate) {
            Intent intent = new Intent(this, ResizeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_new_measurement) {
            Intent intent = new Intent(this, MeasurementsOverviewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_edit_photo) {
            Intent intent = new Intent(this, DrawOnPhotoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_take_photo) {
            Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

            //folder stuff
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
            if(imagesFolder.mkdirs()){
                Log.i("","Folder created");
            }

            File image = new File(imagesFolder, "QR_" + timeStamp + ".png");
            Uri uriSavedImage = FileProvider.getUriForFile(MainActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    image);

            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
//        else if (id == R.id.nav_send) {
//            Intent intent = new Intent(this, StickerTest.class);
//            startActivity(intent);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This listener is for the horizontal drag view
     */
    private final class horizontalDragViewListener implements View.OnTouchListener {
        int orgX, orgY;
        int offsetX, offsetY;
        int orgYView;
        int orgWidth, orgHeight;


        DisplayMetrics dm = getResources().getDisplayMetrics();
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            draggableMeasureView draggable = (draggableMeasureView) v;
            v.bringToFront();
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
                    android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) v
                            .getLayoutParams();
                    Integer finalPlace = orgYView + offsetY;
                    if(finalPlace < 0){
                        finalPlace = 0;
                    } else if(finalPlace > dm.heightPixels - 100){
                        finalPlace = dm.heightPixels - 100;
                    }
                    layoutParams.topMargin = finalPlace;
                    draggable.setConversion(finalPlace);
                    v.setLayoutParams(layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            layout.invalidate();
            return true;
        }
    }

    /**
     * This listener is for the vertical drag view
     */
    private final class verticalDragViewListener implements View.OnTouchListener {
        int orgX, orgY;
        int offsetX, offsetY;
        int orgXView;
        int orgWidth, orgHeight;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            draggableMeasureView draggable = (draggableMeasureView) v;
            v.bringToFront();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    orgX = (int) event.getRawX();
                    orgY = (int) event.getRawY();
                    orgWidth = v.getMeasuredWidth();
                    orgHeight = v.getMeasuredHeight();

                    orgXView = (int)v.getX();

                    break;
                case MotionEvent.ACTION_MOVE:
                    offsetX = (int) event.getRawX() - orgX;
                    offsetY = (int) event.getRawY() - orgY;
                    android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) v
                            .getLayoutParams();
                    Integer finalPlace = orgXView + offsetX;
                    if (finalPlace < 0) {
                        finalPlace = 0;
                    } else if (finalPlace > dm.widthPixels - 100) {
                        finalPlace = dm.widthPixels - 100;
                    }
                    layoutParams.leftMargin = finalPlace;
                    v.setLayoutParams(layoutParams);
                    draggable.setConversion(finalPlace);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            layout.invalidate();
            return true;
        }
    }


}
