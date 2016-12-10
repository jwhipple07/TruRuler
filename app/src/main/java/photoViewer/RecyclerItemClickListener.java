package photoviewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;
    private Integer FLAG = 0;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }


    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onDown(MotionEvent e){
                FLAG = 0;
                return super.onDown(e);
            }
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            @Override public void onLongPress(MotionEvent event) {
                // triggers after onDown only for long press
                FLAG = 1;
                super.onLongPress(event);
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            switch(FLAG) {
                case 0:
                    mListener.onItemClick(childView, view.getChildPosition(childView));
                    return true;
            }
        }
        if(FLAG == 1){
            mListener.onItemLongClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}