package com.phannguyen.statusshare.mvp.presenter;

import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.global.AppPresenter;
import com.phannguyen.statusshare.mvp.base.IHomeMVP;
import com.phannguyen.statusshare.mvp.model.HomeModel;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by phannguyen on 4/14/17.
 */

public class HomePresenter implements IHomeMVP.Presenter, IHomeMVP.ModelCallback{
    private IHomeMVP.View _view;
    private IHomeMVP.Model _model;
    private ReentrantLock mLoadMoreLock = new ReentrantLock();
    public HomePresenter(IHomeMVP.View _view) {
        this._view = _view;
        _model = new HomeModel(this);
    }

    @Override
    public void loadLocalStatusData() {
        _model.executeLoadLocalStatus();
    }

    @Override
    public void startListeningStatusPosted() {
        _model.executeListeningStatusPosted();
    }

    @Override
    public void stopListeningStatusPosted() {
        _model.executeStopListening();
    }

    @Override
    public void logout() {
        FirebaseHelper.Instance().logout();
        AppPresenter.Instance().clear();
    }

    @Override
    public void deleteStatus(StatusItemData statusItemData) {
        StatusModel statusModel = new StatusModel(statusItemData);
        _model.executeDeleteStatus(statusModel);
    }

    @Override
    public void loadMoreStatus() {
        if(mLoadMoreLock.isLocked()){
            return;
        }
        mLoadMoreLock.lock();
        _model.executeLoadMoreStatus();
    }

    @Override
    public void onStatusPosted(int number) {
        _view.onStatusPosted(number);
    }

    @Override
    public void onLocalStatusDataLoaded(List<StatusModel> statusModelsList) {
        _view.onLocalStatusDataLoaded(StatusItemData.castFromModelList(statusModelsList));
    }

    @Override
    public void onStatusRemoved(StatusModel statusModel) {
        StatusItemData itemData = new StatusItemData(statusModel);
        _view.onStatusRemoved(itemData);
    }

    @Override
    public void onLoadMoreStatusComplete(List<StatusModel> statusModelsList) {
        if(mLoadMoreLock.isLocked()){
            mLoadMoreLock.unlock();
        }
        _view.onLoadMoreStatusComplete(StatusItemData.castFromModelList(statusModelsList));
    }


}
