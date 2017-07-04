package io.octet.android.octetsdk.provider;


import io.octet.android.octetsdk.AccountInfo;
import io.octet.android.octetsdk.PRunnable;

public interface Provider {

    void requestLink(PRunnable<Boolean> canLink);

    void defaultAccount(PRunnable<AccountInfo> result);
}
