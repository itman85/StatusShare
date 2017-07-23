package com.phannguyen.statusshare.ui.activities.base;

import android.support.v7.app.AppCompatActivity;

import com.phannguyen.statusshare.utils.MemoryLeakHelper;

/**
 * Created by phannguyen on 4/19/17.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoryLeakHelper.Instance().addWatch(this);
        MemoryLeakHelper.Instance().analysis();
    }
}
