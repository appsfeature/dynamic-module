package com.dynamic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.adapter.holder.BaseCommonHolder;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;

import java.util.List;

public class DynamicAdapter extends BaseDynamicAdapter {

    public DynamicAdapter(Context context, List<DMCategory> mList, Response.OnClickListener<DMContent> listener) {
        super(context, mList, listener);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        return new BaseDynamicAdapter.CommonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false));
    }

    @Override
    protected void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        DMCategory item = mList.get(position);
        if (viewHolder instanceof CommonHolder) {
            CommonHolder holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
        }
    }

    @Override
    protected RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, DMCategory category, List<DMContent> childList) {
        return new DynamicChildAdapter(context, itemType, category, childList, listener);
    }


    public class ScrollViewHolder extends BaseCommonHolder {
        protected final TextView tvTitle;
        ScrollViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
        }
        public void setData(DMCategory item, int position) {

        }
    }
}