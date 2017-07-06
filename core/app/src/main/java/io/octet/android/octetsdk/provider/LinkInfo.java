package io.octet.android.octetsdk.provider;


import android.content.Intent;
import org.ethereum.geth.Address;

import java.io.File;
import java.io.Serializable;

/**
 * Represents a link result to be returned to an {@link io.octet.android.octetsdk.merchant.EthereumLink}.
 *
 * A {@link LinkInfo} object will contain the keystore path to the associated wallet <b>and</b>
 * an index for the account to use in {@link org.ethereum.geth.Accounts#get(long)}
 *
 * If the link provider does not want to provide keystore access (whether because it's encrypted or
 * other reason), then the provider can simply give a {@link Address} of the account to use.
 *
 * Nothing is gained/lost from using either constructor
 */
public class LinkInfo implements Serializable {
    public static final String EXTRA_ID = "io.octet.android.LINKINFO";

    //OPTION 1
    private String filePath;
    private long accountIndex;

    //OPTION 2
    private byte[] exportedAccount;
    private String exportedPassword;

    public LinkInfo(String filePath, long accountIndex) {
        this.filePath = filePath;
        this.accountIndex = accountIndex;
    }

    public LinkInfo(byte[] exportedAccount, String exportedPassword) {
        this.exportedAccount = exportedAccount;
        this.exportedPassword = exportedPassword;
    }

    /**
     * Get the keystore path for this wallet
     * @return The file path of the keystore owning this wallet
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Get the account to limit access to for this wallet
     * @return The account to use
     */
    public long getAccountIndex() {
        return accountIndex;
    }

    public byte[] getExportedAccount() {
        return exportedAccount;
    }

    public String getExportedPassword() {
        return exportedPassword;
    }

    /**
     * Whether the wallet app has given us keystore access, or if they are
     * REEEEEEEEE
     *
     * If no access is provided, then {@link LinkInfo#getExportedAccount()}
     * will return a valid exported account
     * @return Whether keystore access was granted
     */
    public boolean hasKeystoreAccess() {
        return filePath != null;
    }

    public Intent putIn(Intent intent) {
        intent.putExtra(EXTRA_ID, this);

        return intent;
    }

    public File getFile() {
        if (filePath == null)
            return null;

        return new File(filePath);
    }
}
