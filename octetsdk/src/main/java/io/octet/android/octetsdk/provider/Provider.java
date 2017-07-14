package io.octet.android.octetsdk.provider;


import org.ethereum.geth.Account;

import java.io.Serializable;

import io.octet.android.octetsdk.merchant.EthereumTask;

public interface Provider extends Serializable {

    void onUnlockRequest(Account account, EthereumTask<Account> onUnlock);


}
