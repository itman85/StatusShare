package com.phannguyen.statusshare.datamodel.ui;

import com.phannguyen.statusshare.datamodel.base.BaseItemData;

/**
 * Created by phannguyen on 4/13/17.
 */

public class ImageItemData extends BaseItemData {
    private String imageUrl;
    private boolean selectedStatus = false;

    public ImageItemData() {
    }
    public ImageItemData(String url) {
        this.imageUrl = url;
    }

    public ImageItemData(String imageUrl, boolean selectedStatus) {
        this.imageUrl = imageUrl;
        this.selectedStatus = selectedStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(boolean selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ImageItemData){
            if(((ImageItemData) obj).getImageUrl().equals(this.getImageUrl()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.imageUrl.hashCode();
    }
}
