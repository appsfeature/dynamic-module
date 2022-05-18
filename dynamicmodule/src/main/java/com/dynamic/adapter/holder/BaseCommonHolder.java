package com.dynamic.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMOtherProperty;

public class BaseCommonHolder extends RecyclerView.ViewHolder{
    protected RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    protected final TextView tvTitle;
    protected final RecyclerView recyclerView;
    protected DMOtherProperty otherProperty;
    protected static final int defaultGridCount = 2;
    protected int mScrollSpeed = 5000;
    protected boolean isEnableAutoScroll = false;

    public BaseCommonHolder(View view) {
        super(view);
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    public void setOtherProperty(DMOtherProperty mOtherProperty) {
        this.otherProperty = mOtherProperty;
        if (otherProperty != null) {
            isEnableAutoScroll = otherProperty.isEnableAutoScroll();
            mScrollSpeed = otherProperty.getScrollSpeed();
        }
    }

    public void applyStyle(DMCategory item) {
        if (otherProperty != null) {
            if (tvTitle != null) {
                tvTitle.setVisibility(otherProperty.isHideTitle() ? View.GONE : View.VISIBLE);
            }
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(DMCategory item) {
        if(item.getItemType() == DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL){
            return new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        }else {
            return new GridLayoutManager(itemView.getContext(), getSpanCount(item));
        }
    }

    public int getSpanCount(DMCategory item) {
        if(item.getItemType() == DMCategoryType.TYPE_GRID || item.getItemType() == DMCategoryType.TYPE_GRID_HORIZONTAL
                || item.getItemType() == DMCategoryType.TYPE_GRID_CARD) {
            if (otherProperty != null) {
                if (otherProperty.isGridAutoAdjust() && item.getChildList() != null) {
                    if (item.getChildList().size() > 0 && item.getChildList().size() <= 4) {
                        return item.getChildList().size();
                    } else {
                        return 3;
                    }
                } else if (otherProperty.getGridCount() > 0) {
                    return otherProperty.getGridCount();
                }
            }
            return defaultGridCount;
        }else {
            return 1;
        }
    }
}
