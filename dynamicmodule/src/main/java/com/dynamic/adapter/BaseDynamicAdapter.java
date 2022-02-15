package com.dynamic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public abstract class BaseDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final Response.OnClickListener<DMContent> listener;
    protected final List<DMCategory> mList;
    protected final String imageUrl;
    protected final Context context;
    protected static final int DEFAULT_GRID_COUNT = 2;

    public BaseDynamicAdapter(Context context, List<DMCategory> mList, Response.OnClickListener<DMContent> listener) {
        this.context = context;
        this.listener = listener;
        this.mList = mList;
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getItemType();
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        switch (position) {
            case DMCategoryType.TYPE_LIST:
            case DMCategoryType.TYPE_GRID:
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_slot_dynamic_list, viewGroup, false), DEFAULT_GRID_COUNT);
            case DMCategoryType.TYPE_LIST_CARD:
            case DMCategoryType.TYPE_GRID_CARD:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_slot_dynamic_list_card, viewGroup, false), DEFAULT_GRID_COUNT);
            default:
                return onCreateViewHolderDynamic(viewGroup, position);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        DMCategory item = mList.get(position);
        if (viewHolder instanceof CommonHolder) {
            CommonHolder holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
        }else {
            onBindViewHolderDynamic(viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    public class ScrollViewHolder extends CommonHolder{
//        ScrollViewHolder(View view) {
//            super(view);
//        }
//    }

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, List<DMContent> childList);

    public class CommonHolder extends RecyclerView.ViewHolder{
        private final ImageView ivIcon;
        private final int mGridSize;
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
        protected final TextView tvTitle;
        protected final RecyclerView recyclerView;

        CommonHolder(View view, int gridSize) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            ivIcon = view.findViewById(R.id.iv_icon);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            mGridSize = gridSize;
        }

        public void setData(DMCategory item, int position) {
            if(tvTitle != null) {
                if (!TextUtils.isEmpty(item.getTitle())) {
                    tvTitle.setText(item.getTitle());
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
            }
            if(recyclerView != null) {
                if (item.getChildList() != null) {
                    adapter = getDynamicChildAdapter(item.getItemType(), item.getChildList());
                    recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), getSpanCount(position)));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
            if(ivIcon != null){
                String imagePath = getUrl(item.getImage());
                int placeHolder = R.drawable.ic_dm_placeholder_slider;
                if (BaseUtil.isValidUrl(imagePath)) {
                    Picasso.get().load(imagePath)
                            .placeholder(placeHolder)
                            .into(ivIcon);
                } else {
                    ivIcon.setBackgroundResource(placeHolder);
                }
            }
        }

        public int getSpanCount(int adapterPosition) {
            if(mGridSize > 0){
                return mGridSize;
            }
            if(adapterPosition >= 0 && mList != null && mList.size() > adapterPosition){
                if(mList.get(adapterPosition).getChildList() != null
                        && mList.get(adapterPosition).getChildList().size() > 0
                        && mList.get(adapterPosition).getChildList().size() <= 4){
                    return mList.get(adapterPosition).getChildList().size();
                }else {
                    return 3;
                }
            }else {
                return 1;
            }
        }
    }

    public String getUrl(String appImage) {
        if (TextUtils.isEmpty(appImage) || BaseUtil.isValidUrl(appImage)) {
            return appImage;
        }
        return imageUrl + appImage;
    }
}