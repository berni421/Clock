package com.elbourn.android.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.function.IntToDoubleFunction;

public class OptionsMenu extends AppCompatActivity {

    private String TAG = "OptionsMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        Context context = getApplicationContext();
        MenuItem introCheckBox = menu.findItem(R.id.subscriptionsIntroOff);
        introCheckBox.setChecked(IntroActivity.getIntroCheckBox(context));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.subscriptionsManage:
                startSubscriptionWebsite();
                return true;
            case R.id.subscriptionsIntroOff:
                Boolean subscriptionsIntroOff = !item.isChecked();
                item.setChecked(subscriptionsIntroOff);
                Log.i(TAG, "subscriptionsIntroOff: " + subscriptionsIntroOff);
                Context context = getApplicationContext();
                IntroActivity.setIntroCheckBox(context, subscriptionsIntroOff);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void startSubscriptionWebsite() {
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