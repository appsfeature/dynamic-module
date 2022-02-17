package com.dynamic.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.model.DMCategory;
import com.dynamic.util.DMConstants;
import com.dynamic.util.DMDataManager;
import com.dynamic.util.DMProperty;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseDynamicFragment extends DMBaseFragment {
    protected View layoutNoData;
    protected RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    protected final List<DMCategory> mList = new ArrayList<>();
    protected RecyclerView rvList;

    @LayoutRes
    public abstract int getLayoutContentView();
    public abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter();
    public abstract void onInitViews(View view);
    public abstract void onUpdateUi();
    public abstract boolean onResumeReloadList();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutContentView(), container, false);
        initView(view);
        onUpdateUi();
        if(!onResumeReloadList()) {
            getDataFromServer();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onResumeReloadList()){
            getDataFromServer();
        }
    }

    private void initView(View view) {
        onInitViews(view);
        layoutNoData = view.findViewById(R.id.ll_no_data);
        rvList = view.findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = getAdapter();
        rvList.setAdapter(adapter);
    }

    private void getDataFromServer() {
        dataManager.getDynamicData(catId, new Response.Callback<List<DMCategory>>() {
            @Override
            public void onSuccess(List<DMCategory> response) {
                loadList(response);
            }

            @Override
            public void onFailure(Exception e) {
                if(mList.size() == 0) {
                    BaseUtil.showNoData(layoutNoData, View.VISIBLE);
                }
            }
        });
    }

    private void loadList(List<DMCategory> list) {
        rvList.setVisibility(View.VISIBLE);
        BaseUtil.showNoData(layoutNoData, View.GONE);
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        if (list == null || list.size() <= 0) {
            BaseUtil.showNoData(layoutNoData, View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
}
