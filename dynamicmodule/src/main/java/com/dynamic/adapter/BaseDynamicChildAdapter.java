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

public abstract class BaseDynamicChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final Response.OnClickListener<DMContent> clickListener;
    protected final List<DMContent> mList;
    protected final String imageUrl;
    protected final int itemType;

    public BaseDynamicChildAdapter(Context context, int itemType, List<DMContent> mList, Response.OnClickListener<DMContent> clickListener) {
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
        this.itemType = itemType;
        this.mList = mList;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType);

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DMCategoryType.TYPE_GRID:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_dynamic_grid_view, parent, false));
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_dynamic_scroll_view, parent, false));
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_dynamic_slider, parent, false));
            case DMCategoryType.TYPE_LIST:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_dynamic_list_view, parent, false));
            default:
                return onCreateViewHolderDynamic(parent, viewType);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommonViewHolder) {
            CommonViewHolder holder = (CommonViewHolder) viewHolder;
            holder.setData(mList.get(position));
        }else {
            onBindViewHolderDynamic(viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    protected String getUrl(String appImage) {
        if (TextUtils.isEmpty(appImage) || BaseUtil.isValidUrl(appImage)) {
            return appImage;
        }
        return imageUrl + appImage;
    }

    protected class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final ImageView ivIcon;

        protected CommonViewHolder(View v) {
            super(v);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvTitle = v.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                clickListener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }

        public void setData(DMContent item) {
            if (tvTitle != null) {
                tvTitle.setText(item.getTitle());
            }
            if(ivIcon != null) {
                String imagePath = getUrl(item.getImage());
                int placeHolder = R.drawable.ic_dm_placeholder_icon;
                if((itemType == DMCategoryType.TYPE_GRID || itemType == DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER)){
                    placeHolder = R.drawable.ic_dm_placeholder_slider;
                }
                if (BaseUtil.isValidUrl(imagePath)) {
                    Picasso.get().load(imagePath)
                            .placeholder(placeHolder)
                            .into(ivIcon);
                } else {
                    ivIcon.setBackgroundResource(placeHolder);
                }
            }
        }
    }
}