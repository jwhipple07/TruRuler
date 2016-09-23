package com.truruler.truruler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GestureDetector mGestureDetector;
    private float inchPixelMark, distancePixelMark;
    public ImageView ourView;
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the gestureDetector to GestureListener
        mGestureDetector = new GestureDetector(this, new GestureListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        draw(R.id.imageviewTest);


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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.mainLayout);
        main.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                boolean eventConsumed=mGestureDetector.onTouchEvent(event);
                if (eventConsumed)
                {
                    GestureListener.actionBar = getSupportActionBar();
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    protected void onResume(){
        super.onResume();
        draw(R.id.imageviewTest);

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

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, ResizeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // This is our draw() method
    public void draw(int ID) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float devicePixelsHeight = getResources().getDisplayMetrics().heightPixels;
        float deviceActualDpi = getResources().getDisplayMetrics().ydpi;
        float deviceActualInchHeight = devicePixelsHeight / deviceActualDpi;

        float devicePixelsWidth = getResources().getDisplayMetrics().widthPixels;
        float deviceActualXDpi = getResources().getDisplayMetrics().xdpi;
        float deviceActualInchWidth = devicePixelsWidth / deviceActualXDpi;

        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.i("", "");

        deviceActualDpi = sharedPreferences.getFloat("ydpi", dm.ydpi);
        screenInches = (double) Math.round(screenInches * 10) / 10;


        // Declare an object of type Bitmap
        Bitmap blankBitmap;
        // Make it 600 x 600 pixels in size and an appropriate format
        blankBitmap = Bitmap.createBitmap(dm, dm.widthPixels, dm.heightPixels + dm.heightPixels, Bitmap.Config.ARGB_8888);
        // Declare an object of type canvas
        Canvas canvas;
        // Initialize it by making its surface our previously created blank bitmap
        canvas = new Canvas(blankBitmap);

        // Initialize our previously declared member object of type ImageView
        ourView = new ImageView(this);
        ourView = (ImageView) findViewById(ID);
        // Put our blank bitmap on ourView
        ourView.setImageBitmap(blankBitmap);

        // We now have a surface ready to draw on
        // But we need something to draw with

        // Declare an object of type Paint
        Paint paint;
        // Initialize it ready for painting our canvas
        paint = new Paint();
        paint.setStrokeWidth(5);
        // Make the canvas white
        canvas.drawColor(Color.LTGRAY);

        // Make the brush blue
        paint.setColor(Color.BLACK);
        distancePixelMark = devicePixelsWidth / 5F;
        switch (type) {

            case 0: //metric
                //draw cm?
                float deviceActualCMHeight = deviceActualInchHeight / 2.54F;
                float dotsPerCMHeight = deviceActualDpi / 2.54F; // dots per cm
                for (int i = 0; i < 20; i++) {
                    float[] points = new float[40];
                    for (int j = 0; j < 40; j += 4) {
                        points[j] = 0; //start x
                        points[j + 1] = (i * dotsPerCMHeight) + ((j / 4) * (dotsPerCMHeight / 10)); //start y
                        switch (j / 4) { //end x
                            case 0:
                                points[j + 2] = distancePixelMark / 1.33F;
                                break;
                            case 5:
                                points[j + 2] = distancePixelMark / 2F;
                                break;
                            default:
                                points[j + 2] = distancePixelMark / 4F;
                                break;
                        }
                        points[j + 3] = (i * dotsPerCMHeight) + ((j / 4) * (dotsPerCMHeight / 10)); //end y
                    }
                    if (i != 0) {
                        //add inch line numbers
                        int textSize = 50;
                        paint.setTextSize(textSize);
                        paint.setColor(Color.BLUE);
                        canvas.drawText(Integer.toString(i), points[2] + 10, points[3] + (textSize / 3), paint);
                        paint.setColor(Color.BLACK);
                    }
                    canvas.drawLines(points, paint);
                }
                float dotsPerCMWidth = dm.xdpi / 2.54F; // dots per cm
                for (int i = 0; i < 20; i++) {
                    float[] points = new float[40];
                    for (int j = 0; j < 40; j += 4) {
                        points[j] = (i * dotsPerCMWidth) + ((j / 4) * (dotsPerCMWidth / 10)); //start x
                        points[j + 1] = 0; //start y
                        points[j + 2] = (i * dotsPerCMWidth) + ((j / 4) * (dotsPerCMWidth / 10)); //end x
                        switch (j / 4) { //end y
                            case 0:
                                points[j + 3] = distancePixelMark / 1.33F;
                                break;
                            case 5:
                                points[j + 3] = distancePixelMark / 2F;
                                break;
                            default:
                                points[j + 3] = distancePixelMark / 4F;
                                break;
                        }
                    }
                    if (i != 0) {
                        //add inch line numbers
                        int textSize = 50;
                        paint.setTextSize(textSize);
                        paint.setColor(Color.BLUE);
                        canvas.drawText(Integer.toString(i), points[2] - textSize / 3, points[3] + (textSize), paint);
                        paint.setColor(Color.BLACK);
                    }
                    canvas.drawLines(points, paint);
                }
                break;
            case 1: //standard
                inchPixelMark = deviceActualDpi;
                // We can change this around as well
                for (int j = 0; j < 5; j++) {
                    float[] points = new float[64];

                    for (int i = 0; i < 64; i += 4) {


                        points[i] = 0; //start x
                        points[i + 1] = (j * deviceActualDpi) + ((i / 4) * (deviceActualDpi / 16)); //start y
                        switch (i / 4) { //end x
                            case 0:
                                points[i + 2] = distancePixelMark;
                                break;
                            case 4:
                            case 12:
                                points[i + 2] = distancePixelMark / 2.6F;
                                break;
                            case 8:
                                points[i + 2] = distancePixelMark / 1.6F;
                                break;
                            default:
                                points[i + 2] = distancePixelMark / 8;
                        }

                        points[i + 3] = (j * deviceActualDpi) + ((i / 4) * (deviceActualDpi / 16)); //end y
                    }
                    if (j != 0) {
                        //add inch line numbers
                        int textSize = 50;
                        paint.setTextSize(textSize);
                        paint.setColor(Color.BLUE);
                        canvas.drawText(Integer.toString(j), points[2] + 10, points[3] + (textSize / 3), paint);
                        paint.setColor(Color.BLACK);
                    }
                    canvas.drawLines(points, paint);
                }
                //draw top inch ruler
                for (int j = 0; j < 3; j++) {
                    float[] points = new float[64];

                    for (int i = 0; i < 64; i += 4) {


                        points[i] = (j * dm.xdpi) + ((i / 4) * (dm.xdpi / 16)); //start x
                        points[i + 1] = 0; //start y
                        points[i + 2] = (j * dm.xdpi) + ((i / 4) * (dm.xdpi / 16)); //end x
                        switch (i / 4) { //end y
                            case 0:
                                points[i + 3] = distancePixelMark;
                                break;
                            case 4:
                            case 12:
                                points[i + 3] = distancePixelMark / 2.6F;
                                break;
                            case 8:
                                points[i + 3] = distancePixelMark / 1.6F;
                                break;
                            default:
                                points[i + 3] = distancePixelMark / 8;
                        }
                    }
                    if (j != 0) {
                        //add inch line numbers
                        int textSize = 50;
                        paint.setTextSize(textSize);
                        paint.setColor(Color.BLUE);
                        canvas.drawText(Integer.toString(j), points[2] - textSize / 3, points[3] + (textSize), paint);
                        paint.setColor(Color.BLACK);
                    }
                    canvas.drawLines(points, paint);
                }
                break;

        }
    }

}
