package com.dynamic.network;


import static com.dynamic.network.ResponseStatusCode.BAD_GATEWAY;
import static com.dynamic.network.ResponseStatusCode.GATEWAY_TIMEOUT;
import static com.dynamic.network.ResponseStatusCode.INTERNAL_SERVER_ERROR;
import static com.dynamic.network.ResponseStatusCode.NOT_FOUND;
import static com.dynamic.network.ResponseStatusCode.REQUEST_TIMEOUT;
import static com.dynamic.network.ResponseStatusCode.SERVICE_UNAVAILABLE;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Abhijit Rao on 5/22/2017.
 */
public class ResponseCallBack<T> implements Callback<T> {

    private final NetworkCallback.Response<T> onNetworkCall;

    public ResponseCallBack(NetworkCallback.Response<T> onNetworkCall) {
        this.onNetworkCall = onNetworkCall;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (onNetworkCall == null) {
            return;
        }
        int responseCode = response.code();
        if (responseCode == ResponseStatusCode.SUCCESS) {
            if (response.body() != null) {
                if(response.body() instanceof NetworkModel){
                    NetworkModel baseModel = (NetworkModel) response.body();
                    boolean status = !TextUtils.isEmpty(baseModel.getStatus())
                            && baseModel.getStatus().equals("success");
                    onNetworkCall.onComplete(status, response.body());
                }else {
                    onNetworkCall.onComplete(true, response.body());
                }
                onNetworkCall.onResponse(call, response);
            } else {
                notifyCallback(call, responseCode, new Throwable("Invalid response body"));
            }
        } else if (responseCode == INTERNAL_SERVER_ERROR || responseCode == NOT_FOUND
                || responseCode == BAD_GATEWAY || responseCode == SERVICE_UNAVAILABLE
                || responseCode == GATEWAY_TIMEOUT || responseCode == REQUEST_TIMEOUT) {
            if(responseCode == REQUEST_TIMEOUT){
                notifyCallback(call, responseCode, new SocketTimeoutException());
            }else {
                notifyCallback(call, responseCode, new Throwable("Invalid response code : " + responseCode));
            }
        } else {
            notifyCallback(call, responseCode, new Throwable("Invalid response code"));
        }
        if(!isRequestCompletedCalled) {
            isRequestCompletedCalled = true;
            onNetworkCall.onRequestCompleted();
        }
    }

    private boolean isRequestCompletedCalled = false;

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        notifyCallback(call, ResponseStatusCode.BAD_REQUEST, t);
        if(!isRequestCompletedCalled) {
            isRequestCompletedCalled = true;
            onNetworkCall.onRequestCompleted();
        }
    }

    private void notifyCallback(Call<T> call, int responseCode, Throwable t) {
        onNetworkCall.onComplete(false, null);
        onNetworkCall.onResponse(call, null);
        onNetworkCall.onFailure(call, new Exception(t));

        NetworkCallback.Retry retryCallback = new NetworkCallback.Retry() {
            @Override
            public void onRetry() {
                try {
                    if (call != null) {
                        call.clone().enqueue(ResponseCallBack.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (t instanceof SocketTimeoutException || t instanceof IOException) {
            // SocketTimeoutException: "Connection Timeout" , IOException: "Timeout";
            onNetworkCall.onRetry(retryCallback, new Exception(t));
        }
        //Trigger on all type of error occurs.
        onNetworkCall.onError(responseCode, new Exception(t));
    }

}
