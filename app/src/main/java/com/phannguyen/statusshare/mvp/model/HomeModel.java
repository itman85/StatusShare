package com.phannguyen.statusshare.mvp.model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.firebase.FirebaseConstant;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.global.AppPresenter;
import com.phannguyen.statusshare.global.ObservablePostedStatus;
import com.phannguyen.statusshare.mvp.base.IHomeMVP;
import com.phannguyen.statusshare.storage.RealmHelper;
import com.phannguyen.statusshare.utils.FnUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by phannguyen on 4/14/17.
 */

public class HomeModel implements IHomeMVP.Model {
    final String TAG = "HomeModel";
    public static final int LIMIT_LOAD_FROM_SERVER = 20;
    public static final int LIMIT_LOAD_FROM_LOCAL = 20;
    private IHomeMVP.ModelCallback modelCallback;
    private DatabaseReference newStatusRef;
    private DatabaseReference deleteStatusRef;
    private ChildEventListener statusPostChildListener;
    private ChildEventListener statusRemoveChildListener;
    private Subscription postedStatusSub;

    public HomeModel(IHomeMVP.ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
        newStatusRef = FirebaseHelper.Instance().getNewStatusRef();
        deleteStatusRef = FirebaseHelper.Instance().getDeletedStatusRef();
        buildStatusPostListener();
        buildStatusRemoveListener();
    }

    @Override
    public void executeListeningStatusPosted() {
        newStatusRef.orderByChild(FirebaseConstant.CREATED_SERVER_STAMP_FIELD)
                .startAt(AppPresenter.Instance().getLastStatusTime()+1)
                .addChildEventListener(statusPostChildListener);

        deleteStatusRef.orderByChild(FirebaseConstant.CREATED_SERVER_STAMP_FIELD)
                .startAt(AppPresenter.Instance().getLastDeletedStatusTime()+1)
                .addChildEventListener(statusRemoveChildListener);

        startSubscibePostStatusEvent();
    }

    @Override
    public void executeStopListening() {
        if(newStatusRef!=null && statusPostChildListener!=null)
            newStatusRef.removeEventListener(statusPostChildListener);

        if (deleteStatusRef!=null && statusRemoveChildListener != null)
            deleteStatusRef.removeEventListener(statusRemoveChildListener);

        if(postedStatusSub!=null)
            postedStatusSub.unsubscribe();
    }

    @Override
    public void executeLoadLocalStatus() {
        final Realm realm = Realm.getDefaultInstance();
        realm.where(StatusModel.class)
                .findAllAsync()
                .sort("createdServerStamp", Sort.DESCENDING)
                .asObservable()
                .filter(RealmResults::isLoaded)
                .first()
                .doOnCompleted(realm::close)
                .subscribe(res -> {
                    if(res.size()>0) {
                        Log.i("REALM", "realm data: " + res.size());
                        int limit = res.size()>LIMIT_LOAD_FROM_LOCAL?LIMIT_LOAD_FROM_LOCAL:res.size();
                        List<StatusModel> resData = res.subList(0,limit);//realm.copyFromRealm(res);
                        Log.i("REALM", "resData : " + resData.size());
                        AppPresenter.Instance().setBottomStatusTime(resData.get(resData.size()-1).getCreatedServerStamp());
                        modelCallback.onLocalStatusDataLoaded(resData);
                    }else{
                        //there no data in local, load from server
                        startLoadDataFromServerAtInit();
                        //because no data in local, load all new from server so only need to listen remove status from now on
                        AppPresenter.Instance().setLastDeletedStatusTime(System.currentTimeMillis());
                    }
                });

    }

    @Override
    public void executeDeleteStatus(StatusModel statusModel) {
        FirebaseHelper.Instance().deleteStatus(statusModel);
        RealmHelper.removeStatus(statusModel.getStatusKeyId());
    }

    @Override
    public void executeLoadMoreStatus() {
        loadMoreData(AppPresenter.Instance().getBottomStatusTime());
    }

    private void startSubscibePostStatusEvent(){
        postedStatusSub = ObservablePostedStatus.instance().getPostEvents()
                .subscribe(res->{
                    if(res!=null) {
                        if (res.getCreatedServerStamp() > AppPresenter.Instance().getLastStatusTime())
                            AppPresenter.Instance().setLastStatusTime(res.getCreatedServerStamp());
                        RealmHelper.storeStatus(res);
                    }
                    if(modelCallback!=null)
                        modelCallback.onStatusPosted(ObservablePostedStatus.instance().getQueueSize());
                });
    }

