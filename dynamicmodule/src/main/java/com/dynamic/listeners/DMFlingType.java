package com.dynamic.listeners;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        DMFlingType.None,
        DMFlingType.LinearSnapHelper,
        DMFlingType.PagerSnapHelper,
})
@Retention(RetentionPolicy.SOURCE)
public @interface DMFlingType {
    int None = 0;
    int LinearSnapHelper = 1;
    int PagerSnapHelper = 2;
}