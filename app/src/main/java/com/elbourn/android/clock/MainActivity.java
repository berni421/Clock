package com.elbourn.android.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        runOnUiThread(new Runnable() {
            public void run() {
                    String msg = "Loading clock .. please wait.";
                    Log.i(TAG, "msg: " + msg);
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

            }
        });

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("file:///android_asset/website_clock/index.html");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "start onResume");
        myWebView.loadUrl("file:///android_asset/website_clock/index.html");
        Log.i(TAG, "end onResume");
    }


}