package com.phannguyen.statusshare.ui.components.views;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.phannguyen.statusshare.global.AppPresenter;

/**
 * Created by phannguyen on 4/15/17.
 */

public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {

    public LinearLayoutManagerWithSmoothScroller(Context context) {
        super(context, VERTICAL, false);
    }

    public LinearLayoutManagerWithSmoothScroller(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {

        int h = AppPresenter.Instance().getScreenHeight();
        int dy = recyclerView.computeVerticalScrollOffset();
        //Log.e("LISTVIEW",String.format("Scroll dy=%d, h=%d, r=%d",dy,h,dy/h));
        boolean isDefaultSpeed = dy/h>9;
        RecyclerView.SmoothScroller smoothScroller = new CustomSpeedSmoothScroller(recyclerView.getContext(),isDefaultSpeed);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

    }

    private class CustomSpeedSmoothScroller extends LinearSmoothScroller {
        private static final float FAST_MILLISECONDS_PER_INCH = 5f;

        boolean isDefaultSpeed;
        public CustomSpeedSmoothScroller(Context context,boolean isDefaultSpeed) {
            super(context);
            this.isDefaultSpeed = isDefaultSpeed;

        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return LinearLayoutManagerWithSmoothScroller.this
                    .computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            if(isDefaultSpeed){
                return super.calculateSpeedPerPixel(displayMetrics);
            }else{
                return FAST_MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        }
        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}
