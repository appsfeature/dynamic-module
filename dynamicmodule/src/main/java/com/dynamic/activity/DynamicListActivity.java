package com.dynamic.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.fragment.DynamicListFragment;
import com.dynamic.listeners.DMContentType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMClassUtil;
import com.dynamic.util.DMConstants;
import com.dynamic.util.DMProperty;
import com.dynamic.util.DMUtility;
import com.helper.util.BaseUtil;


public class DynamicListActivity extends AppCompatActivity implements DynamicCallback.OnDynamicListListener {
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_activity_dynamic);
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getSerializable(DMConstants.CATEGORY_PROPERTY) instanceof DMProperty) {
            DMProperty property = (DMProperty) bundle.getSerializable(DMConstants.CATEGORY_PROPERTY);
            title = property.getTitle();
            setUpToolBar();
            initFragment(bundle);
        } else {
            DMUtility.showPropertyError(this);
        }
    }

    private void initFragment(Bundle bundle) {
        Runnable runnable = new Runnable() {
            public void run() {
                Fragment fragment = new DynamicListFragment();
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
            DMClassUtil.openDynamicListActivity(this, DMUtility.getProperty(item));
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