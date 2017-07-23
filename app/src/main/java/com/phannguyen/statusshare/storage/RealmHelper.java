package com.phannguyen.statusshare.storage;

import android.os.Handler;

import com.phannguyen.statusshare.datamodel.service.StatusModel;

import java.util.List;

import io.realm.Realm;

/**
 * Created by phannguyen on 4/16/17.
 */

public class RealmHelper {
    public static void storeStatus(final StatusModel statusModel){
        new Handler().post(()->{
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(r->{
                        r.insertOrUpdate(statusModel);
                },
                    realm::close,
                    e->realm.close());

        });
    }

    public static void storeStatus(final List<StatusModel> statusModelList){
        new Handler().post(()->{
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(r->{
                        r.insertOrUpdate(statusModelList);
                    },
                    realm::close,
                    e->realm.close());

        });
    }

    public static void removeStatus(String statusId){
        Realm.getDefaultInstance().executeTransactionAsync(r->{
            StatusModel result = r.where(StatusModel.class).equalTo("statusKeyId",statusId).findFirst();
            if(result!=null)
                result.deleteFromRealm();
        });
    }
}
