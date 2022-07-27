package com.dynamic.fragment.pager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.dynamic.R;
import com.dynamic.adapter.DynamicChildAdapter;
import com.dynamic.fragment.base.DMBaseFragment;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;


public class DynamicPagerFragment extends DMBaseFragment {

    private static final long SLIDER_DELAY_TIME_IN_MILLIS = 3000;
    private View layoutNoData;
    private final List<DMContent> mList = new ArrayList<>();
    private Activity activity;
    private ViewPager2 viewPager;
    private WormDotsIndicator indicatorView;
    private DynamicCallback.OnDynamicPagerListener mClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DynamicCallback.OnDynamicPagerListener) {
            mClickListener = (DynamicCallback.OnDynamicPagerListener) context;
        }
    }

    public static DynamicPagerFragment getInstance(int catId) {
        DynamicPagerFragment fragment = new DynamicPagerFragment();
        fragment.setArguments(DMUtility.getPropertyBundle(catId));
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dm_fragment_dynamic_pager, container, false);
        activity = getActivity();
        initView(view);
        loadData();
        return view;
    }

    private void loadData() {
        getSliderDataFromServer();
    }


    private void initView(View view) {
        layoutNoData = view.findViewById(R.id.ll_no_data);
        viewPager = view.findViewById(R.id.view_pager);
        indicatorView = view.findViewById(R.id.indicator_view);
    }

    private void openItemOnClicked(View view, DMContent item) {
        if(BaseUtil.isValidUrl(item.getLink())) {
            if (mClickListener != null) {
                mClickListener.onItemClicked(activity, view, item);
            }
        }else {
            BaseUtil.showToast(activity, "Update later.");
        }
    }

    private void getSliderDataFromServer() {
        dataManager.getContent(catId, new DynamicCallback.Listener<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {
                loadViewPager(response);
            }

            @Override
            public void onFailure(Exception e) {
                if(mList.size() == 0) {
                    BaseUtil.showNoData(layoutNoData, View.GONE);
                }
            }
        });
    }

    private final Handler sliderHandler = new Handler(Looper.myLooper());

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int nextPos = viewPager.getCurrentItem() + 1;
            if(mList.size() > 0) {
                if(mList.size() > nextPos) {
                    viewPager.setCurrentItem(nextPos);
                }else{
                    viewPager.setCurrentItem(0);
                }
            }
        }
    };

    private void loadViewPager(List<DMContent> response) {
        BaseUtil.showNoData(layoutNoData, View.GONE);
        mList.clear();
        mList.addAll(response);
        viewPager.setAdapter(new DynamicChildAdapter<>(activity, DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER, DMUtility.getCategory(property), mList, new Response.OnClickListener<DMContent>() {
            @Override
            public void onItemClicked(View view, DMContent item) {
                openItemOnClicked(view, item);
            }
        }));
        indicatorView.setViewPager2(viewPager);
        viewPager.setVisibility(View.VISIBLE);
        indicatorView.setVisibility(mList.size() > 1 ? View.VISIBLE : View.GONE);
        viewPager.setOffscreenPageLimit(3);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY_TIME_IN_MILLIS);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY_TIME_IN_MILLIS);
    }
}
