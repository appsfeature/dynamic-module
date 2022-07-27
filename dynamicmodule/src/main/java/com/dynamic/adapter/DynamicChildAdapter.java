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

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public class DynamicChildAdapter<T1,T2> extends BaseDynamicChildAdapter<T1, T2> {


    public DynamicChildAdapter(Context context, int itemType, T1 category, List<T2> mList, Response.OnClickListener<T2> clickListener) {
        super(context, itemType, category, mList, clickListener);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
    }

    @Override
    public void onBindViewHolderDynamic(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommonChildHolder) {// this viewHolder is always on the bottom
            CommonChildHolder<T1> holder = (CommonChildHolder) viewHolder;
            holder.setData((T1) mList.get(position), position);
        }
    }

}