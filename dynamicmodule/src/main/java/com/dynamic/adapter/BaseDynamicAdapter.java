package com.dynamic.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.adapter.holder.BaseCommonHolder;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMOtherProperty;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.List;

public abstract class BaseDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final long SLIDER_DELAY_TIME_IN_MILLIS = 3000;
    protected final Response.OnClickListener<DMContent> listener;
    protected final List<DMCategory> mList;
    protected final String imageUrl;
    protected final Context context;

    public BaseDynamicAdapter(Context context, List<DMCategory> mList, Response.OnClickListener<DMContent> listener) {
        this.context = context;
        this.listener = listener;
        this.mList = mList;
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getItemType();
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        switch (position) {
            case DMCategoryType.TYPE_LIST:
            case DMCategoryType.TYPE_GRID:
            case DMCategoryType.TYPE_GRID_HORIZONTAL:
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
            case DMCategoryType.TYPE_TITLE_ONLY:
            case DMCategoryType.TYPE_TITLE_WITH_COUNT:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list, viewGroup, false));
            case DMCategoryType.TYPE_LIST_CARD:
            case DMCategoryType.TYPE_GRID_CARD:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list_card, viewGroup, false));
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return new AutoSliderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_auto_slider, viewGroup, false));
            default:
                return onCreateViewHolderDynamic(viewGroup, position);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        DMCategory item = mList.get(position);
        if (viewHolder instanceof CommonHolder) {
            CommonHolder holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
        }else if (viewHolder instanceof AutoSliderViewHolder) {
            AutoSliderViewHolder holder = (AutoSliderViewHolder) viewHolder;
            holder.setData(item, position);
        }else {
            onBindViewHolderDynamic(viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AutoSliderViewHolder extends RecyclerView.ViewHolder{
        private final ViewPager2 viewPager;
        private final WormDotsIndicator indicatorView;

        AutoSliderViewHolder(View view) {
            super(view);
            viewPager = view.findViewById(R.id.view_pager);
            indicatorView = view.findViewById(R.id.indicator_view);
        }

        public void setData(DMCategory item, int position) {
            viewPager.setAdapter(getDynamicChildAdapter(item.getItemType(), item, item.getChildList()));
            indicatorView.setViewPager2(viewPager);
            viewPager.setVisibility(View.VISIBLE);
            indicatorView.setVisibility(mList.size() > 1 ? View.VISIBLE : View.GONE);
            viewPager.setOffscreenPageLimit(mList.size());
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY_TIME_IN_MILLIS);
                }
            });
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

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, DMCategory category, List<DMContent> childList);

    public class CommonHolder extends BaseCommonHolder {

        public CommonHolder(View view) {
            super(view);
        }

        public void setData(DMCategory item, int position) {
            setOtherProperty(item.getOtherPropertyModel());
            if(tvTitle != null) {
                if (!TextUtils.isEmpty(item.getTitle())) {
                    tvTitle.setText(item.getTitle());
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
            }
            if(recyclerView != null) {
                if (item.getChildList() != null) {
                    adapter = getDynamicChildAdapter(item.getItemType(), item, item.getChildList());
                    recyclerView.setLayoutManager(getLayoutManager(item));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            applyStyle(item);
        }
    }
}