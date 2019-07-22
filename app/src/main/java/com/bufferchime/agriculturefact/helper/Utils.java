package com.bufferchime.agriculturefact.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.bufferchime.agriculturefact.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Utils {
    public static AdRequest adRequest;
    public static InterstitialAd interstitial;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static AlertDialog alertDialog;
    public static boolean isNetworkAvailable(Activity activity) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadAd(Context context) {

        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(context.getString(R.string.admob_interstitial_id));
        adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
    }
    public static void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
            interstitial.show();
        }
    }


}
