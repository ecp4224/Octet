package io.octet.android.octetsdk;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

/**
 * Base class that automatically gets merchant info from the
 * passing intent. If no extra data is passed, all getters in this method will
 * return null and {@link OctetActivity#hasOctetInfo} will return false
 */
public class OctetActivity extends Activity {

    private boolean hasOctetInfo;
    private String merchantName, merchantAddress;
    private Drawable merchantDrawable;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (getIntent() == null || getIntent().getExtras() == null)
            return;

        merchantName = getIntent().getExtras().getString(getString(R.string.MERCHANT_NAME));
        String merchantPackage = getIntent().getExtras().getString(getString(R.string.MERCHANT_PACKAGE));
        try {
            this.merchantDrawable = getBaseContext().getPackageManager().getApplicationIcon(merchantPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        this.merchantAddress = getIntent().getExtras().getString(getString(R.string.TRANSACTION_SENDTO));

        hasOctetInfo = true;
    }

    public final String getMerchantName() {
        return merchantName;
    }

    public final String getMerchantAddress() {
        return merchantAddress;
    }

    public final Drawable getMerchantDrawable() {
        return merchantDrawable;
    }

    public final boolean hasOctetInfo() {
        return hasOctetInfo;
    }
}
