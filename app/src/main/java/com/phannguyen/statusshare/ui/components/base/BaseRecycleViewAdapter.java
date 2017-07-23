package com.phannguyen.statusshare.ui.components.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.phannguyen.statusshare.datamodel.base.BaseItemData;

import java.util.List;

/**
 * Created by phannguyen on 4/13/17.
 */

public abstract class BaseRecycleViewAdapter <T extends BaseItemData> extends RecyclerView.Adapter<BaseRecycleViewHolder> {
    protected Context context;
    protected final int VIEW_ITEM = 1;
    protected final int VIEW_PROG = 0;
    public abstract T getItem(int position);

    public abstract void setDataList(List<T> dataList);

    public abstract List<T> getDataList();

    /**
     * Add the items to the current list on last
     *
     * @param dataList
     */
    public abstract void addItemsOnLast(List<T> dataList);

    public abstract void addItemsOnLast(T data);

    /**
     * Add the items to the current list on first
     *
     * @param dataList
     */
    public abstract void addItemsOnFirst(List<T> dataList);
    public abstract void addItemsOnFirst(T data);

    public abstract void removeItem(T data);
}