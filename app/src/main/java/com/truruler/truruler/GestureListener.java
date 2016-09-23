package com.truruler.truruler;

import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

class GestureListener extends GestureDetector.SimpleOnGestureListener
{

    static String currentGestureDetected = "";
    static ActionBar actionBar;

    // Override s all the callback methods of GestureDetector.SimpleOnGestureListener
    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
        currentGestureDetected="on single tap up";
    Log.d("111111111111", currentGestureDetected);
        return true;
    }
    @Override
    public void onShowPress(MotionEvent ev) {
        currentGestureDetected="on show press up";
        Log.d("111111111111", currentGestureDetected);

    }
    @Override
    public void onLongPress(MotionEvent ev) {
        currentGestureDetected="on long press";
        Log.d("111111111111", currentGestureDetected);

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        currentGestureDetected="scroll - xdistance: " + distanceX + " ydistance: " + distanceY;
        Log.d("111111111111", currentGestureDetected);

        return true;
    }
    @Override
    public boolean onDown(MotionEvent ev) {
        currentGestureDetected="on down";
        Log.d("111111111111", currentGestureDetected);

        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        currentGestureDetected="on fling velocityx: " + velocityX + " velocityY : " + velocityY;
        Log.d("111111111111", currentGestureDetected);
        return true;
    }
    @Override
    public boolean onDoubleTap(MotionEvent ev){
        currentGestureDetected="on double tap";
        Log.d("111111111111", currentGestureDetected);
        actionBar.setShowHideAnimationEnabled(true);
        if(actionBar.isShowing()){
            actionBar.hide();
        } else {
            actionBar.show();
        }
        return true;
    }

}
