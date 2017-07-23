package com.phannguyen.statusshare.global;

import com.phannguyen.statusshare.datamodel.service.StatusModel;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by phannguyen on 4/16/17.
 */

public class ObservablePostedStatus {
    private static ObservablePostedStatus instance;
    private static final Object lock = new Object();
    private PublishSubject<StatusModel> subject = PublishSubject.create();
    private List<StatusModel> statusQueue = new ArrayList<>();
    public static ObservablePostedStatus instance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new ObservablePostedStatus();
            }
        }
        return instance;
    }

    public void onPosted(StatusModel newStatus) {
        statusQueue.add(0,newStatus);
        subject.onNext(newStatus);
    }

    public Observable<StatusModel> getPostEvents() {
        return subject;
    }

    public void clearQueue(){
        statusQueue.clear();
    }

    public int getQueueSize(){
        return statusQueue.size();
    }

    public List<StatusItemData> getStatusList(){
        return StatusItemData.castFromModelList(statusQueue);
    }

    public void deletedStatusInQueue(StatusModel deletedStatus){
        if(statusQueue.contains(deletedStatus)) {
            statusQueue.remove(deletedStatus);
            subject.onNext(null);
        }
    }
}
