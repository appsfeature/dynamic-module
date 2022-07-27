package com.sample.dynamicmodule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.adapter.BaseDynamicAdapter;
import com.dynamic.adapter.holder.base.BaseCommonHolder;
import com.dynamic.listeners.DynamicCallback;
import com.helper.callback.Response;
import com.sample.dynamicmodule.R;
import com.sample.dynamicmodule.model.CategoryModel;
import com.sample.dynamicmodule.model.ContentModel;

import java.util.List;

public class HomeAdapter extends BaseDynamicAdapter<CategoryModel, ContentModel> {

    public static final int TYPE_LOGIN = 1001;

    public HomeAdapter(Context context, List<CategoryModel> mList, DynamicCallback.OnClickListener<CategoryModel, ContentModel> listener) {
        super(context, mList, listener);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_LOGIN){
            return new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false));
        }else {
            return new CommonHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false));
        }
    }

    @Override
    protected void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CategoryModel item = mList.get(position);
        if (viewHolder instanceof AppViewHolder) {
            AppViewHolder holder = (AppViewHolder) viewHolder;
            holder.setData(item, position);
        }else if (viewHolder instanceof CommonHolder) {// this viewHolder is always on the bottom
            CommonHolder<CategoryModel, ContentModel> holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
            holder.tvTitle.setVisibility(View.GONE);
        }
    }

    @Override
    protected <T1, T2> RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, T1 category, List<T2> childList) {
        return new HomeChildAdapter(context, itemType, (CategoryModel) category, (List<ContentModel>) childList, listener);
    }


    public class AppViewHolder extends BaseCommonHolder<CategoryModel> {
        private final ImageView ivImage;

        AppViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_icon);
        }

        public void setData(CategoryModel item, int position) {
            if(recyclerView != null) {
                if (item.getChildList() != null && item.getChildList().size() > 0) {
                    adapter = getDynamicChildAdapter(item.getItemType(), item, item.getChildList());
                    recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
    }
}