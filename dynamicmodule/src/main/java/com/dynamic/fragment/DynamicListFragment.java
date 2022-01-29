package com.dynamic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.adapter.DynamicAdapter;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.network.DMNetworkManager;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;

import java.util.ArrayList;
import java.util.List;


public class DynamicListFragment extends Fragment {
    private View layoutNoData;
    private DynamicAdapter adapter;
    private final List<DMCategory> mList = new ArrayList<>();
    private Activity activity;
    private DMNetworkManager networkManager;
    private RecyclerView rvList;
    private DynamicCallback.OnDynamicListListener mClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DynamicCallback.OnDynamicListListener) {
            mClickListener = (DynamicCallback.OnDynamicListListener) context;
        }
    }

    public static DynamicListFragment getInstance() {
        DynamicListFragment fragment = new DynamicListFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(LoginConstant.LOGIN_TYPE, getLoginType());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_list, container, false);
        activity = getActivity();
        networkManager = new DMNetworkManager(activity);
        initDataFromArguments();
        initView(view);
        loadData();
        return view;
    }

    private void initDataFromArguments() {
        if(getArguments() != null){
//            loginType = getArguments().getInt(LoginConstant.LOGIN_TYPE, LoginType.DEFAULT_USER);
        }
    }

    private void loadData() {
        getSliderDataFromServer();
    }


    private void initView(View view) {
        layoutNoData = view.findViewById(R.id.ll_no_data);
        rvList = view.findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new DynamicAdapter(activity, mList, new Response.OnClickListener<DMContent>() {
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

    private void getSliderDataFromServer() {
        networkManager.getDynamicCategory(new Response.Callback<List<DMCategory>>() {
            @Override
            public void onSuccess(List<DMCategory> response) {
                loadList(response);
            }

            @Override
            public void onFailure(Exception e) {
                if(mList.size() == 0) {
                    BaseUtil.showNoData(layoutNoData, View.GONE);
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
