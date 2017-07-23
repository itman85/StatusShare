package com.phannguyen.statusshare.mvp.model;

import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.firebase.FibCallback;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.global.AppPresenter;
import com.phannguyen.statusshare.mvp.base.ICreateStatusMVP;
import com.phannguyen.statusshare.storage.RealmHelper;

/**
 * Created by phannguyen on 4/14/17.
 */
public class CreateStatusModel implements ICreateStatusMVP.Model {
    private ICreateStatusMVP.ModelCallback modelCallback;

    public CreateStatusModel(ICreateStatusMVP.ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }

    @Override
    public void executePostSatus(StatusModel newStatusModel, StatusModel editedStatusModel) {
        FirebaseHelper.Instance().createNewStatus(newStatusModel, new FibCallback<StatusModel>() {
            @Override
            public void onSuccess(StatusModel response) {
                if(editedStatusModel!=null) {
                    FirebaseHelper.Instance().deleteStatus(editedStatusModel);
                    RealmHelper.removeStatus(editedStatusModel.getStatusKeyId());
                }

                if(newStatusModel.getCreatedServerStamp()> AppPresenter.Instance().getLastStatusTime())
                    AppPresenter.Instance().setLastStatusTime(newStatusModel.getCreatedServerStamp());
                RealmHelper.storeStatus(response);
                modelCallback.postSatusSuccessful(response,editedStatusModel);
            }

            @Override
            public void onError(String error) {
                modelCallback.postSatusFailed(error);
            }
        });
    }
}
