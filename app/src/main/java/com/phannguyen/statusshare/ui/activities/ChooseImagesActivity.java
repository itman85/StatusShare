package com.phannguyen.statusshare.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.ui.ImageItemData;
import com.phannguyen.statusshare.ui.activities.base.BaseActivity;
import com.phannguyen.statusshare.ui.components.adapters.GalleryAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;


/**
 * Created by phannguyen on 4/13/17.
 */

public class ChooseImagesActivity extends BaseActivity {
    @Bind(R.id.doneBtn)
    Button doneBtn;
    @Bind(R.id.clearBtn)
    Button clearBtn;
    @Bind(R.id.tvBarTitle)
    TextView tvBarTitle;
    @Bind(R.id.recycler_view)
    RecyclerView imageRecyclerView;
    private GalleryAdapter mImageAdapter;
    List<ImageItemData> imageUrls = new ArrayList<>();
    SparseArray<ImageItemData> selectedImagesMap = new SparseArray<>();
    private ProgressDialog pDialog;
    ReadImageJsonTask readDataTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        ButterKnife.bind(this,this);
        if(getIntent().getExtras()!=null){
            List<String> selectedImageUrls = getIntent().getExtras().getStringArrayList("pre_added_images");
            initView(selectedImageUrls);
        }else {
            initView(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(readDataTask!=null) {
            readDataTask.cancel(true);
            readDataTask = null;
        }
    }

    private void initView(List<String> selectedImageUrls){
        doneBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.blue_button), PorterDuff.Mode.MULTIPLY);
        doneBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        clearBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey_button), PorterDuff.Mode.MULTIPLY);
        clearBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        if(selectedImageUrls!=null && selectedImageUrls.size()>0) {
            tvBarTitle.setText( String.format(getString(R.string.choose_images_text)+" (%d)",selectedImageUrls.size()));
            clearBtn.setVisibility(View.VISIBLE);
        }else{
            tvBarTitle.setText(R.string.choose_images_text);
            clearBtn.setVisibility(GONE);
        }

        pDialog = new ProgressDialog(this);
        mImageAdapter = new GalleryAdapter(getApplicationContext(), imageUrls,imageSelectCallback);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        imageRecyclerView.setLayoutManager(mLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageRecyclerView.setAdapter(mImageAdapter);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedImageUrls = new ArrayList<>();
                for (int i=0;i<selectedImagesMap.size();i++) {
                    selectedImageUrls.add(selectedImagesMap.get(selectedImagesMap.keyAt(i)).getImageUrl());
                }
                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("added_images",selectedImageUrls);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImagesMap.clear();
                for(ImageItemData itemData:imageUrls){
                    itemData.setSelectedStatus(false);
                }
                mImageAdapter.notifyDataSetChanged();
                updateTitleBar();
            }
        });
        //
        loadImageFromRawResource(selectedImageUrls);
    }

    private GalleryAdapter.IImageSelectCallback imageSelectCallback = new GalleryAdapter.IImageSelectCallback() {
        @Override
        public void onImageSelectedChange(int pos, ImageItemData imageItemData) {
            if(selectedImagesMap.indexOfKey(pos)>0){
                if(!imageItemData.isSelectedStatus())
                    selectedImagesMap.remove(pos);
            }else{
                if(imageItemData.isSelectedStatus())
                    selectedImagesMap.put(pos,imageItemData);
            }
            updateTitleBar();
        }
    };

    public void updateTitleBar(){
        if(selectedImagesMap.size()>0) {
            tvBarTitle.setText("Choose images ("+selectedImagesMap.size()+")");
            clearBtn.setVisibility(View.VISIBLE);
        }else{
            tvBarTitle.setText("Choose images");
            clearBtn.setVisibility(View.GONE);
        }
    }

    public void loadImageFromRawResource(List<String> selectedImageUrls){
        pDialog.setMessage("Reading images resource...");
        pDialog.show();
        readDataTask = new ReadImageJsonTask(this,selectedImageUrls);
        readDataTask.execute();
    }

    private class ReadImageJsonTask extends AsyncTask<Void, Void, ArrayList<ImageItemData>> {
        Context _context;
        List<String> _selectedImageUrls;

         ReadImageJsonTask(Context context,List<String>  selectedImageUrls) {
            this._context = context;
            this._selectedImageUrls = selectedImageUrls;

        }
        @Override
        protected ArrayList<ImageItemData> doInBackground(Void... params) {
            JSONArray imagedata = readImageJsonData(_context);
            if(imagedata!=null) {
                ArrayList<ImageItemData> images = new ArrayList<>();
                ArrayList<ImageItemData> selectedImages = new ArrayList<>();
                for (int i = 0; i < imagedata.length(); i++) {
                    try {
                        if (_selectedImageUrls != null && _selectedImageUrls.contains((String) imagedata.get(i))) {
                            ImageItemData item = new ImageItemData((String) imagedata.get(i), true);
                            images.add(0, item);
                            selectedImages.add(item);
                        } else {
                            images.add(new ImageItemData((String) imagedata.get(i), false));
                        }
                    } catch (JSONException e) {
                        Log.e("Read Image", "Json parsing error: " + e.getMessage());
                    }
                }
                for (ImageItemData imageItemData : selectedImages) {
                    int idx = images.indexOf(imageItemData);
                    if (idx != -1) {
                        selectedImagesMap.put(idx, imageItemData);
                    }
                }
                return images;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageItemData> images) {
            if(images!=null && images.size()>0){
                imageUrls = images;
                mImageAdapter.setData(imageUrls);
                mImageAdapter.notifyDataSetChanged();
            }
            if(pDialog!=null)
                pDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i("TASK","Task cancelled");
        }
    }

    private JSONArray readImageJsonData(Context mContext){
        InputStream is = mContext.getResources().openRawResource(R.raw.imageurls);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        }catch (UnsupportedEncodingException e) {
            return null;
        }
        catch (IOException e) {
            return null;
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String jsonString = writer.toString();
            return new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
