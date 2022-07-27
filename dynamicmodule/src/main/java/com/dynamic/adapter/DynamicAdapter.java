package com.dynamic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.adapter.holder.base.BaseCommonHolder;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;

import java.util.List;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public class DynamicAdapter<T1, T2> extends BaseDynamicAdapter<T1,T2> {

    public DynamicAdapter(Context context, List<T1> mList, DynamicCallback.OnClickListener<T1, T2> listener) {
        super(context, mList, listener);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        return new BaseDynamicAdapter.CommonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false));
    }

    @Override
    protected void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        T1 item = mList.get(position);
        if (viewHolder instanceof CommonHolder) {// this viewHolder is always on the bottom
            CommonHolder holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
        }
    }

    @Override
    protected <T11, T21> RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, T11 category, List<T21> childList) {
        return new DynamicChildAdapter<>(context, itemType, (T1)category, (List<T2>) childList, listener)
                .setMediumVideoPlaceholderQuality(false);
    }

//    @Override
//    protected RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, T1 category, List<T2> childList) {
//        return new DynamicChildAdapter<T1, T2>(context, itemType, category, childList, listener)
//                .setMediumVideoPlaceholderQuality(false);
//    }


    public class ScrollViewHolder extends BaseCommonHolder<T1> {
        protected final TextView tvTitle;
        ScrollViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
        }
        public void setData(DMCategory<DMContent> item, int position) {

        }
    }
}