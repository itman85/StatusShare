package com.phannguyen.statusshare.ui.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.phannguyen.statusshare.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phannguyen on 4/13/17.
 */

public class CustomAppBar extends FrameLayout {
    public interface IAppBarCallback{
        void onSignoutClicked();
    }
    @Bind(R.id.ivSignout)
    protected ImageView mIvSignout;

    @Bind(R.id.tvBarTitle)
    protected TextView mTvTitle;

    private IAppBarCallback mAppBarCallback;

    public CustomAppBar(Context context) {
        super(context);
        init(context,null);
    }

    public CustomAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomAppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.ctrl_appbar, this, true);
        ButterKnife.bind(this, this);

        mIvSignout.setOnClickListener(mOnClickListener);
    }

    public void setBarTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setBarTitle(int titleId) {
        mTvTitle.setText(titleId);
    }

    public void setAppBarCallback(IAppBarCallback appBarCallback){
       this.mAppBarCallback = appBarCallback;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ivSignout:
                    if(mAppBarCallback != null){
                        mAppBarCallback.onSignoutClicked();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
