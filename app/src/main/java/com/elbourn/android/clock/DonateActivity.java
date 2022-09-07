package com.elbourn.android.clock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Random;

public class DonateActivity extends OptionsMenu {

    static String TAG = "DonateActivity";
    boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "start onCreate");
        Random r = new Random();
        int i = r.nextInt(100);
        Log.i(TAG, "i: " + i);
        if (i < 10 || testing) {
            setContentView(R.layout.activity_donate);
            setupButtons();
        } else {
            startApplication();
        }

        Log.i(TAG, "end onCreate");
    }

    void setupButtons() {
        Button donateButton = findViewById(R.id.donateButton);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "donateButton clicked");
                startDonationWebsite();
                Log.i(TAG, "donateButton ended");
            }
        });

        ImageButton continueButton = findViewById(R.id.donateContinueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "continueButton clicked");
                startApplication();
            }
        });

    }

    void startApplication() {
        Context context = getApplicationContext();
        runOnUiThread(new Runnable() {
            public void run() {
                String msg = "Starting application...";
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(context, MainActivity.class));
    }

//    void grantEntitlement(String licenseType) {
//        Log.i(TAG, "start grantEntitlement");
////        TextView subscriptionsStatus = findViewById(R.id.subscriptionStatus);
////        subscriptionsStatus.setText("Subscription status: " + licenseType);
//        Log.i(TAG, "licenseType: " + licenseType);
//        Context context = getApplicationContext();
//        runOnUiThread(new Runnable() {
//            public void run() {
//                String msg = licenseType + " granted.\nStarting application...";
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//            }
//        });
//        startActivity(new Intent(context, MainActivity.class));
//    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}