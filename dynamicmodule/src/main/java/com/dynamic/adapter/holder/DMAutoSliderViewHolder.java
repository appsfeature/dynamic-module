package com.dynamic.adapter.holder;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.dynamic.R;
import com.dynamic.adapter.holder.base.AbstractDynamicAdapter;
import com.dynamic.model.DMCategory;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public abstract class DMAutoSliderViewHolder extends AbstractDynamicAdapter {
    protected static final long SLIDER_DELAY_TIME_IN_MILLIS = 3000;

    private final ViewPager2 viewPager;
    private final WormDotsIndicator indicatorView;

    public DMAutoSliderViewHolder(View view) {
        super(view);
        viewPager = view.findViewById(R.id.view_pager);
        indicatorView = view.findViewById(R.id.indicator_view);
    }

    public void setData(DMCategory item, int position) {
        if(item.getChildList() != null && item.getChildList().size() > 0) {
            viewPager.setAdapter(getChildAdapter(item.getItemType(), item, item.getChildList()));
            indicatorView.setViewPager2(viewPager);
            viewPager.setVisibility(View.VISIBLE);
            indicatorView.setVisibility(item.getChildList().size() > 1 ? View.VISIBLE : View.GONE);
            viewPager.setOffscreenPageLimit(item.getChildList().size());
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY_TIME_IN_MILLIS);
                }
            });
        }else {
            viewPager.setVisibility(View.GONE);
            indicatorView.setVisibility(View.GONE);
        }
    }

    private final Handler sliderHandler = new Handler(Looper.myLooper());

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int nextPos = viewPager.getCurrentItem() + 1;
            if(viewPager.getAdapter() != null && viewPager.getAdapter().getItemCount() > 0) {
                if(viewPager.getAdapter().getItemCount() > nextPos) {
                    viewPager.setCurrentItem(nextPos);
                }else{
                    viewPager.setCurrentItem(0);
                }
            }
        }
    };
}