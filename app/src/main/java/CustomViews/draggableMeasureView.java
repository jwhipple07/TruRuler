package CustomViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.truruler.truruler.R;

import java.util.Locale;

public class draggableMeasureView extends View {
    private DisplayMetrics dm;
    private Paint paint;
    private Paint paintText;
    private Integer myMinWidth, myMinHeight;
    public Float currentLocation = 0F;
    private SharedPreferences sharedPreferences;

    public boolean verticalFlag = false;

    public draggableMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) return;
        init();
    }

    public void init(){
        dm = getResources().getDisplayMetrics();
        paint = new Paint();
        paintText = new Paint();
        myMinWidth = dm.widthPixels;
        myMinHeight = dm.heightPixels*2;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = resolveSize(50, widthMeasureSpec);
        int h = resolveSize(100, 10);

        int wv = resolveSize(100, 50);
        int hv = resolveSize(100, heightMeasureSpec);
        if(verticalFlag){
            setMeasuredDimension(wv, hv);

        } else {
            setMeasuredDimension(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setStrokeWidth(5);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorDragBar));
        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), ContextCompat.getColor(getContext(),R.color.colorDragBar), ContextCompat.getColor(getContext(),R.color.colorDragHandle), Shader.TileMode.MIRROR));

        if(verticalFlag){
            canvas.drawLine(50, 0, 50, myMinHeight, paint);
            canvas.drawCircle(50, dm.heightPixels, 100, paint);
            paintText.setColor(Color.LTGRAY);
            paintText.setTextSize(50);
            paintText.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.format(Locale.ENGLISH, "%.1f", currentLocation), 50, dm.heightPixels-25, paintText);

        } else {
            canvas.drawLine(0, 50, myMinWidth, 50, paint);
            canvas.drawCircle(myMinWidth, 50, 100, paint);
            paintText.setColor(Color.LTGRAY);
            paintText.setTextSize(50);
            paintText.setTextAlign(Paint.Align.CENTER);
            canvas.save();
            canvas.rotate(90);
            canvas.drawText(String.format(Locale.ENGLISH, "%.1f", currentLocation), 50,-dm.widthPixels+50, paintText);
            canvas.restore();
        }
    }

    public void setConversion(Integer pixels){
        float deviceActualDpi;
        if(verticalFlag){
            deviceActualDpi = dm.xdpi;
        } else {
            deviceActualDpi = sharedPreferences.getFloat("ydpi", dm.ydpi);
        }
        int type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));

        switch(type){
            case 0: //metric
                this.currentLocation = (pixels+ 55)/(deviceActualDpi/2.5F) ;
                break;
            case 1:
                this.currentLocation = ((pixels+ 55)/deviceActualDpi) ;
                break;

        }

    }

}


