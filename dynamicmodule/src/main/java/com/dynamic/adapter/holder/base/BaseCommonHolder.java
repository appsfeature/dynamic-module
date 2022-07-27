package com.dynamic.adapter.holder.base;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMOtherProperty;
import com.dynamic.util.DMConstants;

/**
 * @param <T1> : DMCategory
 */
public class BaseCommonHolder<T1> extends RecyclerView.ViewHolder{
    public RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    public final TextView tvTitle;
    public final RecyclerView recyclerView;
    public DMOtherProperty otherProperty;
    public static final int defaultGridCount = 2;
    public int mScrollSpeed = DMConstants.DEFAULT_SCROLL_SPEED;
    public boolean isEnableAutoScroll = false;

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

    public void applyStyle(T1 item) {
        if (otherProperty != null) {
            if (tvTitle != null) {
                tvTitle.setVisibility(otherProperty.isHideTitle() ? View.GONE : View.VISIBLE);
            }
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(T1 item) {
        if(item instanceof DMCategory && ((DMCategory) item).getItemType() == DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL){
            return new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        }else {
            return new GridLayoutManager(itemView.getContext(), getSpanCount(item));
        }
    }

    public int getSpanCount(T1 mItem) {
        if(mItem instanceof DMCategory) {
            DMCategory<DMContent> item = ((DMCategory) mItem);
            if (item.getItemType() == DMCategoryType.TYPE_GRID || item.getItemType() == DMCategoryType.TYPE_GRID_HORIZONTAL
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
            } else {
                return 1;
            }
        }else {
            return 1;
        }
    }
}
