package com.dynamic.adapter.holder.base;

import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.util.List;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public abstract class DynamicCommonHolder<T1,T2> extends BaseCommonHolder<T1> {

    protected int mListSize = 0;

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, T1 category, List<T2> childList);

    public DynamicCommonHolder(View view) {
        super(view);
    }

    public void setData(T1 mItem, int position) {
        setData(mItem, position, false);
    }
    public void setData(T1 mItem, int position, boolean isHorizontalScroll) {
        if(mItem instanceof DMCategory) {
            DMCategory<DMContent> item = ((DMCategory) mItem);

            setOtherProperty(item.getOtherPropertyModel());
            if (tvTitle != null) {
                if (!TextUtils.isEmpty(item.getTitle())) {
                    tvTitle.setText(item.getTitle());
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
            }
            if (recyclerView != null) {
                if (item.getChildList() != null && item.getChildList().size() > 0) {
                    adapter = getChildAdapter(item.getItemType(), mItem, (List<T2>) item.getChildList());
                    recyclerView.setLayoutManager(getLayoutManager(mItem, isHorizontalScroll));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
            mListSize = item.getChildList() != null ? item.getChildList().size() : 0;

            applyStyle(mItem);
        }
    }
}