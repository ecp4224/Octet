package io.octet.android.octetsdk.merchant;


import android.app.Activity;
import android.content.Intent;

import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.EthereumClient;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Node;
import org.ethereum.geth.NodeConfig;

import java.io.File;
import java.security.SecureRandom;

import io.octet.android.octetsdk.provider.LinkInfo;

public final class EthereumLink extends Activity {
    public static final String ACTION_REQUEST = "org.ethereum.LINK_REQUEST";

    private final SecureRandom RANDOM = new SecureRandom();
    private int id;
    private EthereumClient gethClient;
    private Address linkedAccount;

    /**
     * Request a link to the Ethereum network with any app/service providing a link. The
     * link may not be established after this function call is complete
     * @return A new {@link EthereumLink}.
     */
    public static EthereumLink requestLink() {
        EthereumLink link = new EthereumLink();
        link.bindRequest();

        return link;
    }

    private void bindRequest() {
        this.id = RANDOM.nextInt();
        Intent linkIntent = new Intent(ACTION_REQUEST);
        startActivityForResult(linkIntent, this.id);
    }

    private EthereumLink() { }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        if (requestCode != id)
            return; //This isn't our result

        if (resultCode == RESULT_CANCELED)
            return; //The result was canceled

        if (!data.hasExtra(LinkInfo.EXTRA_ID))
            return; //The result doesn't contain any info

        LinkInfo info = (LinkInfo) data.getExtras().getSerializable(LinkInfo.EXTRA_ID);

        if (info == null)
            return; //The result contains a null value


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
                e.printStackTrace();
            }
        } else {
            File file = new File(getFilesDir(), ".octet");
            try {
                Node node = Geth.newNode(file.getAbsolutePath(), new NodeConfig());

                this.linkedAccount = info.getAccountAddress();

                node.start();

                gethClient = node.getEthereumClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        finish();
    }

    public void sendTransaction(double amount, Address sendTo) throws Exception {
        //TODO Implement a UI confirmation with options
        //TODO Implement actual transaction using Geth.newTransaction
    }
}
