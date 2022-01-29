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
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Response.OnClickListener<DMContent> listener;
    private final List<DMCategory> mList;
    private final String imageUrl;

    public DynamicAdapter(Context context, List<DMCategory> mList, Response.OnClickListener<DMContent> listener) {
        this.listener = listener;
        this.mList = mList;
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
    }

    public interface ItemType {
        int ITEM_TYPE_LIST = 0;
        int ITEM_TYPE_GRID = 1;
        int ITEM_TYPE_SCROLL = 2;
        int ITEM_TYPE_SLIDER = 3;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case ItemType.ITEM_TYPE_GRID:
            case ItemType.ITEM_TYPE_SCROLL:
                return new ScrollViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_dynamic_grid, viewGroup, false));
            case ItemType.ITEM_TYPE_LIST:
            default:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_dynamic_list, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        DMCategory item = mList.get(position);
        if (viewHolder instanceof ScrollViewHolder) {
            ScrollViewHolder holder = (ScrollViewHolder) viewHolder;
            holder.setData(item, position);
        }else if (viewHolder instanceof CommonHolder) {
            CommonHolder holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getSpanCount(int adapterPosition) {
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

    public class ScrollViewHolder extends CommonHolder{
        ScrollViewHolder(View view) {
            super(view);
        }
    }

    public class CommonHolder extends RecyclerView.ViewHolder{
        private final ImageView ivIcon;
        protected DynamicChildAdapter adapter;
        protected final TextView tvTitle;
        protected final RecyclerView recyclerView;

        CommonHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            ivIcon = view.findViewById(R.id.iv_icon);
            recyclerView = itemView.findViewById(R.id.recycler_view);
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
                    adapter = new DynamicChildAdapter(itemView.getContext(), item.getItemType(), item.getChildList(), listener);
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
                int placeHolder = R.drawable.ic_slider_placeholder;
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

    private String getUrl(String appImage) {
        if (TextUtils.isEmpty(appImage) || BaseUtil.isValidUrl(appImage)) {
            return appImage;
        }
        return imageUrl + appImage;
    }
}