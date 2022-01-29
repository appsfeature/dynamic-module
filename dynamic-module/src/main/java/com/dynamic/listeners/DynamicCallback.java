package com.dynamic.listeners;

import android.view.View;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

public interface DynamicCallback {

    interface OnDynamicListListener{
        void onItemClicked(View view, DMContent item);
    }
    interface OnDynamicPagerListener{
        void onItemClicked(View view, DMContent item);
    }
}
