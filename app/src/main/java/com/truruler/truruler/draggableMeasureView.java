package com.truruler.truruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by JW043373 on 10/8/2016.
 */

public class draggableMeasureView extends View {
    private DisplayMetrics dm;
    private Paint paint;
    private Integer myMinWidth, myMinHeight;

    int orgX=0, orgY=0;
    int offsetX=0, offsetY=0;

    int orgWidth=200, orgHeight=0;
    public boolean verticalFlag = false;

    public draggableMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) return;
        init();
    }

    public void init(){
        dm = getResources().getDisplayMetrics();
        paint = new Paint();
        myMinWidth = dm.widthPixels;
        myMinHeight = dm.heightPixels*2;
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

        paint.setStrokeWidth(15);
        paint.setColor(ContextCompat.getColor(getContext(),R.color.colorDragBar));

        if(verticalFlag){
            canvas.drawLine(0, orgHeight + offsetY, 0, myMinHeight, paint);

        } else {
            canvas.drawLine(0, orgHeight + offsetY, myMinWidth, orgHeight + offsetY, paint);
        }
        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), ContextCompat.getColor(getContext(),R.color.colorDragBar), ContextCompat.getColor(getContext(),R.color.colorDragHandle), Shader.TileMode.MIRROR));
        if(verticalFlag) {
            canvas.drawCircle(0, dm.heightPixels, 100, paint);
        } else {
            canvas.drawCircle(myMinWidth, orgHeight, 100, paint);
        }
    }

}


