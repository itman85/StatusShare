package com.phannguyen.statusshare.ui.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by phannguyen on 4/13/17.
 */

public class RatioImageView extends ImageView {
    private static final String TAG = "RatioImageView";
    private double mHeightRatio = 0.0;


    private int mImageWidth;
    private int mImageHeight;
    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatioImageView(Context context) {
        super(context);
        init();
    }
    public RatioImageView(Context context, AttributeSet attrs, int def){
        super(context, attrs, def);
        init();
    }
    private void init(){
       //
    }
    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }
    public double getHeightRatio() {
        return mHeightRatio;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

}
