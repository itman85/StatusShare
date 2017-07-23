package com.phannguyen.statusshare.ui.components.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.phannguyen.statusshare.datamodel.base.BaseItemData;

/**
 * Created by phannguyen on 4/13/17.
 */

public abstract class  BaseRecycleViewHolder <T extends BaseItemData> extends RecyclerView.ViewHolder {
    protected BaseRecycleViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T itemdata,int pos);
    public abstract T getDataItem();
    public abstract void setDataItem(T dataItem);
}
