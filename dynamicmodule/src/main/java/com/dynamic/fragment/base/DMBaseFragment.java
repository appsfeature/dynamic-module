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

public abstract class DMBaseFragment extends Fragment {
    protected int catId;
    protected DMDataManager dataManager;
    protected Activity activity;
    protected DMProperty property;

    public abstract void onInitDataFromArguments(Bundle bundle, Serializable extraProperty);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        dataManager = DMDataManager.get(activity);
        initDataFromArguments();
    }

    private void initDataFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(DMConstants.CATEGORY_PROPERTY) instanceof DMProperty) {
            property = (DMProperty) bundle.getSerializable(DMConstants.CATEGORY_PROPERTY);
            catId = property.getCatId();
            dataManager.setDisableCaching(property.isDisableCaching());
            onInitDataFromArguments(bundle, bundle.getSerializable(DMConstants.CATEGORY_PROPERTY));
        } else {
            DMUtility.showPropertyError(activity);
        }
    }
}
