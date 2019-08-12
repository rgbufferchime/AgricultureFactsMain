package com.bufferchime.agriculturefact.materialdesgin;

/**
 * Created by cloudfuze on 14/08/15.
 */
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;


/**
 * Created by Ravi on 01/06/15.
 */
public class ParseUtils {

    private static String TAG = ParseUtils.class.getSimpleName();

    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(AppConfig.PARSE_APPLICATION_ID) || TextUtils.isEmpty(AppConfig.PARSE_CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, "QU8G4Y9m9DRVRIe1aTadcxFn57UFtyrQ1xJtfrAl", "2tuxGelt2dFmlWhdezRaGmYvt1ru8JfxzkvVx6gj");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground(AppConfig.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to Parse!");

            }
        });
    }

    public static void subscribeWithEmail(String email) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("email", email);
        installation.saveInBackground();
    }
}
