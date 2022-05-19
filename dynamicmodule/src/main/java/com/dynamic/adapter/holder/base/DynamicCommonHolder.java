package com.dynamic.adapter.holder.base;

import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.util.List;

public abstract class DynamicCommonHolder extends BaseCommonHolder {

    protected int mListSize = 0;

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, DMCategory category, List<DMContent> childList);

    public DynamicCommonHolder(View view) {
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
            if (item.getChildList() != null && item.getChildList().size() > 0) {
                adapter = getChildAdapter(item.getItemType(), item, item.getChildList());
                recyclerView.setLayoutManager(getLayoutManager(item));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }
        mListSize = item.getChildList() != null ? item.getChildList().size() : 0;

        applyStyle(item);
    }
}