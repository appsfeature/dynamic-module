package com.dynamic.adapter;


import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMOtherProperty;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public abstract class BaseDynamicChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final Response.OnClickListener<DMContent> clickListener;
    protected final List<DMContent> mList;
    protected final String imageUrl;
    protected final int itemType;
    protected final DMCategory category;
    private final Context context;
    private final DMOtherProperty otherProperty;

    public BaseDynamicChildAdapter(Context context, int itemType, DMCategory category, List<DMContent> mList, Response.OnClickListener<DMContent> clickListener) {
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
        this.context = context;
        this.itemType = itemType;
        this.category = category;
        this.mList = mList;
        this.clickListener = clickListener;
        this.otherProperty = category != null ? category.getOtherPropertyModel() : null;
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
            case DMCategoryType.TYPE_LIST:
            case DMCategoryType.TYPE_GRID_HORIZONTAL:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
            case DMCategoryType.TYPE_LIST_CARD:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_view, parent, false));
            case DMCategoryType.TYPE_GRID:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_grid_card_view, parent, false));
            case DMCategoryType.TYPE_GRID_CARD:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_grid_view, parent, false));
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_scroll_view, parent, false));
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_slider, parent, false));
            default:
                return onCreateViewHolderDynamic(parent, viewType);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommonViewHolder) {
            CommonViewHolder holder = (CommonViewHolder) viewHolder;
            holder.setData(mList.get(position), position);
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
        private final View cardView;

        protected CommonViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
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

        public void setData(DMContent item, int pos) {
            if (tvTitle != null) {
                if(itemType != DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE) {
                    tvTitle.setText(item.getTitle());
                    tvTitle.setVisibility(View.VISIBLE);
                }else {
                    tvTitle.setVisibility(View.GONE);
                }
            }
            if(ivIcon != null) {
                String imagePath = getUrl(item.getImage());
                int placeHolder = getPlaceHolder();
                if (BaseUtil.isValidUrl(imagePath)) {
                    Picasso.get().load(imagePath)
                            .placeholder(placeHolder)
                            .into(ivIcon);
                } else {
                    ivIcon.setImageResource(placeHolder);
                }
            }
            applyStyle(item, pos);
        }

        private void applyStyle(DMContent item, int pos) {
            if (otherProperty != null) {
                if (otherProperty.isRandomBGColor()) {
                    if(cardView != null){
                        setColorFilter(cardView.getBackground(), getSequentialColor(pos));
                    }
                }
                if (otherProperty.isRandomIconColor()) {
                    if(ivIcon != null){
                        setColorFilter(ivIcon, getSequentialColor(pos));
                    }
                }

            }
        }
    }

    protected int getPlaceHolder() {
        switch (itemType) {
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return R.drawable.ic_dm_placeholder_slider;
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return R.drawable.ic_dm_placeholder_graphic;
            default:
                return R.drawable.ic_dm_placeholder_icon;
        }
    }

    @SuppressWarnings("deprecation")
    public static void setColorFilter(@NonNull Object drawable, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(drawable instanceof ImageView) {
                ((ImageView)drawable).setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            }else if(drawable instanceof Drawable) {
                ((Drawable)drawable).setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            }
        } else {
            if(drawable instanceof ImageView) {
                ((ImageView)drawable).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }else if(drawable instanceof Drawable) {
                ((Drawable)drawable).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private int getSequentialColor(int i) {
        String[] colors = context.getResources().getStringArray(R.array.dynamic_colors);
        if(i % colors.length == 0) {
            return Color.parseColor(colors[0]);
        } else  {
            for (int c = 1; c < colors.length; c++){
                if(i == c || (i-c) % colors.length == 0) {
                    return Color.parseColor(colors[c]);
                }
            }
            return Color.parseColor(colors[0]);
        }
    }
}