package com.elbourn.android.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class SubscriptionsActivity extends AppCompatActivity {

    static String TAG = "SubscriptionsActivity";
    BillingClient billingClient;
    String mySubscription = "to be set on play console";
    Boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "start onCreate");
        setContentView(R.layout.activity_subscriptions);
        checkPurchase();
        Log.i(TAG, "end onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "start onResume");
        setContentView(R.layout.activity_subscriptions);
        Log.i(TAG, "end onResume");
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

    void checkPurchase() {
        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {
                    for (Purchase purchase : purchases) {
                        handlePurchase(billingClient, purchase);
                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling or failure in the purchase flow.
                    Context context = getApplicationContext();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "Error connecting to billing system. Try later.";
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }
                    });
                    finishAffinity();
                }
            }
        };
        Context context = getApplicationContext();
        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    checkSubscription(billingClient);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Context context = getApplicationContext();
                runOnUiThread(new Runnable() {
                    public void run() {
                        String msg = "Error connecting to billing system. Try later.";
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                });
                finishAffinity();
            }
        });
    }

    void handlePurchase(BillingClient billingClient, Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.
        // Verify the purchase.
        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {

            }
        };
        // Acknowledge purchase
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
        // Consume Response
        ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                    checkSubscription(billingClient);
                }
            }
        };
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        billingClient.consumeAsync(consumeParams, consumeResponseListener);
    }

    void checkSubscription(BillingClient billingClient) {
        Log.i(TAG, "start checkSubscription");
        Context context = getApplicationContext();
        runOnUiThread(new Runnable() {
            public void run() {
                String msg = "Checking subscription ... please wait";
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
        QueryPurchasesParams queryPurchaseParams = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build();
        billingClient.queryPurchasesAsync(queryPurchaseParams, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(BillingResult billingResult, List purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {
                    Boolean subscriptionVaild = false;
                    for (Object purchase : purchases) {
                        if (purchase == mySubscription)
                            subscriptionVaild = true;
                        break;
                    }
                    if (subscriptionVaild) {
                        grantEntitlement("Subscription");
                    }
                    if (testing) {
                        grantEntitlement("Test subscription");
                    }
                }
            }
        });
        Log.i(TAG, "end checkSubscription");
    }

    void grantEntitlement(String licenseType) {
        Log.i(TAG, "start grantEntitlement");
        Context context = getApplicationContext();
        runOnUiThread(new Runnable() {
            public void run() {
                String msg = licenseType + " granted.\nStarting application...";
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
        Intent mA = new Intent(context, MainActivity.class);
        startActivity(mA);
        Log.i(TAG, "end grantEntitlement");
    }
}