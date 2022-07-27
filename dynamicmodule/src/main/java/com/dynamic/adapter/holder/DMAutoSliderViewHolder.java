package com.dynamic.adapter.holder;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.dynamic.R;
import com.dynamic.adapter.holder.base.AbstractDynamicAdapter;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.List;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public abstract class DMAutoSliderViewHolder<T1, T2> extends AbstractDynamicAdapter<T1, T2> {
    protected static final long SLIDER_DELAY_TIME_IN_MILLIS = 3000;

    public final ViewPager2 viewPager;
    public final WormDotsIndicator indicatorView;

    public DMAutoSliderViewHolder(View view) {
        super(view);
        viewPager = view.findViewById(R.id.view_pager);
        indicatorView = view.findViewById(R.id.indicator_view);
    }

    public void setData(T1 mItem, int position) {
        if(mItem instanceof DMCategory) {
            DMCategory<DMContent> item = ((DMCategory) mItem);
            if (item.getChildList() != null && item.getChildList().size() > 0) {
                viewPager.setAdapter(getChildAdapter(item.getItemType(), mItem, (List<T2>) item.getChildList()));
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
            } else {
                viewPager.setVisibility(View.GONE);
                indicatorView.setVisibility(View.GONE);
            }
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