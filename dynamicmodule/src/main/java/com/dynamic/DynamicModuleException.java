package com.dynamic;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class DynamicModuleException extends Exception {

    public DynamicModuleException(String message) {
        super(message);
    }

    public DynamicModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicModuleException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public DynamicModuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}