package com.bufferchime.agriculturefact.game.quiz.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bufferchime.agriculturefact.game.quiz.Constant;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.game.quiz.helper.AppController;
import com.bufferchime.agriculturefact.game.quiz.helper.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrivacyPolicy extends AppCompatActivity {


    public ProgressBar prgLoading;
    public WebView mWebView;
    public String activity;
    public ImageView back,setting;
    public TextView tvTitle;


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        activity = getIntent().getStringExtra("activity");
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        mWebView = (WebView) findViewById(R.id.webView1);
        setting=(ImageView)findViewById(R.id.setting) ;
        back = (ImageView) findViewById(R.id.back);
        tvTitle=(TextView)findViewById(R.id.tvLevel);
        tvTitle.setText(getString(R.string.privacy_policy));
        setting.setVisibility(View.GONE);
        try {
            if (Utils.isNetworkAvailable(this)) {
                mWebView.setClickable(true);
                mWebView.setFocusableInTouchMode(true);
                mWebView.getSettings().setJavaScriptEnabled(true);

                GetPrivacyAndTerms();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void GetPrivacyAndTerms() {
        if (!prgLoading.isShown()) {
            prgLoading.setVisibility(View.VISIBLE);
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.QUIZ_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("error").equals("false")) {


                        String privacyStr = obj.getString("data");
                        mWebView.setVerticalScrollBarEnabled(true);
                        mWebView.loadDataWithBaseURL("", privacyStr, "text/html", "UTF-8", "");
                        mWebView.setBackgroundColor(getResources().getColor(R.color.white));

                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    prgLoading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                prgLoading.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.accessKey, Constant.accessKeyValue);
                params.put(Constant.getPrivacy, "1");
                return params;

            }
        };

        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(strReq);

        // }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();

    }
}
