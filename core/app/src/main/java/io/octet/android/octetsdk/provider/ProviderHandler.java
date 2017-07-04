package io.octet.android.octetsdk.provider;


import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.security.SecureRandom;

import io.octet.android.octetsdk.AccountInfo;
import io.octet.android.octetsdk.ResultCallback;

import static io.octet.android.octetsdk.provider.ServiceProvider.*;

public class ProviderHandler extends Handler {
    private final ServiceProvider serviceProvider;
    private final AsyncProvider provider;
    private final SecureRandom RANDOM = new SecureRandom();

    public ProviderHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        this.provider = this.serviceProvider.getProvider();
    }

    @Override
    public void handleMessage(final Message msg) {
        if (msg.what != REQUEST_LINK && !serviceProvider.hasID((Long) msg.obj))
            return;

        switch (msg.what) {
            case REQUEST_LINK:
                provider.requestLink(new ResultCallback<Boolean>() {
                    @Override
                    public void completed(Boolean result) {
                        try {
                            if (!result) {
                                Message failMsg = Message.obtain(null, LINK_FAILED);
                                msg.replyTo.send(failMsg);
                                return;
                            }

                            long nextID;
                            do {
                                nextID = RANDOM.nextLong();
                            } while (serviceProvider.hasID(nextID));

                            serviceProvider.link(nextID, msg.replyTo);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case DEFAULT_ACCOUNT:
                provider.defaultAccount(new ResultCallback<AccountInfo>() {
                    @Override
                    public void completed(AccountInfo result) {
                        Messenger client = serviceProvider.getClient((Long)msg.obj);

                        Message msg = Message.obtain(null, DEFAULT_ACCOUNT, result);
                        try {
                            client.send(msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }
}
