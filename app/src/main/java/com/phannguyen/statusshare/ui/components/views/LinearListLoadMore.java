package com.phannguyen.statusshare.ui.components.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.ui.components.base.BaseRecycleViewAdapter;


/**
 * Created by phannguyen on 11/20/15.
 */
public class LinearListLoadMore extends FrameLayout {
    private static final String TAG = "LinearListLoadMore";

    private LinearListView mMainListView;
    private boolean mIsLoadingMore = false;
    private OnLoadMoreListener mOnLoadMoreListener;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;


    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
    public LinearListLoadMore(Context context) {
        super(context);
        initComponent(context, null);
    }

    public LinearListLoadMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs);
    }

    public LinearListLoadMore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs);
    }

    public void initComponent(Context context, AttributeSet attrs) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.ctrl_linearlist, this,true);
        mMainListView = (LinearListView)findViewById(R.id.mainListView);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LinearListLoadMore);
        boolean isLineDivider = ta.getBoolean(R.styleable.LinearListLoadMore_line_divider, false);
        if(isLineDivider){
            mMainListView.addItemDecoration(new LinearListItemDecorationWithDivider());
        }else{
            mMainListView.addItemDecoration(new LinearListItemDecorationWithSpace(context.getResources().getDimension(R.dimen.linearlist_space)));
        }
        ta.recycle();

        setupOnScrollLoadMoreListener();
    }

    public  void setAdapter(BaseRecycleViewAdapter adapter) {
        mMainListView.setRecycleAdapter(adapter);

    }

    private void setupOnScrollLoadMoreListener(){
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mMainListView
                .getLayoutManager();

        mMainListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnLoadMoreListener != null && dy>0) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!mIsLoadingMore &&  totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        mIsLoadingMore = true;
                        activeProgressbarLoadmore();
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void onLoadMoreComplete() {
        mIsLoadingMore = false;
        removeProgressbarLoadmore();
    }

    private void activeProgressbarLoadmore(){
        //add null , so the adapter will check view_type and show progress bar at bottom
        BaseRecycleViewAdapter adapter = mMainListView.getRecycleAdapter();
        mMainListView.post(new Runnable() {
            public void run() {
                adapter.getDataList().add(null);
                mMainListView.getAdapter().notifyItemInserted(adapter.getDataList().size() - 1);
            }
        });

    }

    private void removeProgressbarLoadmore(){
        BaseRecycleViewAdapter adapter = mMainListView.getRecycleAdapter();
        adapter.getDataList().remove(adapter.getDataList().size() - 1);
        mMainListView.getAdapter().notifyItemRemoved(adapter.getDataList().size());

    }
    public void enableClippingOnPaddingTop(int paddingTop){
        mMainListView.enableClippingOnPaddingTop(paddingTop);
    }
    public LinearListView getMainLinearListView() {
        return mMainListView;
    }

    public void scrollTo(int dy){
        mMainListView.smoothScrollBy(0, dy);
    }

    public void smoothScrollToPosition(int position){
        mMainListView.smoothScrollToPosition(position);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

    }

    public LinearListView getMainListView() {
        return mMainListView;
    }

}
