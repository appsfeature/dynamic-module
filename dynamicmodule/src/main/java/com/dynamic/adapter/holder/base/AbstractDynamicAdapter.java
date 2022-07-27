package com.dynamic.adapter.holder.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.util.List;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public abstract class AbstractDynamicAdapter<T1,T2> extends RecyclerView.ViewHolder{

    public AbstractDynamicAdapter(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, T1 category, List<T2> childList);

}
