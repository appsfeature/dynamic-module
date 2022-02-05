package com.dynamic.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DynamicChildAdapter extends BaseDynamicChildAdapter {


    public DynamicChildAdapter(Context context, int itemType, List<DMContent> mList, Response.OnClickListener<DMContent> clickListener) {
        super(context, itemType, mList, clickListener);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_dynamic_list_view, parent, false));
    }

    @Override
    public void onBindViewHolderDynamic(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CommonViewHolder) {
            CommonViewHolder holder = (CommonViewHolder) viewHolder;
            holder.setData(mList.get(i));
        }
    }

}