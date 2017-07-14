package io.octet.android.octetsdk.provider.dialogs;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ethereum.geth.Account;
import org.ethereum.geth.Accounts;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.octet.android.octetsdk.R;
import io.octet.android.octetsdk.utils.Blockies;

public class AccountListViewAdapter extends ArrayAdapter<Account> {
    private final Blockies BLOCKIES = new Blockies();
    private Accounts accountManager;
    public AccountListViewAdapter(@NonNull Context context, Accounts accountManager) {
        super(context, -1);
        this.accountManager = accountManager;
    }

    @Override
    public Account getItem(int position) {
        try {
            return accountManager.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCount() {
        return (int) accountManager.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_info, parent, false);
            holder.imageView = (CircleImageView) convertView.findViewById(R.id.account_image);
            holder.address = (TextView) convertView.findViewById(R.id.account_address);
            holder.name = (TextView) convertView.findViewById(R.id.account_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.address.setText(account.getAddress().getHex());
        holder.name.setText(account.toString()); //TODO How do we get the name?

        Bitmap bmp = BLOCKIES.createIcon(account.getAddress().getHex());

        holder.imageView.setImageBitmap(bmp);

        return convertView;
    }

    private final class ViewHolder {
        public CircleImageView imageView;
        public TextView address;
        public TextView name;
    }
}
