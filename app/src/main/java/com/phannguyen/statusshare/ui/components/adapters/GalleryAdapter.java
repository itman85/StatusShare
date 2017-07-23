package com.phannguyen.statusshare.ui.components.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.ui.ImageItemData;
import com.phannguyen.statusshare.ui.components.views.SquareLayout;
import com.phannguyen.statusshare.utils.FnUtils;

import java.util.List;

/**
 * Created by phannguyen on 4/13/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    public interface IImageSelectCallback{
        void onImageSelectedChange(int pos,ImageItemData imageItemData);
    }
    private List<ImageItemData> images;
    private Context mContext;
    IImageSelectCallback selectCallback;
    public GalleryAdapter(Context context, List<ImageItemData> images,IImageSelectCallback selectCallback) {
        mContext = context;
        this.images = images;
        this.selectCallback = selectCallback;
    }

    public void setData(List<ImageItemData> images){
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ctrl_gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView,parent.getContext());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ImageItemData image = images.get(position);
        holder.bind(image,position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public SquareLayout parentView;
        public AppCompatCheckBox checkBox;
        private ImageItemData mItemData;
        private int mPosition;
        private int padding;
        private Context context;

        public MyViewHolder(View view,Context context) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            parentView = (SquareLayout) view.findViewById(R.id.parentView);
            checkBox = (AppCompatCheckBox) view.findViewById(R.id.chkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemData.setSelectedStatus(checkBox.isChecked());
                    selectCallback.onImageSelectedChange(mPosition,mItemData);
                    if(checkBox.isChecked()){
                        parentView.setPadding(padding, padding, padding, padding);
                        parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.checkbox_border));

                    }else{
                        parentView.setPadding(0, 0, 0, 0);
                        parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

                    }
                }
            });
            this.context = context;
            padding= this.context.getResources().getDimensionPixelOffset(R.dimen.common_padding_half);
        }

        public void bind(ImageItemData itemData,int position){
            this.mItemData = itemData;
            this.mPosition = position;
            parentView.setPadding(0, 0, 0, 0);
            parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setChecked(this.mItemData.isSelectedStatus());
            Glide.with(mContext).load(this.mItemData.getImageUrl())
                    .placeholder(FnUtils.getRandomDrawbleColor())
                    .thumbnail(0.5f)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            //checkBox.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            checkBox.setVisibility(View.VISIBLE);
                            if(checkBox.isChecked()) {
                                parentView.setPadding(padding, padding, padding, padding);
                                parentView.setBackgroundColor(ContextCompat.getColor(context, R.color.checkbox_border));
                            }
                            return false;
                        }
                    })
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnail);


        }
    }

}