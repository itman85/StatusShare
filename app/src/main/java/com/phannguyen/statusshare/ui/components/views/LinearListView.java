package com.phannguyen.statusshare.ui.components.views;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.phannguyen.statusshare.ui.components.base.BaseRecycleViewAdapter;


/**
 * Created by phannguyen on 11/20/15.
 */
public class LinearListView extends RecyclerView {
    private LinearLayoutManager listViewLinearLayoutManager;
    public LinearListView(Context context) {
        super(context);
        init(context);
    }

    public LinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinearListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        //listViewLinearLayoutManager = new LinearLayoutManager(context);
        //listViewLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));
        //setLayoutManager(listViewLinearLayoutManager);
        setItemAnimator(new DefaultItemAnimator());
    }

    public void enableClippingOnPaddingTop(int paddingTop){
        setPadding(0, paddingTop, 0, 0);
        setClipToPadding(false);
    }
    public BaseRecycleViewAdapter getRecycleAdapter(){
        return (BaseRecycleViewAdapter)this.getAdapter();
    }

    public void setRecycleAdapter(BaseRecycleViewAdapter adapter){
        this.setAdapter(adapter);
    }
}
