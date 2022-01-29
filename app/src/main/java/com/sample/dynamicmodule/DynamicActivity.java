package com.sample.dynamicmodule;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dynamic.fragment.DynamicListFragment;
import com.dynamic.fragment.DynamicPagerFragment;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;

public class DynamicActivity extends AppCompatActivity implements DynamicCallback.OnDynamicPagerListener
        , DynamicCallback.OnDynamicListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        fragmentMapping(DynamicPagerFragment.getInstance(), R.id.content);
        fragmentMapping(DynamicListFragment.getInstance(), R.id.content2);
    }

    private void fragmentMapping(Fragment fragment, int layoutId) {
        getSupportFragmentManager().beginTransaction().replace(layoutId, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(View view, DMContent item) {

    }
}
