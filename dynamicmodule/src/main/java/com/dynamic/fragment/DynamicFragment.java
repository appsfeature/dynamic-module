package com.dynamic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.adapter.DynamicAdapter;
import com.dynamic.listeners.DMContentType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;

import java.io.Serializable;
import java.util.List;


public class DynamicFragment extends BaseDynamicFragment {

    protected DynamicCallback.OnDynamicListListener mClickListener;

    public static DynamicFragment getInstance(int catId) {
        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(DMUtility.getPropertyBundle(catId));
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DynamicCallback.OnDynamicListListener) {
            mClickListener = (DynamicCallback.OnDynamicListListener) context;
        }
    }

    @Override
    public int getLayoutContentView() {
        return R.layout.dm_fragment_dynamic;
    }

    @Override
    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return new DynamicAdapter<>(activity, mList, new DynamicCallback.OnClickListener<DMCategory<DMContent>, DMContent>() {
            @Override
            public void onItemClicked(View v, DMCategory<DMContent> category, DMContent item) {
                if(item.getItemType() != DMContentType.TYPE_NO_ACTION) {
                    if (mClickListener != null) {
                        mClickListener.onItemClicked(activity, v, property, item);
                    }else {
                        BaseUtil.showToast(activity, "Invalid Listener!");
                    }
                }
            }
        });
    }

    @Override
    public void onInitViews(View view) {

    }

    @Override
    public void onUpdateUi() {

    }

    @Override
    public boolean onResumeReloadList() {
        return false;
    }

    @Override
    public List<DMCategory<DMContent>> getStaticList() {
        return null;
    }

    @Override
    public void onValidateList(List<DMCategory<DMContent>> list, Response.Status<List<DMCategory<DMContent>>> callback) {
        callback.onSuccess(list);
    }

    @Override
    public void onNetworkRequestCompleted() {
    }

    @Override
    public boolean onUpdateWhenListCountChanged() {
        return false;
    }

    @Override
    public void onInitDataFromArguments(Bundle bundle, Serializable extraProperty) {

    }
}
