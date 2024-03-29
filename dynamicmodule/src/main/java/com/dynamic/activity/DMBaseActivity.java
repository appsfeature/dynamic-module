package com.dynamic.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dynamic.util.DMConstants;
import com.dynamic.util.DMProperty;
import com.dynamic.util.DMUtility;

import java.io.Serializable;

public abstract class DMBaseActivity extends AppCompatActivity {

    protected String title;
    protected DMProperty property;

    public abstract void onInitDataFromArguments(Bundle bundle, Serializable extraProperty);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getSerializable(DMConstants.CATEGORY_PROPERTY) instanceof DMProperty) {
            property = (DMProperty) bundle.getSerializable(DMConstants.CATEGORY_PROPERTY);
            title = property.getTitle();
            onInitDataFromArguments(bundle, bundle.getSerializable(DMConstants.CATEGORY_PROPERTY));
        } else {
            DMUtility.showPropertyError(this);
        }
    }
}
