package com.phannguyen.statusshare.ui.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.phannguyen.statusshare.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phannguyen on 4/13/17.
 */

public class StatusActionPopupLayout extends Spinner {
    public interface IStatusActionCallback {
        void onStatusEdited();
        void onStatusDeleted();
    }
    private IStatusActionCallback mStatusActionCallback;
    private List<String> mStatusActionList = new ArrayList<>();
    public StatusActionPopupLayout(Context context) {
        super(context);
        init();
    }

    public StatusActionPopupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusActionPopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mStatusActionList.add(getResources().getString(R.string.edit_text));
        mStatusActionList.add(getResources().getString(R.string.delete_text));
        setAdapter(new StatusActionAdapter());
    }

    public void setStatusActionCallback(IStatusActionCallback noteActionCallback){
        this.mStatusActionCallback = noteActionCallback;
    }
    /**
     * Adapter to inflate all the phone
     * Also start to call
     */
    private class StatusActionAdapter extends BaseAdapter {
        @Override
        public Object getItem(int i) {
            return mStatusActionList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            return mStatusActionList != null ? mStatusActionList.size(): 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = View.inflate(getContext(), R.layout.ctrl_simple_text, null);
            }
            final TextView tvStatusAction = (TextView)view.findViewById(R.id.tvSimpleText);
            tvStatusAction.setText(getItem(i).toString());
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mStatusActionCallback != null){
                        if(tvStatusAction.getText().equals(getResources().getString(R.string.edit_text))){
                            mStatusActionCallback.onStatusEdited();
                        }else if(tvStatusAction.getText().equals(getResources().getString(R.string.delete_text))){
                            mStatusActionCallback.onStatusDeleted();
                        }
                    }


                    try {
                        Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
                        method.setAccessible(true);
                        method.invoke(StatusActionPopupLayout.this);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            return view;
        }
    }
}

