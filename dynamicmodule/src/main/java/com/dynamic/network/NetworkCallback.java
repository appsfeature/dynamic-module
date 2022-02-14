package com.dynamic.network;

import retrofit2.Call;

public interface NetworkCallback {

    interface Retry{
        void onRetry();
    }

    interface Response<T> {
        void onComplete(boolean status, T response);

        default void onResponse(Call<T> call, final retrofit2.Response<T> response) {
        }

        void onFailure(Call<T> call, Exception e);

        default void onRetry(Retry retryCallback, Exception e) {
        }

        default void onError(int responseCode, Exception e) {
        }
    }
}
