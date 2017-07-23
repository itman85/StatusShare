package com.phannguyen.statusshare.ui.components.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;

import com.phannguyen.statusshare.utils.FnUtils;
import com.webianks.library.PopupBubble;

/**
 * Created by phannguyen on 4/16/17.
 */

public class CustomPopupBubble extends PopupBubble {
    public CustomPopupBubble(Context context) {
        super(context);
    }

    public CustomPopupBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPopupBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void hide() {
        FnUtils.popout(this, 1000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        }).start();
    }

    public boolean isShowing(){
        if(this.getVisibility()==VISIBLE)
            return true;
        return false;
    }

}
