package com.phannguyen.statusshare.mvp.presenter;

import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.global.AppPresenter;
import com.phannguyen.statusshare.mvp.base.ICreateStatusMVP;
import com.phannguyen.statusshare.mvp.model.CreateStatusModel;

/**
 * Created by phannguyen on 4/14/17.
 */
public class CreateStatusPresenter implements ICreateStatusMVP.Presenter,ICreateStatusMVP.ModelCallback {
    private ICreateStatusMVP.View _view;
    private ICreateStatusMVP.Model _model;

    public CreateStatusPresenter(ICreateStatusMVP.View _view) {
        this._view = _view;
        this._model = new CreateStatusModel(this);
    }

    @Override
    public void postSatusAction(StatusItemData newStatusItemData, StatusItemData editedStatusItemData) {
        StatusModel newModel = new StatusModel(newStatusItemData);
        newModel.setDeviceId(AppPresenter.Instance().deviceId());
        StatusModel oldModel = null;
        if(editedStatusItemData!=null) {
            oldModel = new StatusModel(editedStatusItemData);
            newModel.setEdited(true);
            oldModel.setDeviceId(AppPresenter.Instance().deviceId());
        }
        _model.executePostSatus(newModel,oldModel);
    }

    @Override
    public String getAuthorName() {
        return FirebaseHelper.Instance().getUserNickName();
    }

    @Override
    public String getAuthorEmail() {
        return FirebaseHelper.Instance().getUserEmail();
    }

    @Override
    public void postSatusSuccessful(StatusModel responseStatus,StatusModel editedStatusModel) {
        StatusItemData itemData = new StatusItemData(responseStatus);
        StatusItemData editItemData = null;
        if(editedStatusModel!=null){
            editItemData = new StatusItemData(editedStatusModel);
        }
        _view.postSatusSuccessful(itemData,editItemData);
    }

    @Override
    public void postSatusFailed(String error) {
        _view.postSatusFailed(error);
    }
}
