package io.octet.android.octetsdk.merchant;


public interface EthereumTask<T> {

    void onComplete(T param);

    void onFailed(String reason, Throwable cause);
}
