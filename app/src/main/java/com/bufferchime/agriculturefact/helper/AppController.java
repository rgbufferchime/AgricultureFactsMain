package com.bufferchime.agriculturefact.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bufferchime.agriculturefact.OlderPosts;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.phverify1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;


public class AppController extends Application {





	//private static final String TAG = Global.class.getSimpleName();

	public FirebaseAuth firebaseAuth;
	private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
	public static Typeface canaroExtraBold;

	public boolean validUser;



	public FirebaseAuth.AuthStateListener mAuthListener;

	public FirebaseAuth getFirebaseAuth() {
		return firebaseAuth = FirebaseAuth.getInstance();
	}

	public String getFirebaseUserAuthenticateId() {
		String userId = null;
		if (firebaseAuth.getCurrentUser() != null) {
			userId = firebaseAuth.getCurrentUser().getUid();
		}
		return userId;
	}

	public void checkUserLogin(final Context context) {
		if (firebaseAuth.getCurrentUser() != null) {
			Intent profileIntent = new Intent(context, OlderPosts.class);
			context.startActivity(profileIntent);
		}
	}

	public void isUserCurrentlyLogin(final Context context) {
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				boolean validUser = false;
				if (null != user) {
					try {
						user.reload();
						if (null != user) {
							validUser = true;
						}
					} catch (Exception e) {
						//} catch (FirebaseAuthInvalidUserException e) {
						validUser = false;
					}


					if (validUser==true) {
						Intent profileIntent = new Intent(context, OlderPosts.class);
						context.startActivity(profileIntent);
					}
				} else {
					Intent loginIntent = new Intent(context, phverify1.class);
					context.startActivity(loginIntent);
				}
			}
		};
	}












	public static final String TAG = AppController.class
			.getSimpleName();

	private static Context mContext;
	private static String mAppUrl;
	public static MediaPlayer player;
	public static Activity currentActivity;

	private RequestQueue mRequestQueue;

	private static AppController mInstance;
	private com.android.volley.toolbox.ImageLoader mImageLoader;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		setContext(getApplicationContext());
		mAppUrl = StaticUtils.PLAYSTORE_URL + mContext.getPackageName();
		setTelephoneListener();
		player = new MediaPlayer();
		mediaPlayerInitializer();
		//AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	public static void mediaPlayerInitializer(){
		try {
			player = MediaPlayer.create(getAppContext(), R.raw.snd_bg);
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setLooping(true);
			player.setVolume(1f, 1f);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public static String getAppUrl() {
		return mAppUrl;
	}
	private static void setContext(Context context) {
		mContext = context;
	}
	public static Context getAppContext() {
		return mContext;
	}


	public static void playSound()
	{
		try {
			if (SettingsPreferences.getMusicEnableDisable(mContext)&&!player.isPlaying()) {
				player.start();
			}else{
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
			mediaPlayerInitializer();
			player.start();
		}
	}
	public static void StopSound() {
		if (player.isPlaying()) {
			player.pause();
		}

	}

	private void setTelephoneListener() {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                	StopSound();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                	StopSound();
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager telephoneManager = (TelephonyManager) getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephoneManager != null) {
            telephoneManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }



	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}
	public com.android.volley.toolbox.ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new BitmapCache());
		}
		return this.mImageLoader;
	}
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	static
	{
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}
}
