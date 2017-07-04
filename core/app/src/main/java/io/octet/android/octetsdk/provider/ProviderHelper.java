package io.octet.android.octetsdk.provider;


import android.os.AsyncTask;

import io.octet.android.octetsdk.AccountInfo;
import io.octet.android.octetsdk.ResultCallback;

public final class ProviderHelper {

    /**
     * Transforms a {@link Provider} to an {@link AsyncProvider} by simply
     * chaining the {@link android.os.AsyncTask} callbacks to the supplied {@link Provider}.
     * @param provider The {@link Provider} to chain
     * @return A new {@link AsyncProvider} that chains to the supplied {@link Provider}
     */
    public static AsyncProvider wrap(final Provider provider) {
        return new AsyncProvider() {
            @Override
            public void requestLink(ResultCallback<Boolean> canLink) {
                canLink.completed(provider.requestLink());
            }

            @Override
            public void defaultAccount(ResultCallback<AccountInfo> result) {
                result.completed(provider.defaultAccount());
            }
        };
    }
}
