package com.sample.dynamicmodule;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dynamic.fragment.DynamicFragment;
import com.dynamic.listeners.DMContentType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.helper.util.BaseUtil;

public class DynamicActivity extends AppCompatActivity implements DynamicCallback.OnDynamicPagerListener
        , DynamicCallback.OnDynamicListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        fragmentMapping(DynamicPagerFragment.getInstance(110), R.id.content);
        fragmentMapping(DynamicFragment.getInstance(115), R.id.content2);
    }

    private void fragmentMapping(Fragment fragment, int layoutId) {
        getSupportFragmentManager().beginTransaction().replace(layoutId, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(View view, DMContent item) {
        if(item.getItemType() == DMContentType.TYPE_LINK) {
            if (BaseUtil.isValidUrl(item.getLink())) {
                BaseUtil.showToast(this, "Update Later!");
            } else {
                BaseUtil.showToast(this, "Invalid Link!");
            }
        }else {
            BaseUtil.showToast(this, "Action Update Later");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
