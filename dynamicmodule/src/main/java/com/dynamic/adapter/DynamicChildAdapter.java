package com.dynamic.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;

import java.util.List;

public class DynamicChildAdapter extends BaseDynamicChildAdapter {


    public DynamicChildAdapter(Context context, int itemType, DMCategory category, List<DMContent> mList, Response.OnClickListener<DMContent> clickListener) {
        super(context, itemType, category, mList, clickListener);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_view, parent, false));
    }

    @Override
    public void onBindViewHolderDynamic(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CommonViewHolder) {
            CommonViewHolder holder = (CommonViewHolder) viewHolder;
            holder.setData(mList.get(i), i);
        }
    }

}