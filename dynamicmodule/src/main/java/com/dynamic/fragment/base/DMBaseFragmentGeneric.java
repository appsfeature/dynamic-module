package com.dynamic.fragment.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dynamic.util.DMConstants;
import com.dynamic.util.DMDataManager;
import com.dynamic.util.DMProperty;
import com.dynamic.util.DMUtility;

import java.io.Serializable;

public abstract class DMBaseFragmentGeneric<T> extends Fragment {
    protected Activity activity;
    protected T property;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        initDataFromArguments();
    }

    private void initDataFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(DMConstants.CATEGORY_PROPERTY) instanceof DMProperty) {
            property = (T) bundle.getSerializable(DMConstants.CATEGORY_PROPERTY);
        } else {
            DMUtility.showPropertyError(activity);
        }
    }
}
