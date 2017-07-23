package com.phannguyen.statusshare.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.global.ObservablePostedStatus;
import com.phannguyen.statusshare.mvp.base.IHomeMVP;
import com.phannguyen.statusshare.mvp.presenter.HomePresenter;
import com.phannguyen.statusshare.ui.activities.base.BaseActivity;
import com.phannguyen.statusshare.ui.components.adapters.StatusFeedAdapter;
import com.phannguyen.statusshare.ui.components.views.CustomAppBar;
import com.phannguyen.statusshare.ui.components.views.LinearListLoadMore;
import com.phannguyen.statusshare.utils.FnUtils;
import com.webianks.library.PopupBubble;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements IHomeMVP.View {
    final int REQUEST_CREATE_STATUS_CODE = 100;
    @Bind(R.id.appbar)
    CustomAppBar appBar;
    @Bind(R.id.statusListView)
    LinearListLoadMore statusFeedListView;
    @Bind(R.id.edtStatus)
    EditText edtStatus;
    @Bind(R.id.popup_bubble)
    PopupBubble popupBubble;
    StatusFeedAdapter statusFeedAdapter;
    IHomeMVP.Presenter mPresenter;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this,this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stopListeningStatusPosted();
    }

    private void initView(){
        appBar.setBarTitle("Welcome "+ FirebaseHelper.Instance().getUserNickName());
        appBar.setAppBarCallback(appBarCallback);
        statusFeedAdapter = new StatusFeedAdapter(this,mStatusItemSelectedCallback);
        //statusFeedAdapter.setDataList(createSampleData());
        statusFeedListView.setAdapter(statusFeedAdapter);
        statusFeedListView.setOnLoadMoreListener(()->mPresenter.loadMoreStatus());

        edtStatus.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, CreateStatusActivity.class);
            startActivityForResult(intent,REQUEST_CREATE_STATUS_CODE);
            overridePendingTransition(R.anim.slide_up,R.anim.no_change);
        });
        mPresenter = new HomePresenter(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //
        popupBubble.setPopupBubbleListener(c-> loadNewStatusPosted());

        popupBubble.setRecyclerView(statusFeedListView.getMainListView());
        popupBubble.setVisibility(View.GONE);

        new Handler().postDelayed(()->mPresenter.loadLocalStatusData(),200);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE_STATUS_CODE) {
            if(resultCode == Activity.RESULT_OK){
                StatusItemData editedItemData = data.getParcelableExtra("edited_status");
                if(editedItemData!=null){
                    statusFeedAdapter.removeItem(editedItemData);
                }
                StatusItemData itemData = data.getParcelableExtra("created_status");
                if(itemData!=null){
                    statusFeedAdapter.addItemsOnFirst(itemData);
                    statusFeedListView.smoothScrollToPosition(0);
                }
            }

        }
    }

    @Override
    public void onStatusPosted(int number) {
        if(number>0) {
            popupBubble.updateText(number + " new status");
            popupBubble.show();
        }else{
            popupBubble.hide();
        }
    }

    @Override
    public void onStatusRemoved(StatusItemData itemData) {
        statusFeedAdapter.removeItem(itemData);
    }

    @Override
    public void onLocalStatusDataLoaded(List<StatusItemData> statusDataList) {
        if(pDialog!=null)
            pDialog.dismiss();
        if(statusDataList!=null && statusDataList.size()>0) {
            statusFeedAdapter.addItemsOnFirst(statusDataList);
            statusFeedListView.smoothScrollToPosition(0);
        }
        //
        mPresenter.startListeningStatusPosted();
    }

    @Override
    public void onLoadMoreStatusComplete(List<StatusItemData> statusModelsList) {
        if(statusModelsList!=null && statusModelsList.size()>0) {
            new Handler().post(()->{
                statusFeedListView.onLoadMoreComplete();
                statusFeedAdapter.addItemsOnLast(statusModelsList);
            });
        }else{
            new Handler().postDelayed(()->statusFeedListView.onLoadMoreComplete(),500);
        }

    }

    private void loadNewStatusPosted(){
        popupBubble.hide();
        //
        if(ObservablePostedStatus.instance().getQueueSize()>0) {
            statusFeedAdapter.addItemsOnFirst(ObservablePostedStatus.instance().getStatusList());
            statusFeedListView.smoothScrollToPosition(0);
            ObservablePostedStatus.instance().clearQueue();
        }
    }
    private List<StatusItemData> createSampleData(){

        List<StatusItemData> dataList = new ArrayList<>();
        StatusItemData item1 = new StatusItemData();
        item1.setAuthorNickname("Phan");
        item1.setCreatedServerStamp(1492158555264L);
        item1.setStatusText("What a beautiful day!");
        item1.setImagesList(new ArrayList<String>());
        item1.getImagesList().add("http://images.dev.omebee.com/photos/product/68/59/be/c5/1484122343801_1.33333.jpg");
        dataList.add(item1);

        StatusItemData item2 = new StatusItemData();
        item2.setAuthorNickname("Thu");
        item2.setCreatedServerStamp(1492156555264L);
        item2.setStatusText("How are you guys?");
        item2.setImagesList(new ArrayList<String>());
        item2.getImagesList().add("http://images.dev.omebee.com/photos/product/42/9f/6a/97/1484122350766_1.33054.jpg");
        item2.getImagesList().add("http://images.dev.omebee.com/photos/product/3b/b7/2a/12/1484122352204_1.33333.jpg");
        dataList.add(item2);

        StatusItemData item3 = new StatusItemData();
        item3.setAuthorNickname("Khai");
        item3.setCreatedServerStamp(1492155555264L);
        item3.setStatusText("How are you guys?");
        item3.setImagesList(new ArrayList<String>());
        item3.getImagesList().add("http://images.dev.omebee.com/photos/product/09/68/d5/3e/1484122354231_1.33333.jpg");
        item3.getImagesList().add("http://images.dev.omebee.com/photos/product/bc/94/4d/7c/1484726880660_0.79722.jpg");
        item3.getImagesList().add("http://images.dev.omebee.com/photos/product/95/6f/3e/7c/1484118408557_1.00000.jpg");
        item3.getImagesList().add("http://images.dev.omebee.com/photos/product/98/df/e3/a2/1484118413019_1.00000.jpg");
        item3.getImagesList().add("http://images.dev.omebee.com/photos/product/29/44/6f/2c/1484118414436_1.00000.jpg");
        item3.getImagesList().add("http://images.dev.omebee.com/photos/product/86/46/e5/b8/1484217558453_1.44526.jpg");
        dataList.add(item3);

        StatusItemData item4 = new StatusItemData();
        item4.setAuthorNickname("Dinh");
        item4.setCreatedServerStamp(1492154555264L);
        item4.setStatusText("How are you guys?");
        item4.setImagesList(new ArrayList<String>());
        item4.getImagesList().add("http://images.dev.omebee.com/photos/product/09/68/d5/3e/1484122354231_1.33333.jpg");
        item4.getImagesList().add("http://images.dev.omebee.com/photos/product/bc/94/4d/7c/1484726880660_0.79722.jpg");
        item4.getImagesList().add("http://images.dev.omebee.com/photos/product/95/6f/3e/7c/1484118408557_1.00000.jpg");
        item4.getImagesList().add("http://images.dev.omebee.com/photos/product/86/46/e5/b8/1484217558453_1.44526.jpg");
        dataList.add(item4);

        StatusItemData item5 = new StatusItemData();
        item5.setAuthorNickname("Van");
        item5.setCreatedServerStamp(1432153555264L);
        item5.setStatusText("How are you guys?");
        item5.setImagesList(new ArrayList<String>());
        item5.getImagesList().add("http://images.dev.omebee.com/photos/product/09/68/d5/3e/1484122354231_1.33333.jpg");
        item5.getImagesList().add("http://images.dev.omebee.com/photos/product/bc/94/4d/7c/1484726880660_0.79722.jpg");
        item5.getImagesList().add("http://images.dev.omebee.com/photos/product/95/6f/3e/7c/1484118408557_1.00000.jpg");
         dataList.add(item5);

        return dataList;
    }

    CustomAppBar.IAppBarCallback appBarCallback = new CustomAppBar.IAppBarCallback() {
        @Override
        public void onSignoutClicked() {
            String message = getString(R.string.logout_confirm);
            FnUtils.showConfirmDialog(HomeActivity.this, "", message, getString(R.string.yes_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.logout();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            }, getString(R.string.no_text), null);
        }
    };


    private StatusFeedAdapter.IStatusItemSelectedCallback mStatusItemSelectedCallback = new StatusFeedAdapter.IStatusItemSelectedCallback() {
        @Override
        public void onStatusEdit(StatusItemData itemData) {
            Intent intent = new Intent(HomeActivity.this, CreateStatusActivity.class);
            intent.putExtra("edit_status",itemData);
            startActivityForResult(intent,REQUEST_CREATE_STATUS_CODE);
            overridePendingTransition(R.anim.slide_up,R.anim.no_change);
        }

        @Override
        public void onStatusDelete(final StatusItemData itemData) {
            FnUtils.showConfirmDialog(HomeActivity.this, "", getString(R.string.delete_status_confirm), getString(R.string.yes_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    statusFeedAdapter.removeItem(itemData);
                    mPresenter.deleteStatus(itemData);
                }
            }, getString(R.string.no_text), null);
        }
    };

}
