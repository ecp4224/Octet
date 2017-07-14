package io.octet.android.examples.simplemerchant;


import android.app.Application;
import android.content.Intent;

import io.octet.android.octetsdk.merchant.EthereumActivity;

public class MerchantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //startActivity(new Intent(this, MainActivity.class));
    }
}
