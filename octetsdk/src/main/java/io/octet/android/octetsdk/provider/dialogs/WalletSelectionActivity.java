package io.octet.android.octetsdk.provider.dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.ethereum.geth.Account;
import org.ethereum.geth.Accounts;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.octet.android.octetsdk.OctetActivity;
import io.octet.android.octetsdk.R;
import io.octet.android.octetsdk.provider.LinkInfo;

public abstract class WalletSelectionActivity extends OctetActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.wallet_selection);

        if (!hasOctetInfo()) {
            finish();
            return;
        }

        CircleImageView merchantImage = (CircleImageView) findViewById(R.id.merchant_image);
        TextView merchantLine = (TextView) findViewById(R.id.merchant_text);
        TextView merchantAddress = (TextView) findViewById(R.id.merchant_address);

        merchantImage.setImageDrawable(getMerchantDrawable());
        merchantLine.setText(getMerchantName() + " would like to make a transaction.");
        merchantAddress.setText(getMerchantAddress());

        ListView view = (ListView) findViewById(R.id.wallet_list);

        setupListView(view);
    }

    protected void setupListView(ListView view) {
        AccountListViewAdapter adapter = new AccountListViewAdapter(this, getAccounts());
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinkInfo result = new LinkInfo(getKeyStoreLocation().getAbsolutePath(), position);

                Intent resultIntent = new Intent();

                result.putIn(resultIntent);

                setResult(1, resultIntent);

                finish();
            }
        });
    }

    protected abstract Accounts getAccounts();

    protected abstract File getKeyStoreLocation();
}
