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
import com.dynamic.listeners.DMFlingType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;

import java.util.List;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public abstract class BaseDynamicAdapter<T1, T2> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final DynamicCallback.OnClickListener<T1, T2> listener;
    protected final List<T1> mList;
    protected final String imageUrl;
    protected final Context context;
    @DMFlingType
    private final int flingType;

    public BaseDynamicAdapter(Context context, List<T1> mList, DynamicCallback.OnClickListener<T1, T2> listener) {
        this(context, mList, DMFlingType.None, listener);
    }
    public BaseDynamicAdapter(Context context, List<T1> mList, @DMFlingType int flingType, DynamicCallback.OnClickListener<T1, T2> listener) {
        this.context = context;
        this.listener = listener;
        this.mList = mList;
        this.flingType = flingType;
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) instanceof DMCategory ? ((DMCategory) mList.get(position)).getItemType() : 0;
    }

    public void resetFlags() {
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
                return new CommonHolder<>(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list, viewGroup, false));
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return new HorizontalCardScrollHolder<>(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list, viewGroup, false));
            case DMCategoryType.TYPE_LIST_CARD:
            case DMCategoryType.TYPE_GRID_CARD:
                return new CommonHolder<>(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_list_card, viewGroup, false));
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return new AutoSliderViewHolder<>(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_parent_slot_auto_slider, viewGroup, false));
            default:
                return onCreateViewHolderDynamic(viewGroup, position);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        T1 item = mList.get(position);
        if (viewHolder instanceof AutoSliderViewHolder) {
            AutoSliderViewHolder holder = (AutoSliderViewHolder) viewHolder;
            holder.setData(item, position);
        } else if (viewHolder instanceof HorizontalCardScrollHolder) {
            HorizontalCardScrollHolder holder = (HorizontalCardScrollHolder) viewHolder;
            holder.setData(item, position, flingType);
        } else {
            onBindViewHolderDynamic(viewHolder, position);
        }
    }

    protected abstract <T1, T2> RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, T1 category, List<T2> childList);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void stopAnimation() {
        DynamicModule.getInstance().getHandler().removeCallbacksAndMessages(null);
    }

    public class AutoSliderViewHolder<T1, T2>  extends DMAutoSliderViewHolder<T1, T2> {

        @Override
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, T1 category, List<T2> childList) {
            return getDynamicChildAdapter(itemType, category, childList);
        }

        AutoSliderViewHolder(View view) {
            super(view);
        }

        public void setData(T1 mItem, int pos) {
            super.setData(mItem, pos);
        }
    }


    public class CommonHolder<T1, T2>  extends DynamicCommonHolder<T1, T2> {

        public CommonHolder(View view) {
            super(view);
        }

        @Override
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, T1 category, List<T2> childList) {
            return getDynamicChildAdapter(itemType, category, childList);
        }
    }


    public class HorizontalCardScrollHolder<T1, T2>  extends DMHorizontalCardScrollHolder<T1, T2> {

        public HorizontalCardScrollHolder(View view) {
            super(view);
        }

        @Override
        protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, T1 category, List<T2> childList) {
            return getDynamicChildAdapter(itemType, category, childList);
        }
    }
}