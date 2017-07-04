package io.octet.android.octetsdk.provider;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

public class ServiceProvider extends Service {
    public static final int REQUEST_LINK = 1;
    public static final int LINK_SUCCESS = 2;
    public static final int LINK_FAILED = 3;
    public static final int DEFAULT_ACCOUNT = 4;

    private final LongSparseArray<Messenger> clients = new LongSparseArray<>();
    private AsyncProvider provider;
    public ServiceProvider(AsyncProvider provider) {
        this.provider = provider;
    }

    public ServiceProvider(Provider provider) {
        this.provider = ProviderHelper.wrap(provider);
    }


    final Messenger connection = new Messenger(new ProviderHandler(this));
    @Nullable
    @Override
    public final IBinder onBind(Intent intent) {
        return connection.getBinder();
    }

    final boolean hasID(long nextID) {
        return clients.get(nextID) != null;
    }

    final void link(long nextID, Messenger replyTo) throws RemoteException {
        if (hasID(nextID))
            throw new RuntimeException("This ID is already being used!");

        clients.put(nextID, replyTo);

        Message msg = Message.obtain(null, LINK_SUCCESS, nextID);
        replyTo.send(msg);
    }

    public AsyncProvider getProvider() {
        return provider;
    }

    public Messenger getClient(long id) {
        return clients.get(id);
    }
}
