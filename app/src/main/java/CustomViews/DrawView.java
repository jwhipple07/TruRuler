package customviews;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.truruler.truruler.AssertSettings;

import junit.framework.Assert;

import java.util.Locale;

public class DrawView extends ImageView {
    private SharedPreferences sharedPreferences;
    private DisplayMetrics dm;
    private Integer type, myMinWidth, myMinHeight;;
    private Paint paint, paintText;
    private float[] points;
    private final Rect textBounds = new Rect(); //don't new this up in a draw method
    private PointF mPoint1, mPoint2;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));
        init();
    }

    public void init(){
        dm = getResources().getDisplayMetrics();
        paint = new Paint();    //This will paint the lines and background of ruler
        paintText = new Paint();    //This will paint the text that will show on the ruler

        points = new float[64];     //initiate the points array to store the paths of what to draw
        myMinWidth = dm.widthPixels;
        myMinHeight = dm.heightPixels;
        mPoint1 = new PointF(dm.widthPixels/2, dm.heightPixels - 100); //starts at canvas left top
        mPoint2 = new PointF(dm.widthPixels - (dm.widthPixels/4), dm.heightPixels - 200);//end of line
    }

    /**
     * This will make the view's default size the full screen size
     *
     * @param widthMeasureSpec - width measurement spec
     * @param heightMeasureSpec - height measurement spec
     */
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

        int textSize = 50;
        paintText.setTextSize(textSize);
        paintText.setColor(Color.BLUE);

        paint.setStrokeWidth(5);

        // Make the canvas white
        canvas.drawColor(Color.WHITE);

        // Make the brush black
        paint.setColor(Color.BLACK);

        if(AssertSettings.PRIORITY1_ASSERTIONS){
            //make sure that the type is set to 0 or 1
            Assert.assertTrue(this.type == 0 || this.type == 1);
        }
        switch (type) {
            case 0: //metric
                drawCM(false, deviceActualDpi / 2.54F, canvas);
                drawCM(true, dm.xdpi / 2.54F, canvas);
                break;
            case 1: //standard
                drawInch(true, dm.xdpi, canvas);
                drawInch(false, deviceActualDpi, canvas);
                break;
        }

        //draw instructions
        AssetManager am = getContext().getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "ExpletiveDeleted.ttf")); //set custom font for instructions
        paint.setTypeface(typeface);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(70);
        canvas.drawText("Add current measurement", ( dm.widthPixels - 45), (dm.heightPixels - 45) , paint);
        paint.setTextSize(70);
        canvas.drawText("Long click to show/hide menu", ( dm.widthPixels - 45), (dm.heightPixels/2 ) , paint);

        //Draw the arrow to point to the floating button
        Path drawPath1 = drawCurve(mPoint1, mPoint2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(drawPath1, paint);
        paint.setStyle(Paint.Style.FILL);


    }

    /**
     * To draw a curve, mPointa will be the starting point and mPointb will be the ending point.
     * @param mPointa   - starting point
     * @param mPointb   - ending point
     * @return  -the path to draw
     */
    private Path drawCurve(PointF mPointa, PointF mPointb) {
        Path myPath = new Path();
        myPath.moveTo(mPointa.x, mPointa.y);
        final float x2 = mPointa.x;
        final float y2 = mPointb.y;
        myPath.cubicTo(mPointa.x, mPointa.y, x2, y2, mPointb.x, mPointb.y);
        return myPath;
    }

    /**
     * This will draw the CM lines of the ruler. For every CM it will loop through and draw each MM line.
     *
     * @param topBar    - flag to draw the ruler on the top or the side
     * @param DPI   - dot per inch for this current drawing
     * @param canvas    - the canvas to draw on
     */
    public void drawCM(boolean topBar, float DPI, Canvas canvas) {
        float distancePixelMark = dm.widthPixels / 5F; //the max size of the line to draw is 1/5 of screen
        int val1, val2;

        //val1 and val2 are used as the index for the path's array
        val1 = topBar ? 2 : 3;
        val2 = topBar ? 3 : 2;

        for (int i = 0; i < 20; i++) { //this will draw up to 20 CM
            for (int j = 0; j < 40; j += 4) {//starting a new path to draw
                //the start of the path
                points[j] = topBar ? (i * DPI) + ((j / 4) * (DPI / 10)) : 0;
                points[j + 1] = topBar ? 0 : (i * DPI) + ((j / 4) * (DPI / 10));

                //these will be the ending points of the path
                points[j + val1] = (i * DPI) + ((j / 4) * (DPI / 10));
                switch (j / 4) {
                    case 0:
                        points[j + val2] = distancePixelMark / 1.33F; //CM line
                        break;
                    case 5:
                        points[j + val2] = distancePixelMark / 2F;  // 1/2 CM line
                        break;
                    default:
                        points[j + val2] = distancePixelMark / 4F;  // MM line
                        break;
                }
            }
            if (i != 0) { //draw the number only on the main CM line
                if(topBar) {
                    drawTextCentered(canvas, paintText, Integer.toString(i), points[2], points[3] + 25);
                } else {
                    drawTextCentered(canvas, paintText, Integer.toString(i), points[2]+25, points[3]);
                }
            }
            canvas.drawLines(points, paint);    //from the paths generated, draw all the lines
        }
    }
    public void drawInch(boolean topBar, float DPI, Canvas canvas ){
        float distancePixelMark = dm.widthPixels / 5F; //the max size of the line to draw is 1/5 of screen
        int val1, val2;

        //val1 and val2 are used as the index for the path's array
        val1 = topBar ? 2 : 3;
        val2 = topBar ? 3 : 2;

        for (int j = 0; j < 5; j++) { //This will draw up to 5 inches
            for (int i = 0; i < 64; i += 4) { //Start a new path to draw

                //the starting point of the path
                points[i] = topBar ? (j * DPI) + ((i / 4) * (DPI / 16)) : 0;
                points[i + 1] = topBar ? 0 : (j * DPI) + ((i / 4) * (DPI / 16));

                //the ending point of the path
                points[i + val1] = (j * DPI) + ((i / 4) * (DPI / 16));
                switch (i / 4) {
                    case 0:
                        points[i + val2] = distancePixelMark;   //INCH line
                        break;
                    case 4:
                    case 12:
                        points[i + val2] = distancePixelMark / 2.6F;    //  1/4 and 3/4 INCH line
                        break;
                    case 8:
                        points[i + val2] = distancePixelMark / 1.6F;    //  1/2 INCH line
                        break;
                    default:
                        points[i + val2] = distancePixelMark / 8;   // All other 1/8 INCH lines
                }
            }
            if (j != 0) {
                if(topBar) { //draw the number only on the main INCH line
                    drawTextCentered(canvas, paintText, Integer.toString(j), points[2], points[3] + 25);
                } else {
                    drawTextCentered(canvas, paintText, Integer.toString(j), points[2]+25, points[3]);
                }
            }
            canvas.drawLines(points, paint);    //from the paths generated, draw all the lines
        }
    }

    /**
     * Draw text centered horizontally and vertically at a specified point
     * @param canvas - canvas to draw on
     * @param paint - the paint to use
     * @param text - the text to draw
     * @param cx - X-point of position
     * @param cy - Y-point of the position
     */
    public void drawTextCentered(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }
}
