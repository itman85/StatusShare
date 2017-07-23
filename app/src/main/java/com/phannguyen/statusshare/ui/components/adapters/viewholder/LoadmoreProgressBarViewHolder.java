package com.phannguyen.statusshare.ui.components.adapters.viewholder;

import android.view.View;
import android.widget.ProgressBar;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.base.BaseItemData;
import com.phannguyen.statusshare.ui.components.base.BaseRecycleViewHolder;

/**
 * Created by phannguyen on 4/17/17.
 */

public class LoadmoreProgressBarViewHolder extends BaseRecycleViewHolder {
    public ProgressBar progressBar;

    public LoadmoreProgressBarViewHolder(View v) {
        super(v);
        progressBar = (ProgressBar) v.findViewById(R.id.loadmore_progress_bar);
    }

    @Override
    public void bind(BaseItemData itemdata, int pos) {
        //do nothing
    }

    @Override
    public BaseItemData getDataItem() {
        return null;
    }

    @Override
    public void setDataItem(BaseItemData dataItem) {
        //do nothing
    }
}
