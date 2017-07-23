package com.phannguyen.statusshare.ui.components.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.ui.components.adapters.viewholder.LoadmoreProgressBarViewHolder;
import com.phannguyen.statusshare.ui.components.adapters.viewholder.StatusFeedItemViewHolder;
import com.phannguyen.statusshare.ui.components.base.BaseRecycleViewAdapter;
import com.phannguyen.statusshare.ui.components.base.BaseRecycleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phannguyen on 4/13/17.
 */

public class StatusFeedAdapter extends BaseRecycleViewAdapter<StatusItemData>{

    public interface IStatusItemSelectedCallback {
        void onStatusEdit(StatusItemData itemData);
        void onStatusDelete(StatusItemData itemData);

    }

    private Context mContext;
    private List<StatusItemData> mStatusItemDataList;
    private IStatusItemSelectedCallback mStatusItemSelectedCallback;
    public StatusFeedAdapter(Context context,IStatusItemSelectedCallback statusItemSelectedCallback) {
        this.mContext = context;
        mStatusItemSelectedCallback = statusItemSelectedCallback;
        mStatusItemDataList = new ArrayList<>();
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ctrl_status_feed_item, parent, false);

            return new StatusFeedItemViewHolder(mContext, itemView, mStatusItemSelectedCallback);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.ctrl_loadmore_progressbar, parent, false);
            return new LoadmoreProgressBarViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(holder instanceof StatusFeedItemViewHolder) {
            ((StatusFeedItemViewHolder)holder).bind(mStatusItemDataList.get(position), position);
        }else{
            ((LoadmoreProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mStatusItemDataList != null ? mStatusItemDataList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mStatusItemDataList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    ///////////////////

    @Override
    public StatusItemData getItem(int position) {
        return mStatusItemDataList!=null?mStatusItemDataList.get(position):null;
    }

    @Override
    public void setDataList(List<StatusItemData> dataList) {
        if(dataList != null)
            mStatusItemDataList = dataList;
    }

    @Override
    public List<StatusItemData> getDataList() {
        return mStatusItemDataList;
    }

    @Override
    public void addItemsOnLast(List<StatusItemData> dataList) {
        if(dataList!=null && dataList.size()>0) {
            mStatusItemDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItemsOnLast(StatusItemData data) {
        if(data!=null) {
            mStatusItemDataList.add(data);
            //notifyDataSetChanged();
            notifyItemInserted(mStatusItemDataList.size()-1);
        }
    }

    @Override
    public void addItemsOnFirst(List<StatusItemData> dataList) {
        if(dataList != null && dataList.size()>0){
            mStatusItemDataList.addAll(0, dataList);
            notifyDataSetChanged();

        }
    }

    @Override
    public void addItemsOnFirst(StatusItemData data) {
        if(data!=null) {
            mStatusItemDataList.add(0, data);
            //notifyDataSetChanged();
            notifyItemInserted(0);
        }
    }

    @Override
    public void removeItem(StatusItemData data) {
        if(data!=null) {
            int idx = mStatusItemDataList.indexOf(data);
            if (idx > -1) {
                mStatusItemDataList.remove(idx);
                notifyItemRemoved(idx);
            }
        }

    }
}
