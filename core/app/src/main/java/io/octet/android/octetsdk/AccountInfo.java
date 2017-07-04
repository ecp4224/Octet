package io.octet.android.octetsdk;


import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Context;
import org.ethereum.geth.EthereumClient;

import java.io.Serializable;

public final class AccountInfo implements Serializable {
    private BigInt balance;
    private byte[] bytes;

    public AccountInfo(BigInt balance, byte[] address) {
        this.balance = balance;
        this.bytes = address;
    }

    public AccountInfo(Account account, EthereumClient client, Context context) throws Exception {
        this.bytes = account.getAddress().getBytes();

        this.balance = client.getBalanceAt(context, account.getAddress(), -1);
    }

    public BigInt getBalance() {
        return balance;
    }

    public long balance() {
        return balance.getInt64();
    }

    public byte[] getBytes() {
        return bytes;
    }
}
