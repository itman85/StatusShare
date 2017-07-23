package com.phannguyen.statusshare.mvp.base;

import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;

import java.util.List;

/**
 * Created by phannguyen on 4/14/17.
 */

public interface IHomeMVP {
    interface View {
        void onStatusPosted(int number);
        void onStatusRemoved(StatusItemData itemData);
        void onLocalStatusDataLoaded(List<StatusItemData> statusDataList);
        void onLoadMoreStatusComplete(List<StatusItemData> statusModelsList);
    }

    interface Presenter {
        void loadLocalStatusData();
        void startListeningStatusPosted();
        void stopListeningStatusPosted();
        void logout();
        void deleteStatus(StatusItemData statusItemData);
        void loadMoreStatus();

    }

    interface Model {
        void executeListeningStatusPosted();
        void executeStopListening();
        void executeLoadLocalStatus();
        void executeDeleteStatus(StatusModel statusModel);
        void executeLoadMoreStatus();

    }

    interface ModelCallback{
        void onStatusPosted(int number);
        void onLocalStatusDataLoaded(List<StatusModel> statusModelsList);
        void onStatusRemoved(StatusModel statusModel);
        void onLoadMoreStatusComplete(List<StatusModel> statusModelsList);
    }
}
