package com.sample.dynamicmodule.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.adapter.BaseDynamicChildAdapter;
import com.dynamic.adapter.holder.base.BaseCommonHolder;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;
import com.sample.dynamicmodule.R;
import com.sample.dynamicmodule.model.CategoryModel;
import com.sample.dynamicmodule.model.ContentModel;

import java.util.List;

public class HomeChildAdapter extends BaseDynamicChildAdapter<CategoryModel, ContentModel> {


    public HomeChildAdapter(Context context, int itemType, CategoryModel category, List<ContentModel> mList, Response.OnClickListener<ContentModel> clickListener) {
        super(context, itemType, category, mList, clickListener);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        if(viewType == HomeAdapter.TYPE_LOGIN){
            return new LoginViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
        }else {
            return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolderDynamic(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LoginViewHolder) {
            LoginViewHolder holder = (LoginViewHolder) viewHolder;
            holder.setData(mList.get(position), position);
        } else if (viewHolder instanceof CommonChildHolder) {// this viewHolder is always on the bottom
            CommonChildHolder<ContentModel> holder = (CommonChildHolder) viewHolder;
            holder.setData(mList.get(position), position);
        }
    }

    public class LoginViewHolder extends BaseCommonHolder<CategoryModel> implements View.OnClickListener{
        protected final TextView tvTitle;

        LoginViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }
        public void setData(DMContent item, int position) {
            tvTitle.setText(item.getTitle());
        }

        @Override
        public void onClick(View view) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                clickListener.onItemClicked(view, mList.get(getAbsoluteAdapterPosition()));
            }
        }
    }

}