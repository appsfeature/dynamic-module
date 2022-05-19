package com.dynamic.adapter.holder.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.util.List;

public abstract class AbstractDynamicAdapter extends RecyclerView.ViewHolder{

    public AbstractDynamicAdapter(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, DMCategory category, List<DMContent> childList);

}
