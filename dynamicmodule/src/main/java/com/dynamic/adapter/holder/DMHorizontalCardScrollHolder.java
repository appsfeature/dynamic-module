package com.dynamic.adapter.holder;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.adapter.holder.base.DynamicCommonHolder;
import com.dynamic.model.DMCategory;

public abstract class DMHorizontalCardScrollHolder extends DynamicCommonHolder {

    private boolean isScrollStateIdle = true;
    private boolean isStateChange = false;

    public DMHorizontalCardScrollHolder(View view) {
        super(view);
    }

    protected abstract int getLayoutSlotWidth();

    protected abstract void setLayoutSlotWidth(int layoutWidth);

    @Override
    public void setData(DMCategory item, int position) {
        super.setData(item, position);
        try {
            if(isEnableAutoScroll){
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(mRunnable, mScrollSpeed);
                if (recyclerView != null) {
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            switch (newState) {
                                case RecyclerView.SCROLL_STATE_IDLE:
                                    isScrollStateIdle = true;
                                    if(isEnableAutoScroll) {
                                        mHandler.removeCallbacksAndMessages(null);
                                        mHandler.postDelayed(mRunnable, mScrollSpeed);
                                    }
                                    break;
                                case RecyclerView.SCROLL_STATE_DRAGGING:
                                    isStateChange = true;
                                    isScrollStateIdle = false;
                                    break;
                                case RecyclerView.SCROLL_STATE_SETTLING:
                                    break;
                            }
                        }
                    });
                }
            }else {
                mHandler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (isScrollStateIdle) {
                    int lastPos = -2;
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        lastPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    }
                    if (lastPos < mListSize - 1) {
                        smoothScrollRecyclerView();
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.postDelayed(this, mScrollSpeed);
                    } else {
                        recyclerView.smoothScrollToPosition(0);
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.postDelayed(this, mScrollSpeed);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void smoothScrollRecyclerView() {
        try {
            if (recyclerView != null) {
                if (isStateChange) {
                    isStateChange = false;
                    int currentPos = -1;
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        currentPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    }
                    if (currentPos >= 0) {
                        RecyclerView.ViewHolder currViewHolder = recyclerView.findViewHolderForLayoutPosition(currentPos);
                        if (currViewHolder != null) {
                            recyclerView.smoothScrollBy((int) currViewHolder.itemView.getX(), 0);
                        }
                    }
                } else {
                    if(getLayoutSlotWidth() == 0){
                        setLayoutSlotWidth(getSlotWidth());
                    }
                    recyclerView.smoothScrollBy(getLayoutSlotWidth(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSlotWidth() {
        try {
            int currentPos = -1;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                currentPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
            if (currentPos >= 0) {
                RecyclerView.ViewHolder currViewHolder = recyclerView.findViewHolderForLayoutPosition(currentPos);
                if (currViewHolder != null) {
                    return currViewHolder.itemView.getMeasuredWidth();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
