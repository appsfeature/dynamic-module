package com.dynamic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.R;
import com.dynamic.adapter.DynamicAdapter;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMConstants;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;


public class DynamicListFragment extends BaseDynamicListFragment implements DynamicCallback.OnDynamicListListener{

    protected DynamicCallback.OnDynamicListListener mClickListener;

    public static DynamicListFragment getInstance(int catId) {
        DynamicListFragment fragment = new DynamicListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DMConstants.CAT_ID, catId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DynamicCallback.OnDynamicListListener) {
            mClickListener = (DynamicCallback.OnDynamicListListener) context;
        }
    }

    @Override
    public int getLayoutContentView() {
        return R.layout.dm_fragment_dynamic_list;
    }

    @Override
    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return new DynamicAdapter(activity, mList, new Response.OnClickListener<DMContent>() {
            @Override
            public void onItemClicked(View view, DMContent item) {
                if(BaseUtil.isValidUrl(item.getLink())) {
                    if (mClickListener != null) {
                        mClickListener.onItemClicked(view, item);
                    }
                }else {
                    BaseUtil.showToast(activity, "Invalid Link!");
                }
            }
        });
    }

    @Override
    public void onInitDataFromArguments(Bundle bundle) {

    }

    @Override
    public void onInitViews(View view) {

    }

    @Override
    public void onUpdateUi() {

    }

    @Override
    public boolean onResumeReloadList() {
        return false;
    }

    @Override
    public void onItemClicked(View view, DMContent item) {
        Toast.makeText(activity, "onItemClicked", Toast.LENGTH_SHORT).show();
    }
}
