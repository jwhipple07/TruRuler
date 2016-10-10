package com.truruler.truruler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by JW043373 on 9/19/2016.
 */
public class DrawView extends ImageView {
    private SharedPreferences sharedPreferences;
    private DisplayMetrics dm;
    private Integer type;
    private Bitmap blankBitmap;
    private Paint paint;
    private float[] points;
    private Integer myMinWidth, myMinHeight;
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));
        init();
    }

    public void init(){
        dm = getResources().getDisplayMetrics();
        blankBitmap = Bitmap.createBitmap(dm, dm.widthPixels, dm.heightPixels + dm.heightPixels, Bitmap.Config.ARGB_8888);
        paint = new Paint();

        points = new float[64];
        myMinWidth = dm.widthPixels;
        myMinHeight = dm.heightPixels*2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = resolveSize(myMinWidth, widthMeasureSpec);
        int h = resolveSize(myMinHeight, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float deviceActualDpi = sharedPreferences.getFloat("ydpi", dm.ydpi);
        this.type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));
        // Put our blank bitmap on ourView
       // this.setImageBitmap(blankBitmap);

        paint.setStrokeWidth(5);
        // Make the canvas white
        canvas.drawColor(Color.LTGRAY);

        // Make the brush blue
        paint.setColor(Color.BLACK);
        float distancePixelMark = dm.widthPixels / 5F;
        switch (type) {

            case 0: //metric
                //draw cm?
                float dotsPerCMHeight = deviceActualDpi / 2.54F; // dots per cm
                for (int i = 0; i < 20; i++) {
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
                // We can change this around as well
                for (int j = 0; j < 5; j++) {
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
