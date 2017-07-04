package io.octet.android.octetsdk.merchant;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import io.octet.android.octetsdk.AccountInfo;
import io.octet.android.octetsdk.ResultCallback;
import io.octet.android.octetsdk.provider.ServiceProvider;

public final class EthereumLink {
    public static final String ACTION_LINK = "io.octet.android.LINK";

    public static final int NOT_LINKED = 0;
    public static final int REQUESTING_LINK = 1;
    public static final int ESTABLISHING_LINK = 2;
    public static final int LINKED = 3;


    private Messenger provider;
    private Context androidContext;
    private int linkState = NOT_LINKED;
    private final Messenger messenger = new Messenger(new LinkHandler(this));
    private final ServiceConnection link = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            provider = new Messenger(service);

            try {
                link();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            provider = null;
        }
    };
    private long id;

    private AccountInfo defaultAccount;

    /**
     * Request a link to the Ethereum network with any app/service providing a link. The
     * link may not be established after this function call is complete
     * @param context The app's context
     * @return A new {@link EthereumLink}.
     */
    public static EthereumLink requestLink(Context context) {
        EthereumLink link = new EthereumLink();
        link.bindRequest(context);

        return link;
    }

    /**
     * Request a link to the Ethereum network with a specific {@link ServiceProvider}. The
     * link may not be established after this function call is complete
     * @param context The app's context
     * @param class_ The class of the {@link ServiceProvider} to link to
     * @return A new {@link EthereumLink}.
     */
    public static EthereumLink link(Context context, Class<? extends ServiceProvider> class_) {
        EthereumLink link = new EthereumLink();
        link.bind(context, class_);

        return link;
    }

    private void bind(Context androidContext, Class<? extends ServiceProvider> class_) {
        this.androidContext = androidContext;

        Intent bindIntent = new Intent(androidContext, class_);
        this.androidContext.bindService(bindIntent, link,
                //TODO Figure out which flags to use here
                Context.BIND_ABOVE_CLIENT | Context.BIND_AUTO_CREATE
        );
        linkState = REQUESTING_LINK;
    }

    private void bindRequest(Context androidContext) {
        this.androidContext = androidContext;

        Intent bindIntent = new Intent(ACTION_LINK);
        this.androidContext.bindService(bindIntent, link,
                //TODO Figure out which flags to use here
                Context.BIND_ABOVE_CLIENT | Context.BIND_AUTO_CREATE
        );
        linkState = REQUESTING_LINK;
    }

    private EthereumLink() { }

    private void link() throws RemoteException {
        Message msg = Message.obtain(null, ServiceProvider.REQUEST_LINK);
        msg.replyTo = messenger;
        provider.send(msg);
        linkState = ESTABLISHING_LINK;
    }

    void completeLink(long id) {
        linkState = LINKED;

        this.id = id;
    }

    public boolean isLinked() {
        return linkState == 3;
    }

    public int getLinkedState() {
        return linkState;
    }

    private ResultCallback<AccountInfo> callback;
    //TODO Should this class poll data every m seconds and provide a stateful API?
    public void defaultAccount(ResultCallback<AccountInfo> callback) {
        this.callback = callback;

        Message msg = Message.obtain(null, ServiceProvider.DEFAULT_ACCOUNT, id);
        try {
            provider.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            linkState = NOT_LINKED;
        }
    }

    void updateDefaultAccount(AccountInfo defaultAccount) {
        if (this.callback != null) {
            callback.completed(defaultAccount);
        }

        this.defaultAccount = defaultAccount;
    }
}
