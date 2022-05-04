package com.dynamic.listeners;

import android.app.Activity;
import android.view.View;

import androidx.annotation.MainThread;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;

public interface DynamicCallback {

    interface OnDynamicListListener {
        void onItemClicked(Activity activity, View view, DMContent item);
    }

    interface OnDynamicPagerListener {
        void onItemClicked(Activity activity, View view, DMContent item);
    }

    interface Listener<T> {
        void onSuccess(T response);

        @MainThread
        default void onValidate(T list, Response.Status<T> callback) {
            callback.onSuccess(list);
        }

        void onFailure(Exception e);

        default void onRequestCompleted() {
        }
    }
}
