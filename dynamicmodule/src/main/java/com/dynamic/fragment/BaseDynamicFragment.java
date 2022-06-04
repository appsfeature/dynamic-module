package com.dynamic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dynamic.R;
import com.dynamic.adapter.BaseDynamicAdapter;
import com.dynamic.fragment.base.DMBaseFragment;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
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
    private SwipeRefreshLayout swipeRefresh;

    @LayoutRes
    public abstract int getLayoutContentView();

    public abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter();

    public abstract void onInitViews(View view);

    public abstract void onUpdateUi();

    public abstract boolean onResumeReloadList();

    public abstract List<DMCategory> getStaticList();

    public abstract void onValidateList(List<DMCategory> list, Response.Status<List<DMCategory>> callback);

    public abstract void onNetworkRequestCompleted();

    public abstract boolean onUpdateWhenListCountChanged();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutContentView(), container, false);
        initView(view);
        onUpdateUi();
        if (!onResumeReloadList()) {
            getDataFromServer();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onResumeReloadList()) {
            getDataFromServer();
        }
    }

    private void initView(View view) {
        onInitViews(view);
        layoutNoData = view.findViewById(R.id.ll_no_data);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rvList = view.findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = getAdapter();
        rvList.setAdapter(adapter);

        if(swipeRefresh != null) {
            showProgress(false);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getDataFromServer();
                        }
                    }
            );
        }
    }

    private void getDataFromServer() {
        dataManager.getDataBySubCategory(catId, getStaticList(), true, new DynamicCallback.Listener<List<DMCategory>>() {
            @Override
            public void onSuccess(List<DMCategory> response) {
                showProgress(false);
                loadList(response);
            }

            @Override
            public void onValidate(List<DMCategory> list, Response.Status<List<DMCategory>> callback) {
                onValidateList(list, callback);
            }

            @Override
            public void onFailure(Exception e) {
                if (mList.size() == 0) {
                    BaseUtil.showNoData(layoutNoData, View.VISIBLE);
                }
                showProgress(false);
            }

            @Override
            public void onRequestCompleted() {
                onNetworkRequestCompleted();
                showProgress(false);
            }
        });
    }

    protected void showProgress(boolean isShow) {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(isShow);
        }
    }

    protected void loadList(List<DMCategory> list) {
        updateList(list, true);
    }

    protected void updateList(List<DMCategory> list) {
        updateList(list, false);
    }

    private void updateList(List<DMCategory> list, boolean isClear) {
        rvList.setVisibility(View.VISIBLE);
        BaseUtil.showNoData(layoutNoData, View.GONE);
        boolean isSizeChanged = isSizeChanged(list);
        if (isClear) {
            if(onUpdateWhenListCountChanged()){
                if(list != null && isSizeChanged) {
                    mList.clear();
                }
            }else {
                mList.clear();
            }
        }
        if (list != null && list.size() > 0) {
            if(onUpdateWhenListCountChanged()){
                if(isSizeChanged) {
                    mList.addAll(list);
                }
            }else {
                mList.addAll(list);
            }
        }
        if (list == null || list.size() <= 0) {
            BaseUtil.showNoData(layoutNoData, View.VISIBLE);
        }
        if(onUpdateWhenListCountChanged()){
            if(list != null && isSizeChanged) {
                if(adapter instanceof BaseDynamicAdapter) {
                    ((BaseDynamicAdapter) adapter).resetFlags();
                }
                adapter.notifyDataSetChanged();
            }
        }else {
            if(adapter instanceof BaseDynamicAdapter) {
                ((BaseDynamicAdapter) adapter).resetFlags();
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(adapter != null && adapter instanceof BaseDynamicAdapter) {
            ((BaseDynamicAdapter) adapter).stopAnimation();
        }
    }

    private boolean isSizeChanged(List<DMCategory> list) {
        if(list != null){
            if(list.size() == 0){
                return true;
            }else if(list.size() != mList.size()){
                return true;
            }else if(list.size() == mList.size()){
                try {
                    for (int i = 0; i < mList.size(); i++) {
                        DMCategory category = mList.get(i);
                        DMCategory lCat = list.get(i);
                        if (category.getChildList() != null && lCat.getChildList() != null
                                && category.getChildList().size() != lCat.getChildList().size()) {
                            return true;
                        } else if (category.getChildCategoryList() != null && lCat.getChildCategoryList() != null
                                && category.getChildCategoryList().size() != lCat.getChildCategoryList().size()) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
