package com.phannguyen.statusshare.mvp.base;

import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;

/**
 * Created by phannguyen on 4/14/17.
 */

public interface ICreateStatusMVP {
    interface View {
        void postSatusSuccessful(StatusItemData itemData,StatusItemData editedItemData);
        void postSatusFailed(String error);

    }

    interface Presenter {
        void postSatusAction(StatusItemData newStatusItemData,StatusItemData editedStatusItemData);
        String getAuthorName();
        String getAuthorEmail();
    }

    interface Model {
        void executePostSatus(StatusModel newStatusModel,StatusModel editedStatusModel);
    }

    interface ModelCallback{
        void postSatusSuccessful(StatusModel responseStatus,StatusModel editedStatusModel);
        void postSatusFailed(String error);

    }
}
