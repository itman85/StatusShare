package com.phannguyen.statusshare.datamodel.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.phannguyen.statusshare.datamodel.base.BaseItemData;
import com.phannguyen.statusshare.datamodel.service.StatusModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phannguyen on 4/13/17.
 */

public class StatusItemData extends BaseItemData implements Parcelable {
    private String statusText;
    private String authorNickname;
    private List<String> imagesList;
    private String authorEmailKey;
    private long createdServerStamp;
    private boolean isEdited;

    public StatusItemData() {
    }


    public StatusItemData(StatusModel model){
        this.statusText = model.getStatusText();
        this.createdServerStamp = model.getCreatedServerStamp();
        this.authorNickname = model.getAuthorNickname();
        this.authorEmailKey = model.getAuthorEmailKey();
        this.imagesList = model.getImagesList();
        this.itemDataKey = model.getStatusKeyId();
        this.isEdited = model.isEdited();
    }

    protected StatusItemData(Parcel in) {
        statusText = in.readString();
        authorNickname = in.readString();
        imagesList = in.createStringArrayList();
        authorEmailKey = in.readString();
        createdServerStamp = in.readLong();
        itemDataKey = in.readString();
        isEdited = in.readByte() != 0;
    }

    public static List<StatusItemData> castFromModelList(List<StatusModel> modelList){
        List<StatusItemData> res = new ArrayList<>();
        if(modelList!=null) {
            for (StatusModel mode : modelList) {
                res.add(new StatusItemData(mode));
            }
        }
        return res;
    }
    public static final Creator<StatusItemData> CREATOR = new Creator<StatusItemData>() {
        @Override
        public StatusItemData createFromParcel(Parcel in) {
            return new StatusItemData(in);
        }

        @Override
        public StatusItemData[] newArray(int size) {
            return new StatusItemData[size];
        }
    };

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }


    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public String getAuthorEmailKey() {
        return authorEmailKey;
    }

    public void setAuthorEmailKey(String authorEmailKey) {
        this.authorEmailKey = authorEmailKey;
    }

    public long getCreatedServerStamp() {
        return createdServerStamp;
    }

    public void setCreatedServerStamp(long createdServerStamp) {
        this.createdServerStamp = createdServerStamp;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StatusItemData){
            return ((StatusItemData) obj).getItemDataKey().equals(this.getItemDataKey());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.itemDataKey.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(statusText);
        dest.writeString(authorNickname);
        dest.writeStringList(imagesList);
        dest.writeString(authorEmailKey);
        dest.writeLong(createdServerStamp);
        dest.writeString(itemDataKey);
        dest.writeByte((byte) (isEdited ? 1 : 0));
    }
}
