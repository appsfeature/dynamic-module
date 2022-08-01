package com.sample.dynamicmodule.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dynamic.R;
import com.dynamic.adapter.DynamicAdapter;
import com.dynamic.fragment.base.DMBaseFragment;
import com.dynamic.fragment.base.DMBaseFragmentGeneric;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.sample.dynamicmodule.adapter.HomeAdapter;
import com.sample.dynamicmodule.model.CategoryModel;
import com.sample.dynamicmodule.model.ContentModel;
import com.sample.dynamicmodule.model.ExtraProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends DMBaseFragmentGeneric<ExtraProperty> {
    private View layoutNoData;
    private HomeAdapter adapter;
    private final List<CategoryModel> mList = new ArrayList<>();
    private Activity activity;
    private RecyclerView rvList;
    private DynamicCallback.OnDynamicListListener mClickListener;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DynamicCallback.OnDynamicListListener) {
            mClickListener = (DynamicCallback.OnDynamicListListener) context;
        }
    }

    public static HomeFragment getInstance(int catId) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(DMUtility.getPropertyBundle(catId));
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dm_fragment_dynamic, container, false);
        activity = getActivity();
        initView(view);
        loadData();
        return view;
    }

    private void loadData() {
        getDataFromServer();
    }


    private void initView(View view) {
        layoutNoData = view.findViewById(R.id.ll_no_data);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rvList = view.findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new HomeAdapter(activity, mList, new DynamicCallback.OnClickListener<CategoryModel, ContentModel>() {
            @Override
            public void onItemClicked(View v, CategoryModel category, ContentModel item) {
                openItemOnClicked(view, item);
            }
        });
        rvList.setAdapter(adapter);

        if(swipeRefresh != null) {
            showProgress(false);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });
        }
    }

    private void openItemOnClicked(View view, DMContent item) {
        if (mClickListener != null) {
            mClickListener.onItemClicked(activity, view, property, item);
        }else {
            if(!BaseUtil.isValidUrl(item.getLink())) {
                BaseUtil.showToast(activity, "Update later.");
            }
        }
    }

    private void getDataFromServer() {
//        dataManager.getDataBySubCategory(catId, null, true, new DynamicCallback.Listener<List<DMCategory<DMContent>>>() {
//            @Override
//            public void onSuccess(List<DMCategory<DMContent>> response) {
//                showProgress(false);
//                loadList(response);
//            }
//
//            @Override
//            public void onValidate(List<DMCategory<DMContent>> list, Response.Status<List<DMCategory<DMContent>>> callback) {
//                callback.onSuccess(list);;
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                if (mList.size() == 0) {
//                    BaseUtil.showNoData(layoutNoData, View.VISIBLE);
//                }
//                showProgress(false);
//            }
//
//            @Override
//            public void onRequestCompleted() {
//                showProgress(false);
//            }
//        });
    }

    private void loadList(List<CategoryModel> list) {
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


    protected void showProgress(boolean isShow) {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(isShow);
        }
    }

}
