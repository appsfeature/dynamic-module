package com.dynamic.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DMUtility {

    public static boolean isValidUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            return (url.startsWith("file://") || url.startsWith("http://") || url.startsWith("https://"));
        }else {
            return false;
        }
    }

    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        try {
            if (context != null && context.getSystemService(Context.CONNECTIVITY_SERVICE) != null && context.getSystemService(Context.CONNECTIVITY_SERVICE) instanceof ConnectivityManager) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Network network = connectivityManager.getActiveNetwork();
                    if (network != null) {
                        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);
                        isConnected = nc != null && (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || nc.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
                    }
                } else {
                    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                    isConnected = activeNetwork != null && activeNetwork.isConnected();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        if (html == null) {
            return new SpannableString("");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static String getSecurityCode(Context ctx) {
        String keyHash = null;
        try {
            Signature[] signatures;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                signatures = info.signingInfo.getSigningCertificateHistory();
            } else {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
                signatures = info.signatures;
            }
            for (Signature signature : signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
//        Log.e("printHashKey", "keyHash : " + keyHash);
        return keyHash;
    }
}
