package com.dynamic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.adapter.DynamicChildAdapter;
import com.dynamic.adapter.holder.BaseCommonHolder;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;

import java.util.ArrayList;
import java.util.List;


public class DynamicListFragment extends DMBaseFragment {
    private View layoutNoData;
    private DynamicChildAdapter adapter;
    private final List<DMContent> mList = new ArrayList<>();
    private Activity activity;
    private RecyclerView rvList;
    private DynamicCallback.OnDynamicListListener mClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DynamicCallback.OnDynamicListListener) {
            mClickListener = (DynamicCallback.OnDynamicListListener) context;
        }
    }

    public static DynamicListFragment getInstance(int catId) {
        DynamicListFragment fragment = new DynamicListFragment();
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
        rvList = view.findViewById(R.id.recycler_view);
        DMCategory item = getCategoryItem();
        BaseCommonHolder holder = new BaseCommonHolder(new View(activity));
        holder.setOtherProperty(item.getOtherPropertyModel());
        rvList.setLayoutManager(holder.getLayoutManager(item));
        adapter = new DynamicChildAdapter(activity, property.getItemType(), null, mList, new Response.OnClickListener<DMContent>() {
            @Override
            public void onItemClicked(View view, DMContent item) {
                openItemOnClicked(view, item);
            }
        });
        rvList.setAdapter(adapter);
    }

    private void openItemOnClicked(View view, DMContent item) {
        if(BaseUtil.isValidUrl(item.getLink())) {
            if (mClickListener != null) {
                mClickListener.onItemClicked(view, item);
            }
        }else {
            BaseUtil.showToast(activity, "Update later.");
        }
    }

    private void getDataFromServer() {
        dataManager.getDataByCategory(catId, new Response.Callback<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {
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

    private void loadList(List<DMContent> list) {
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

    private DMCategory getCategoryItem() {
        DMCategory item = new DMCategory();
        item.setItemType(property.getItemType());
        item.setOtherProperty(property.getOtherProperty());
        item.setChildList(mList);
        return item;
    }
}