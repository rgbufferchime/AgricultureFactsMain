package com.bufferchime.agriculturefact.materialdesgin;

/**
 * Created by cloudfuze on 14/08/15.
 */
import android.support.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // register with parse
        ParseUtils.registerParse(this);


    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}