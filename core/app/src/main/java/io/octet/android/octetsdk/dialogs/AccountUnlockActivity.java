package io.octet.android.octetsdk.dialogs;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.octet.android.octetsdk.R;
import io.octet.android.octetsdk.utils.Blockies;

public class AccountUnlockActivity extends Activity {
    private static final Blockies BLOCKIES = new Blockies();

    private String merchantName, merchantAddress;
    private String accountName, accountAddress;
    private Drawable merchantDrawable;
    private double transactionAmount;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        boolean useFingerprint = getIntent().getExtras().getBoolean("fingerprintUnlock", false);

        merchantName = getIntent().getExtras().getString(getString(R.string.MERCHANT_NAME));
        String merchantPackage = getIntent().getExtras().getString(getString(R.string.MERCHANT_PACKAGE));
        try {
            this.merchantDrawable = getBaseContext().getPackageManager().getApplicationIcon(merchantPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        this.transactionAmount = getIntent().getExtras().getDouble(getString(R.string.TRANSACTION_AMOUNT));
        this.merchantAddress = getIntent().getExtras().getString(getString(R.string.TRANSACTION_SENDTO));

        this.accountName = getIntent().getExtras().getString(getString(R.string.ACCOUNT_NAME));
        this.accountAddress = getIntent().getExtras().getString(getString(R.string.ACCOUNT_ADDRESS));

        if (!useFingerprint) {
            plainTextSetup();
        } else {
            fingerprintSetup();
        }
    }

    private void plainTextSetup() {
        setContentView(R.layout.plain_text_account_unlock);

        CircleImageView merchantImage = (CircleImageView) findViewById(R.id.merchant_image);
        TextView merchantLine = (TextView) findViewById(R.id.merchant_text);
        TextView merchantAddress = (TextView) findViewById(R.id.merchant_address);

        merchantImage.setImageDrawable(merchantDrawable);
        merchantLine.setText(merchantName + " would like to make a transaction.");
        merchantAddress.setText(this.merchantAddress);



        TextView ethAmount = (TextView) findViewById(R.id.eth_amount);
        ethAmount.setText(transactionAmount + " ETH");



        CircleImageView accountImage = (CircleImageView) findViewById(R.id.account_image);

        Bitmap accountBitmap = BLOCKIES.createIcon(accountAddress);
        accountImage.setImageBitmap(accountBitmap);



        TextView accountName = (TextView) findViewById(R.id.account_name);
        TextView accountAddress = (TextView) findViewById(R.id.account_address);

        accountName.setText(this.accountName);
        accountAddress.setText(this.accountAddress);


        final EditText password = (EditText) findViewById(R.id.password);

        Button authorize = (Button) findViewById(R.id.auth_btn);
        authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.ACCOUNT_PASSWORD), password.getText());

                setResult(1, intent);
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(-1);
                finish();
            }
        });
    }

    private void fingerprintSetup() {

    }
}