    private void buildStatusPostListener(){
        statusPostChildListener = new ChildEventListener() {
            //only handle when new status created in new status node of firebase
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"new status posted id "+dataSnapshot.getKey());
                StatusModel statusModel;
                try {
                    statusModel = new StatusModel(dataSnapshot);
                } catch (Exception e) {
                    e.printStackTrace();
                    statusModel = null;
                }
                if(statusModel!=null && !FnUtils.isFromUserOnThisDevice(statusModel.getAuthorEmailKey(),statusModel.getDeviceId())){
                    ObservablePostedStatus.instance().onPosted(statusModel);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s){}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot){}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s){}
            @Override
            public void onCancelled(DatabaseError databaseError){}
        };
    }

    private void buildStatusRemoveListener(){
        //only handle when new status created in deleted status node of firebase
        statusRemoveChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"delete status id "+dataSnapshot.getKey());
                StatusModel statusModel;
                try {
                    statusModel = new StatusModel(dataSnapshot);
                } catch (Exception e) {
                    e.printStackTrace();
                    statusModel = null;
                }
                if(statusModel!=null) {
                    if(statusModel.getCreatedServerStamp()>AppPresenter.Instance().getLastDeletedStatusTime())
                        AppPresenter.Instance().setLastDeletedStatusTime(statusModel.getCreatedServerStamp());

                    if (!FnUtils.isFromUserOnThisDevice(statusModel.getAuthorEmailKey(),statusModel.getDeviceId())) {
                        RealmHelper.removeStatus(dataSnapshot.getKey());
                        ObservablePostedStatus.instance().deletedStatusInQueue(statusModel);
                        modelCallback.onStatusRemoved(statusModel);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
    }

    /**
     * load latest limit number of data in firebase at init (when local has no data)
     */
    private void startLoadDataFromServerAtInit(){
        newStatusRef.orderByChild(FirebaseConstant.CREATED_SERVER_STAMP_FIELD)
                .limitToLast(LIMIT_LOAD_FROM_SERVER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            List<StatusModel> res = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    res.add(0,new StatusModel(snapshot));
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                            if(res.size()>0){
                                //update last time of status
                                AppPresenter.Instance().setLastStatusTime(res.get(0).getCreatedServerStamp());
                                //update oldest time of status
                                AppPresenter.Instance().setBottomStatusTime(res.get(res.size()-1).getCreatedServerStamp());
                                //store into realm db
                                RealmHelper.storeStatus(res);
                                //
                                modelCallback.onLocalStatusDataLoaded(res);
                            }else{
                                modelCallback.onLocalStatusDataLoaded(null);
                            }
                        }else{
                            modelCallback.onLocalStatusDataLoaded(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG,databaseError.getMessage());
                        modelCallback.onLocalStatusDataLoaded(null);
                    }
                });
    }

    /*
       Load more data from local first. If not load enough, continue to load from server
     */
    private void loadMoreData(long bottomTime){
        final Realm realm = Realm.getDefaultInstance();
        realm.where(StatusModel.class)
                .lessThan("createdServerStamp",bottomTime)
                .findAllAsync()
                .sort("createdServerStamp", Sort.DESCENDING)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(RealmResults::isLoaded)
                .first()
                .doOnCompleted(realm::close)
                .subscribe(res -> {
                    int limitLoadServer = LIMIT_LOAD_FROM_SERVER;
                    List<StatusModel> resData = null;
                    if(res.size()>0) {
                        Log.e("REALM", "realm data: " + res.size());
                        int limit = res.size()>LIMIT_LOAD_FROM_LOCAL?LIMIT_LOAD_FROM_LOCAL:res.size();
                        resData = res.subList(0,limit);
                        Log.e("REALM", "resData : " + resData.size());
                        //update oldest time of status for next load more
                        AppPresenter.Instance().setBottomStatusTime(resData.get(resData.size()-1).getCreatedServerStamp());
                        limitLoadServer = LIMIT_LOAD_FROM_LOCAL - resData.size();
                        //check if load from local is enough
                        if(limitLoadServer==0){
                            modelCallback.onLoadMoreStatusComplete(resData);
                        }
                    }
                    //check if need to load more from server
                    if(limitLoadServer>0){
                        loadMoreDataFromServer(AppPresenter.Instance().getBottomStatusTime(),
                                limitLoadServer,resData);
                    }
                });

    }

    /*
      Load more data from firebase
     */
    private void loadMoreDataFromServer(long bottomtime,int limitload,List<StatusModel>localLoadedList){
        newStatusRef.orderByChild(FirebaseConstant.CREATED_SERVER_STAMP_FIELD)
                .endAt(bottomtime)
                .limitToLast(limitload+1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>1){
                            List<StatusModel> res = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    res.add(0,new StatusModel(snapshot));
                                    if(res.size()==limitload)//exclude last item because it already existed in local
                                        break;
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                            //store into local
                            RealmHelper.storeStatus(res);
                            //append to local loaded data
                            if(localLoadedList!=null && localLoadedList.size()>0)
                                res.addAll(0,localLoadedList);
                            //
                            if(res.size()>0){
                                //update oldest time of status for next load more
                                AppPresenter.Instance().setBottomStatusTime(res.get(res.size()-1).getCreatedServerStamp());
                                modelCallback.onLoadMoreStatusComplete(res);
                            }else{
                                modelCallback.onLoadMoreStatusComplete(null);
                            }
                        }else{
                            modelCallback.onLoadMoreStatusComplete(localLoadedList);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        modelCallback.onLoadMoreStatusComplete(null);
                    }
                });
    }
}
