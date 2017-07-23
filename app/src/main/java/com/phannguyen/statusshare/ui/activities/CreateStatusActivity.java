package com.phannguyen.statusshare.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.datamodel.ui.StatusItemData;
import com.phannguyen.statusshare.mvp.base.ICreateStatusMVP;
import com.phannguyen.statusshare.mvp.presenter.CreateStatusPresenter;
import com.phannguyen.statusshare.ui.activities.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phannguyen on 4/13/17.
 */

public class CreateStatusActivity extends BaseActivity implements ICreateStatusMVP.View {
    final int REQUEST_ADD_IMAGES_CODE = 123;
    @Bind(R.id.tvBarTitle)
    TextView tvTitleBar;
    @Bind(R.id.CancelBtn)
    Button cancelBtn;
    @Bind(R.id.PostBtn)
    Button postBtn;
    @Bind(R.id.addPhotoBtn)
    Button addPhotoBtn;
    @Bind(R.id.edtStatus)
    EditText edtStatus;
    List<String> selectedImageUrls;
    ICreateStatusMVP.Presenter mPresenter;
    private ProgressDialog pDialog;
    private StatusItemData editedStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        ButterKnife.bind(this,this);
        if(getIntent().getExtras()!=null){
            editedStatus = getIntent().getExtras().getParcelable("edit_status");
        }
        initView(editedStatus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new Handler()).postDelayed(new Runnable() {

            public void run() {
                edtStatus.setInputType(InputType.TYPE_CLASS_TEXT);
                edtStatus.setTextIsSelectable(true);
                edtStatus.setSelection(edtStatus.getText().length());
                InputMethodManager inputMethodManager = (InputMethodManager) CreateStatusActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edtStatus, 0);
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }

    private void initView(StatusItemData itemData){
        postBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.blue_button), PorterDuff.Mode.MULTIPLY);
        postBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        cancelBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey_button), PorterDuff.Mode.MULTIPLY);
        cancelBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        pDialog = new ProgressDialog(this);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                overridePendingTransition(R.anim.no_change,R.anim.slide_down);

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postSatus();
            }
        });

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStatusActivity.this,ChooseImagesActivity.class);
                if(selectedImageUrls!=null)
                    intent.putStringArrayListExtra("pre_added_images", (ArrayList<String>) selectedImageUrls);
                startActivityForResult(intent,REQUEST_ADD_IMAGES_CODE);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        mPresenter = new CreateStatusPresenter(this);
        if(itemData!=null){
            edtStatus.setText(itemData.getStatusText());
            edtStatus.setSelection(edtStatus.getText().length());
            selectedImageUrls = itemData.getImagesList();
            tvTitleBar.setText(getString(R.string.edit_status));

        }
        updateSelectedImageInfo(selectedImageUrls);
        //disable popup keyboard
        edtStatus.setInputType(InputType.TYPE_NULL);

    }

    private void postSatus(){
        if(!"".equals(edtStatus.getText().toString())) {
            StatusItemData itemData = new StatusItemData();
            itemData.setStatusText(edtStatus.getText().toString());
            itemData.setAuthorNickname(mPresenter.getAuthorName());
            itemData.setImagesList(selectedImageUrls);
            itemData.setAuthorEmailKey(mPresenter.getAuthorEmail());
            //
            mPresenter.postSatusAction(itemData, editedStatus);
            //
            pDialog.setMessage("Your status is posting...");
            pDialog.show();
        }else{
            edtStatus.setError("Status must not empty");
        }

    }

    public void returnStatusWall(StatusItemData itemData,StatusItemData editedItemData){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("created_status",itemData);
        if(editedItemData!=null)
            returnIntent.putExtra("edited_status",editedItemData);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down);
    }
    private void updateSelectedImageInfo(List<String> selectedImageUrls){
        if(selectedImageUrls==null || selectedImageUrls.size()==0){
            addPhotoBtn.setText(R.string.add_images_text);
            addPhotoBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.gray_2), PorterDuff.Mode.MULTIPLY);
            addPhotoBtn.setTextColor(ContextCompat.getColor(this, R.color.white));

        }else{
            addPhotoBtn.setText(String.format(getString(R.string.add_or_remove_images_text)+" (%d)",selectedImageUrls.size()));
            addPhotoBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.checkbox_border), PorterDuff.Mode.MULTIPLY);
            addPhotoBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_IMAGES_CODE) {
            if(resultCode == Activity.RESULT_OK){
                selectedImageUrls = data.getStringArrayListExtra("added_images");
                updateSelectedImageInfo(selectedImageUrls);
            }
        }
    }

    @Override
    public void postSatusSuccessful(StatusItemData itemData,StatusItemData editedItemData) {
        if(pDialog!=null)
            pDialog.dismiss();
        returnStatusWall(itemData,editedItemData);
    }

    @Override
    public void postSatusFailed(String error) {
        if(pDialog!=null)
            pDialog.dismiss();
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();

    }
}
