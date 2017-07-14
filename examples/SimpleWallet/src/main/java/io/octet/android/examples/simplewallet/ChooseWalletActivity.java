package io.octet.android.examples.simplewallet;


import org.ethereum.geth.Accounts;

import java.io.File;

import io.octet.android.octetsdk.provider.dialogs.WalletSelectionActivity;

public class ChooseWalletActivity extends WalletSelectionActivity {
    @Override
    protected Accounts getAccounts() {
        return ((WalletApplication)getApplication()).getAccountManager();
    }

    @Override
    protected File getKeyStoreLocation() {
        return ((WalletApplication)getApplication()).getKeystorePath();
    }
}
