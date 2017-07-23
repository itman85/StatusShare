package com.phannguyen.statusshare.ui.components.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by phannguyen on 11/15/15.
 */
public class LinearListItemDecorationWithSpace extends RecyclerView.ItemDecoration {
    private int halfSpace;

    public LinearListItemDecorationWithSpace(float space) {
        this.halfSpace = (int)space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        /*if (parent.getPaddingLeft() != halfSpace) {
            parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
            parent.setClipToPadding(false);
        }*/
        outRect.top = halfSpace;
        outRect.bottom = halfSpace;
        outRect.left = halfSpace;
        outRect.right = halfSpace;
    }
}

