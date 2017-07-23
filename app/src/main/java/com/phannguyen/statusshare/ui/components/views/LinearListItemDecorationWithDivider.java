package com.phannguyen.statusshare.ui.components.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by IT on 1/27/2016.
 */
public class LinearListItemDecorationWithDivider extends RecyclerView.ItemDecoration{
    private Paint paint;
    private int offset;

    public LinearListItemDecorationWithDivider(){
        offset = 10;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect.set(offset, offset, offset, offset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for(int i=0; i<parent.getChildCount(); i++){
            final View child = parent.getChildAt(i);
            c.drawRect(
                layoutManager.getDecoratedLeft(child),
                layoutManager.getDecoratedTop(child),
                layoutManager.getDecoratedRight(child),
                layoutManager.getDecoratedBottom(child),
                paint);
        }

    }
}
