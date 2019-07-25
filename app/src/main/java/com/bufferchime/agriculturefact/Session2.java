package com.bufferchime.agriculturefact;

import android.content.Context;
import android.content.SharedPreferences;

public class Session2 {
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "snow-intro-slider";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public Session2(Context context) {
        this._context = context;
        pref2 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor2 = pref2.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor2.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor2.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref2.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}