package io.octet.android.octetsdk.merchant;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.EthereumClient;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Node;
import org.ethereum.geth.NodeConfig;
import org.ethereum.geth.Transaction;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

import io.octet.android.octetsdk.R;
import io.octet.android.octetsdk.dialogs.AccountUnlockActivity;
import io.octet.android.octetsdk.provider.LinkInfo;

public final class EthereumLink extends Activity {
    public static final int ACTION_REQUEST_CODE = 1;
    public static final int ACTION_UNLOCK_CODE = 2;

    public static final String ACTION_REQUEST = "io.octet.android.octetsdk.LINK_REQUEST";

    private final SecureRandom RANDOM = new SecureRandom();
    private int id;
    private EthereumClient gethClient;
    private Address linkedAccount;

    private String importedAccountPassword, keystoreLocation;

    private EthereumTask<EthereumLink> bindListener;


    private Context appContext;
    private String appName;
    private String packageName;

    private EthereumLink(Context context) {
        this.appContext = context;

        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;

        this.appName = stringId == 0 ?
                applicationInfo.nonLocalizedLabel.toString() :
                context.getString(stringId);

        this.packageName = context.getPackageName();
    }

    /**
     * Request a link to the Ethereum network with any app/service providing a link. The
     * link may not be established after this function call is complete
     * @return A new {@link EthereumLink}.
     */
    public static EthereumLink requestLink(Context context) {
        EthereumLink link = new EthereumLink(context);
        link.bindRequest();

        return link;
    }

    /**
     * Request a link to the Ethereum network with any app/service providing a link. The
     * link may not be established after this function call is complete
     * @return A new {@link EthereumLink}.
     */
    public static EthereumLink requestLink(Context context, EthereumTask<EthereumLink> callback) {
        EthereumLink link = new EthereumLink(context);
        link.bindListener = callback;
        link.bindRequest();

        return link;
    }

    private void bindRequest() {
        this.id = RANDOM.nextInt();
        Intent linkIntent = new Intent(ACTION_REQUEST);
        startActivityForResult(linkIntent, ACTION_REQUEST_CODE);
    }

    private EthereumLink() { }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        switch (requestCode) {

            case ACTION_REQUEST_CODE:
                if (resultCode == RESULT_CANCELED) {
                    bindListener.onFailed("The link was canceled by the user", null);
                    return; //The result was canceled
                }

                if (!data.hasExtra(LinkInfo.EXTRA_ID)) {
                    bindListener.onFailed("No wallet info was provided", null);
                    return; //The result doesn't contain any info
                }

                LinkInfo info = (LinkInfo) data.getExtras().getSerializable(LinkInfo.EXTRA_ID);

                if (info == null) {
                    bindListener.onFailed("No wallet info was provided", null);
                    return; //The result contains a null value
                }


                if (info.hasKeystoreAccess()) {
                    File file = info.getFile();

                    try {
                        Node node = Geth.newNode(file.getAbsolutePath(), new NodeConfig());
                        KeyStore accountManager = Geth.newKeyStore(
                                file.getAbsolutePath(), Geth.LightScryptN, Geth.LightScryptP
                        );

                        this.linkedAccount = accountManager
                                .getAccounts()
                                .get(info.getAccountIndex())
                                .getAddress();

                        node.start();

                        gethClient = node.getEthereumClient();
                    } catch (Exception e) {
                        bindListener.onFailed("Failed to load keystore from wallet", e);
                        return;
                    }
                } else {
                    keystoreLocation = randomString();
                    importedAccountPassword = randomString();

                    File file = new File(getFilesDir(), keystoreLocation);
                    try {
                        Node node = Geth.newNode(file.getAbsolutePath(), new NodeConfig());
                        KeyStore accountManager = Geth.newKeyStore(
                                file.getAbsolutePath(), Geth.LightScryptN, Geth.LightScryptP
                        );
                        Account linkedAccount = accountManager.importKey(
                                info.getExportedAccount(),
                                info.getExportedPassword(),
                                importedAccountPassword
                        );

                        this.linkedAccount = linkedAccount.getAddress();

                        node.start();

                        gethClient = node.getEthereumClient();
                    } catch (Exception e) {
                        bindListener.onFailed("Failed to import account into keystore", e);
                        return;
                    }
                }

                bindListener.onComplete(this);
                break;

            case ACTION_UNLOCK_CODE:
                //TODO Check resultCode for success
                //TODO Implement actual transaction using Geth.newTransaction
                //TODO Call currentTransaction.onComplete(Transaction)

                currentTransaction.onFailed("Not Implemented", new UnsupportedOperationException());
                break;
        }
    }

    private EthereumTask<Transaction> currentTransaction;
    public void sendTransaction(double amount, Address sendTo, EthereumTask<Transaction> callback)
            throws Exception {
        Intent intent = new Intent(appContext, AccountUnlockActivity.class);
        intent.putExtra("fingerprintUnlock", false); //TODO Make this an option
        intent.putExtra(getString(R.string.TRANSACTION_AMOUNT), amount);
        intent.putExtra(getString(R.string.TRANSACTION_SENDTO), sendTo.getHex());
        intent.putExtra(getString(R.string.ACCOUNT_NAME), "Main Account"); //TODO Get account info from LinkInfo
        intent.putExtra(getString(R.string.ACCOUNT_ADDRESS), linkedAccount.getHex());
        passAppExtras(intent);

        currentTransaction = callback;

        startActivityForResult(intent, RANDOM.nextInt());
    }

    private String randomString() {
        return new BigInteger(130, RANDOM).toString(32);
    }

    private Intent passAppExtras(Intent intent) {
        intent.putExtra(getString(R.string.MERCHANT_NAME), appName);
        intent.putExtra(getString(R.string.MERCHANT_PACKAGE), packageName);

        return intent;
    }
}
