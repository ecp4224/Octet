package io.octet.android.examples.simplewallet;


import android.app.Application;
import android.content.Intent;

import org.ethereum.geth.Accounts;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Node;
import org.ethereum.geth.NodeConfig;

import java.io.File;

public class WalletApplication extends Application {
    private KeyStore keyStore;
    private File keystorePath;

    @Override
    public void onCreate() {
        super.onCreate();

        keystorePath = new File(getFilesDir(), ".ethereum");
        try {
            Node node = Geth.newNode(keystorePath.getAbsolutePath(), new NodeConfig());
            node.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        keyStore = Geth.newKeyStore(keystorePath.getAbsolutePath(),
                Geth.LightScryptN, Geth.LightScryptP);

        Accounts accountManager = keyStore.getAccounts();

        if (accountManager.size() == 0) {
            try {
                keyStore.newAccount("edkek 1");
                keyStore.newAccount("edkek 2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        startActivity(new Intent(this, MainActivity.class));
    }

    public Accounts getAccountManager() {
        return keyStore.getAccounts();
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public File getKeystorePath() {
        return keystorePath;
    }
}
