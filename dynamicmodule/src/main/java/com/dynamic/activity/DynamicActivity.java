package com.dynamic.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.fragment.DynamicFragment;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.DMClassUtil;
import com.dynamic.util.DMUtility;


public class DynamicActivity extends DMBaseActivity implements DynamicCallback.OnDynamicListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_activity_dynamic);
        setUpToolBar();
        initFragment(getIntent().getExtras());
    }

    private void initFragment(Bundle bundle) {
        Runnable runnable = new Runnable() {
            public void run() {
                Fragment fragment = new DynamicFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, fragment);
                transaction.commitAllowingStateLoss();
            }
        };
        new Handler().post(runnable);
    }

    @Override
    public void onItemClicked(View view, DMContent item) {
        if(!item.isContent()){
            DMClassUtil.openDynamicListActivity(this, DMUtility.getProperty(property, item));
        }else {
            DynamicModule.getInstance().dispatchListClickListener(view, item);
        }
    }

    private void setUpToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home ){
            onBackPressed();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}