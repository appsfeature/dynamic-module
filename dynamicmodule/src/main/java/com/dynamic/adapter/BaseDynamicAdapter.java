package com.dynamic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.adapter.holder.DMAutoSliderViewHolder;
import com.dynamic.adapter.holder.DMHorizontalCardScrollHolder;
import com.dynamic.adapter.holder.base.DynamicCommonHolder;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;

import java.util.List;

public abstract class BaseDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final Response.OnClickListener<DMContent> listener;
    protected final List<DMCategory> mList;
    protected final String imageUrl;
    protected final Context context;

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

    public void resetFlags() {
        this.mSlotWidth = 0;
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        switch (position) {
            case DMCategoryType.TYPE_LIST:
            case DMCategoryType.TYPE_GRID:
            case DMCategoryType.TYPE_GRID_HORIZONTAL:
            case DMCategoryType.TYPE_TITLE_ONLY:
            case DMCategoryType.TYPE_TITLE_WITH_COUNT:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list, viewGroup, false));
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return new HorizontalCardScrollHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list, viewGroup, false));
            case DMCategoryType.TYPE_LIST_CARD:
            case DMCategoryType.TYPE_GRID_CARD:
                return new CommonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list_card, viewGroup, false));
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return new AutoSliderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_auto_slider, viewGroup, false));
            default:
                return onCreateViewHolderDynamic(viewGroup, position);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        DMCategory item = mList.get(position);
        if (viewHolder instanceof AutoSliderViewHolder) {
            AutoSliderViewHolder holder = (AutoSliderViewHolder) viewHolder;
            holder.setData(item, position);
        } else if (viewHolder instanceof HorizontalCardScrollHolder) {
            HorizontalCardScrollHolder holder = (HorizontalCardScrollHolder) viewHolder;
            holder.setData(item, position);
        } else {
            onBindViewHolderDynamic(viewHolder, position);
        }
    }

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, DMCategory category, List<DMContent> childList);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AutoSliderViewHolder extends DMAutoSliderViewHolder {

        @Override
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, DMCategory category, List<DMContent> childList) {
            return getDynamicChildAdapter(itemType, category, childList);
        }

        AutoSliderViewHolder(View view) {
            super(view);
        }
    }


    public class CommonHolder extends DynamicCommonHolder {

        public CommonHolder(View view) {
            super(view);
        }

        @Override
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, DMCategory category, List<DMContent> childList) {
            return getDynamicChildAdapter(itemType, category, childList);
        }
    }

    private int mSlotWidth = 0;

    public class HorizontalCardScrollHolder extends DMHorizontalCardScrollHolder {

        public HorizontalCardScrollHolder(View view) {
            super(view);
        }

        @Override
        protected int getLayoutSlotWidth() {
            return mSlotWidth;
        }

        @Override
        protected void setLayoutSlotWidth(int layoutWidth) {
            mSlotWidth = layoutWidth;
        }

        @Override
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, DMCategory category, List<DMContent> childList) {
            return getDynamicChildAdapter(itemType, category, childList);
        }
    }
}