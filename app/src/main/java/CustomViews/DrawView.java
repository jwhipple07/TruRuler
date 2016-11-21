package CustomViews;

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


/**
 *
 * Created by JW043373 on 9/19/2016.
 */
public class DrawView extends ImageView {
    private SharedPreferences sharedPreferences;
    private DisplayMetrics dm;
    private Integer type;
    private Paint paint, paintText;
    private float[] points;
    private Integer myMinWidth, myMinHeight;
    private final Rect textBounds = new Rect(); //don't new this up in a draw method
    PointF mPoint1, mPoint2;
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));
        init();
    }

    public void init(){
        dm = getResources().getDisplayMetrics();
        paint = new Paint();
        paintText = new Paint();

        points = new float[64];
        myMinWidth = dm.widthPixels;
        myMinHeight = dm.heightPixels*2;
        mPoint1 = new PointF(dm.widthPixels/2, dm.heightPixels - 100); //starts at canvas left top
        mPoint2 = new PointF(dm.widthPixels - (dm.widthPixels/4), dm.heightPixels - 200);//end of line
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

        int textSize = 50;
        paintText.setTextSize(textSize);
        paintText.setColor(Color.BLUE);

        paint.setStrokeWidth(5);
        // Make the canvas white
        canvas.drawColor(Color.WHITE);

        // Make the brush blue
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

        Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "ExpletiveDeleted.ttf"));
        paint.setTypeface(typeface);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(70);
        canvas.drawText("Add current measurement", ( dm.widthPixels - 45), (dm.heightPixels - 45) , paint);
        paint.setTextSize(70);
        canvas.drawText("Long click to show/hide menu", ( dm.widthPixels - 45), (dm.heightPixels/2 ) , paint);


        Path drawPath1 = drawCurve(mPoint1, mPoint2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(drawPath1, paint);
        paint.setStyle(Paint.Style.FILL);


    }
    private Path drawCurve(PointF mPointa, PointF mPointb) {
        Path myPath = new Path();
        myPath.moveTo(mPointa.x, mPointa.y);
        final float x2 = mPointa.x;
        final float y2 = mPointb.y;
        myPath.cubicTo(mPointa.x, mPointa.y, x2, y2, mPointb.x, mPointb.y);
        return myPath;
    }

    public void drawCM(boolean topBar, float DPI, Canvas canvas) {
        float distancePixelMark = dm.widthPixels / 5F;
        int val1, val2;
        if(topBar){
            val1 = 2;
            val2 = 3;
        } else {
            val1 =3;
            val2 = 2;
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 40; j += 4) {
                if(topBar){
                    points[j] = (i * DPI) + ((j / 4) * (DPI / 10));
                    points[j + 1] = 0;
                } else {
                    points[j] = 0;
                    points[j + 1] = (i * DPI) + ((j / 4) * (DPI / 10));
                }
                points[j + val1] = (i * DPI) + ((j / 4) * (DPI / 10));
                switch (j / 4) {
                    case 0:
                        points[j + val2] = distancePixelMark / 1.33F;
                        break;
                    case 5:
                        points[j + val2] = distancePixelMark / 2F;
                        break;
                    default:
                        points[j + val2] = distancePixelMark / 4F;
                        break;
                }
            }
            if (i != 0) {
                if(topBar) {
                    drawTextCentered(canvas, paintText, Integer.toString(i), points[2], points[3] + 25);
                } else {
                    drawTextCentered(canvas, paintText, Integer.toString(i), points[2]+25, points[3]);
                }
            }
            canvas.drawLines(points, paint);
        }
    }
    public void drawInch(boolean topBar, float DPI, Canvas canvas ){
        float distancePixelMark = dm.widthPixels / 5F;
        int val1, val2;
        if(topBar){
            val1 = 2;
            val2 = 3;
        } else {
            val1 =3;
            val2 = 2;
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 64; i += 4) {
                if(topBar) {
                    points[i] = (j * DPI) + ((i / 4) * (DPI / 16));
                    points[i + 1] = 0;
                } else {
                    points[i + 1] = (j * DPI) + ((i / 4) * (DPI / 16));
                    points[i] = 0;
                }
                points[i + val1] = (j * DPI) + ((i / 4) * (DPI / 16));
                switch (i / 4) {
                    case 0:
                        points[i + val2] = distancePixelMark;
                        break;
                    case 4:
                    case 12:
                        points[i + val2] = distancePixelMark / 2.6F;
                        break;
                    case 8:
                        points[i + val2] = distancePixelMark / 1.6F;
                        break;
                    default:
                        points[i + val2] = distancePixelMark / 8;
                }
            }
            if (j != 0) {
                if(topBar) {
                    drawTextCentered(canvas, paintText, Integer.toString(j), points[2], points[3] + 25);
                } else {
                    drawTextCentered(canvas, paintText, Integer.toString(j), points[2]+25, points[3]);
                }
            }
            canvas.drawLines(points, paint);
        }
    }

    public void drawTextCentered(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }
}
