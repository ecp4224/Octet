package io.octet.android.octetsdk.provider;


import io.octet.android.octetsdk.AccountInfo;

public interface Provider {

    boolean requestLink();

    AccountInfo defaultAccount();
}
