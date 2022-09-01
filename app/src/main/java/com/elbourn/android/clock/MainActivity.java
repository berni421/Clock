package com.elbourn.android.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
            finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.manageYourSubscriptions:
                startSubscriptionWebsite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSubscriptionWebsite() {
        Log.i(TAG, "start startSubscriptionWebsite");
        Context context = getApplicationContext();
        runOnUiThread(new Runnable() {
            public void run() {
                String msg = "Starting browser to access billing system...";
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
        String url = "https://play.google.com/store/account/subscriptions";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        Log.i(TAG, "end startSubscriptionWebsite");
    }

}