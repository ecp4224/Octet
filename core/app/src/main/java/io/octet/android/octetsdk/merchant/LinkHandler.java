package io.octet.android.octetsdk.merchant;


import android.os.Handler;
import android.os.Message;

import io.octet.android.octetsdk.AccountInfo;

import static io.octet.android.octetsdk.provider.ServiceProvider.DEFAULT_ACCOUNT;
import static io.octet.android.octetsdk.provider.ServiceProvider.LINK_SUCCESS;

public class LinkHandler extends Handler {
    private final EthereumLink link;

    public LinkHandler(EthereumLink link) {
        this.link = link;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case LINK_SUCCESS:
                link.completeLink((Long) msg.obj);
                break;
            case DEFAULT_ACCOUNT:
                link.updateDefaultAccount((AccountInfo)msg.obj);
                break;
        }
    }
}
