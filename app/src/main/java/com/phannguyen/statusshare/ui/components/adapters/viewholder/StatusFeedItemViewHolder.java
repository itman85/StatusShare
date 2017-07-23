package com.phannguyen.statusshare.ui.components.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.ui.components.adapters.StatusFeedAdapter;
import com.phannguyen.statusshare.ui.components.base.BaseRecycleViewHolder;
import com.phannguyen.statusshare.ui.components.views.RatioImageView;
import com.phannguyen.statusshare.ui.components.views.StatusActionPopupLayout;
import com.phannguyen.statusshare.utils.FnUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phannguyen on 4/13/17.
 */

public class StatusFeedItemViewHolder extends BaseRecycleViewHolder<StatusItemData> implements View.OnClickListener {
    @Bind(R.id.tvAuthor)
    protected TextView tvAuthor;
    @Bind(R.id.tvUpdatedTime)
    protected TextView tvUpdatedTime;
    @Bind(R.id.tvStatusText)
    protected TextView tvStatusText;
    @Bind(R.id.tvCount)
    protected TextView tvCountImage;

    @Bind(R.id.tvEdit)
    protected TextView tvEdited;

    @Bind(R.id.flDefaultImageLayout)
    protected View flDefaultImageLayout;
    @Bind(R.id.ivDefaultImage)
    protected RatioImageView mDefaultImage;

    @Bind(R.id.llSub)
    protected LinearLayout mLlSubLayout;

    @Bind(R.id.flSubImage1Layout)
    protected View flSubImage1Layout;
    @Bind(R.id.ivSubImage1)
    protected ImageView mSubImage1;

    @Bind(R.id.flSubImage2Layout)
    protected View flSubImage2Layout;
    @Bind(R.id.ivSubImage2)
    protected ImageView mSubImage2;

    @Bind(R.id.flSubImage3Layout)
    protected View flSubImage3Layout;
    @Bind(R.id.ivSubImage3)
    protected ImageView mSubImage3;
    @Bind(R.id.llStatusItemAction)
    protected View mllStatusItemAction;
    @Bind(R.id.ivStatusItemAction)
    protected ImageView ivStatusItemAction;
    @Bind(R.id.spStatusItemAction)
    protected StatusActionPopupLayout spStatusActionPopup;

    private Context mContext;
    private StatusItemData itemData;
    private StatusFeedAdapter.IStatusItemSelectedCallback mStatusItemSelectedCallback;
    public StatusFeedItemViewHolder(Context context,View itemView,StatusFeedAdapter.IStatusItemSelectedCallback statusItemSelectedCallback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //
        this.mContext = context;
        this.mStatusItemSelectedCallback = statusItemSelectedCallback;
        ivStatusItemAction.setOnClickListener(this);
        spStatusActionPopup.setStatusActionCallback(new StatusActionPopupLayout.IStatusActionCallback() {
            @Override
            public void onStatusEdited() {
                mStatusItemSelectedCallback.onStatusEdit(itemData);
            }

            @Override
            public void onStatusDeleted() {
                mStatusItemSelectedCallback.onStatusDelete(itemData);
            }
        });
    }

    @Override
    public void bind(StatusItemData itemdata, int pos) {
        this.itemData = itemdata;
        tvAuthor.setText(itemdata.getAuthorNickname());
        tvUpdatedTime.setText(FnUtils.getDisplayTime(itemdata.getCreatedServerStamp()));
        tvStatusText.setText(itemdata.getStatusText());
        resetStatusForViews();
        bindDefaultImages();
        bindSubImages1();
        bindSubImages2();
        bindSubImages3();
    }

    @Override
    public StatusItemData getDataItem() {
        return this.itemData;
    }

    @Override
    public void setDataItem(StatusItemData dataItem) {
        this.itemData = dataItem;
    }

    private void resetStatusForViews(){
        /*mLlSubLayout.setVisibility(View.GONE);
        mDefaultImage.setVisibility(View.GONE);
        progressBar0.setVisibility(View.GONE);*/
        mLlSubLayout.setVisibility(View.VISIBLE);
        if(FirebaseHelper.Instance().isCurrentUser(this.itemData.getAuthorEmailKey())){
            mllStatusItemAction.setVisibility(View.VISIBLE);
        }else{
            mllStatusItemAction.setVisibility(View.GONE);
        }
        if(itemData.isEdited())
            tvEdited.setVisibility(View.VISIBLE);
        else
            tvEdited.setVisibility(View.GONE);
    }
    private void bindDefaultImages(){
        if(itemData.getImagesList()!=null && itemData.getImagesList().size()>0){
            mDefaultImage.setHeightRatio(FnUtils.getImageRatioFromUrl(itemData.getImagesList().get(0)));
            handleDisplayImage(true,flDefaultImageLayout,mDefaultImage,null,itemData.getImagesList().get(0));
        }else{
            mLlSubLayout.setVisibility(View.GONE);
            mDefaultImage.setVisibility(View.GONE);
        }
    }

    private void bindSubImages1(){
        if(itemData.getImagesList()!=null && itemData.getImagesList().size()>1){
            handleDisplayImage(true,flSubImage1Layout,mSubImage1,null,itemData.getImagesList().get(1));
        }else {
            mLlSubLayout.setVisibility(View.GONE);
        }
    }
    private void bindSubImages2(){
        if(itemData.getImagesList()!=null && itemData.getImagesList().size()>2){
            handleDisplayImage(true,flSubImage2Layout,mSubImage2,null,itemData.getImagesList().get(2));
        }else{
            flSubImage2Layout.setVisibility(View.INVISIBLE);
        }
    }
    private void bindSubImages3(){
        if(itemData.getImagesList()!=null && itemData.getImagesList().size()>3){
             if(itemData.getImagesList().size()>4){
                tvCountImage.setVisibility(View.VISIBLE);
                tvCountImage.setText("+"+(itemData.getImagesList().size()-4));
                 tvCountImage.setAlpha((float)190/255);
                 handleDisplayImage(true,flSubImage3Layout,mSubImage3,tvCountImage,itemData.getImagesList().get(3));

             }else{
                tvCountImage.setVisibility(View.GONE);
                 handleDisplayImage(true,flSubImage3Layout,mSubImage3,null,itemData.getImagesList().get(3));
             }
        }else{
            flSubImage3Layout.setVisibility(View.INVISIBLE);
        }
    }
    private void handleDisplayImage(boolean isShow, View flLayout, ImageView imageView, final TextView textView, String imageUrl){
        if(isShow){
            flLayout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            if(textView!=null)
                textView.setVisibility(View.GONE);
            Glide.with(mContext).load(imageUrl)
                    .placeholder(FnUtils.getRandomDrawbleColor())
                    .thumbnail(0.5f)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if(textView!=null)
                                textView.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if(textView!=null)
                                textView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }else{
            flLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivStatusItemAction:
                spStatusActionPopup.performClick();
                break;
            default:
                break;
        }
    }
}
