package com.phannguyen.statusshare.datamodel.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.firebase.FirebaseConstant;
import com.phannguyen.statusshare.utils.RealmString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by phannguyen on 4/14/17.
 */

public class StatusModel extends RealmObject{
    private String statusText;
    private String authorNickname;
    private RealmList<RealmString> _imagesList;
    private String authorEmailKey;
    private long createdServerStamp;
    private boolean isEdited;
    private String deviceId;
    @PrimaryKey
    private String statusKeyId;
    @Ignore
    private List<String> imagesList;

    public StatusModel() {
    }

    public StatusModel(StatusItemData itemData){
        this.statusText = itemData.getStatusText();
        this.authorEmailKey = itemData.getAuthorEmailKey();
        this.authorNickname = itemData.getAuthorNickname();
        this.setImagesList(itemData.getImagesList());
        this.statusKeyId = itemData.getItemDataKey();
        this.isEdited = itemData.isEdited();
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }


    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public List<String> getImagesList() {
        if(imagesList!=null && imagesList.size()>0)
            return imagesList;
        else if(_imagesList!=null && _imagesList.size()>0){
            imagesList = new ArrayList<>();
            for(RealmString rStr:_imagesList){
                imagesList.add(rStr.getValue());
            }
            return imagesList;
        }
        return null;
    }

    public void setImagesList(List<String> imagesList) {
        if(imagesList!=null) {
            this.imagesList = imagesList;
            _imagesList = new RealmList<>();
            for (String str : imagesList) {
                RealmString rStr = new RealmString(str);
                _imagesList.add(rStr);
            }
        }
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public String getStatusKeyId() {
        return statusKeyId;
    }

    public void setStatusKeyId(String statusKeyId) {
        this.statusKeyId = statusKeyId;
    }

    public StatusModel(DataSnapshot snapshot) throws Exception {
        try {
            this.authorNickname = snapshot.child(FirebaseConstant.AUTHOR_NAME_FIELD).getValue(String.class);
            this.setImagesList((List<String>) snapshot.child(FirebaseConstant.IMAGES_ARRAY_FIELD).getValue());
            this.authorEmailKey = snapshot.child(FirebaseConstant.AUTHOR_EMAIL_KEY_FIELD).getValue(String.class);
            this.statusText = snapshot.child(FirebaseConstant.STATUS_TEXT_FIELD).getValue(String.class);
            this.createdServerStamp = snapshot.child(FirebaseConstant.CREATED_SERVER_STAMP_FIELD).getValue(Long.class);
            this.statusKeyId = snapshot.getKey();
            this.isEdited = snapshot.child(FirebaseConstant.STATUS_IS_EDITED_FIELD).getValue(Boolean.class);
            this.deviceId = snapshot.child(FirebaseConstant.STATUS_DEVICE_ID_FIELD).getValue(String.class);

            //Log.e("StatusModel",statusKeyId);
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(FirebaseConstant.AUTHOR_NAME_FIELD, authorNickname);
        result.put(FirebaseConstant.AUTHOR_EMAIL_KEY_FIELD, authorEmailKey);
        result.put(FirebaseConstant.CREATED_SERVER_STAMP_FIELD, ServerValue.TIMESTAMP);
        result.put(FirebaseConstant.STATUS_TEXT_FIELD, statusText);
        result.put(FirebaseConstant.IMAGES_ARRAY_FIELD, getImagesList());
        result.put(FirebaseConstant.STATUS_IS_EDITED_FIELD, isEdited);
        result.put(FirebaseConstant.STATUS_DEVICE_ID_FIELD, deviceId);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StatusModel && ((StatusModel) obj).getStatusKeyId().equals(this.getStatusKeyId());
    }

    @Override
    public int hashCode() {
        return this.getStatusKeyId().hashCode();
    }
}
