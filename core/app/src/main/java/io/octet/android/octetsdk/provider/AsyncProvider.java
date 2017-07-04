package io.octet.android.octetsdk.provider;


import android.os.AsyncTask;

import io.octet.android.octetsdk.AccountInfo;
import io.octet.android.octetsdk.ResultCallback;

public interface AsyncProvider {

    void requestLink(ResultCallback<Boolean> canLink);

    void defaultAccount(ResultCallback<AccountInfo> result);
}
