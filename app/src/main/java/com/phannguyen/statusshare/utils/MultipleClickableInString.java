package com.phannguyen.statusshare.utils;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class MultipleClickableInString {
    public interface IItemClickListener{
        void onItemClicked();
    }
    private String str;
    private SpannableString ss;
    private TextView tv;
    public MultipleClickableInString(TextView tv, String str){
        this.str = str;
        ss = new SpannableString(str);
        this.tv = tv;
        tv.setHighlightColor(Color.TRANSPARENT);
    }
    public void addClickableItemsInPairs(Object... objects){
        if(objects != null && objects.length > 1){
            for(int index = 0; index < objects.length; index += 2){
                addClickableItem(objects[index].toString(), (IItemClickListener)objects[index + 1]);
            }
        }
        tv.setText(ss);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void addClickableItem(String itemName, final IItemClickListener iItemClickListener){
        int itemIndexOf = str.indexOf(itemName);
        int itemIndexEnd = itemIndexOf + itemName.length();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if(iItemClickListener != null){
                    iItemClickListener.onItemClicked();
                }
            }
            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(tv.getContext(), android.R.color.holo_blue_dark));
                textPaint.setUnderlineText(true);
            }
        };
        if(itemIndexOf > -1) {
            ss.setSpan(clickableSpan, itemIndexOf, itemIndexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
