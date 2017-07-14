package io.octet.android.examples.simplemerchant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.ethereum.geth.Geth;
import org.ethereum.geth.Transaction;

import io.octet.android.octetsdk.merchant.EthereumActivity;
import io.octet.android.octetsdk.merchant.EthereumTask;

public class MainActivity extends EthereumActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button btn = (Button) findViewById(R.id.test_button);
        btn.setEnabled(false);
        requestLink(new EthereumTask<EthereumActivity>() {
            @Override
            public void onComplete(EthereumActivity param) {
                btn.setEnabled(true);
            }

            @Override
            public void onFailed(String reason, Throwable cause) {
                System.err.println(reason);
                cause.printStackTrace();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendTransaction(
                            1.0,
                            Geth.newAddressFromHex("0x2f68c304F358192e0148d2FE09eE71146ACc8258"),
                            new EthereumTask<Transaction>() {
                        @Override
                        public void onComplete(Transaction param) {

                        }

                        @Override
                        public void onFailed(String reason, Throwable cause) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
