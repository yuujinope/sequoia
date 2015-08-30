package com.tokopedia.toped.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.toped.R;
import com.tokopedia.toped.base.BaseFragment;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class FragmentLauncher extends BaseFragment {

    private class ViewHolder {
        TextInputLayout emailLayout;
        TextInputLayout passwordLayout;
        EditText email;
        EditText password;
        View loginButton;
    }

    private ViewHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getViewId() {
        return R.layout.layout_launcher;
    }

    @Override
    protected Object createViewHolder(View view) {
        holder = new ViewHolder();
        holder.emailLayout = (TextInputLayout) findViewById(R.id.email);
        holder.email = (EditText) findViewById(R.id.email_text);
        holder.password = (EditText) findViewById(R.id.password_text);
        holder.passwordLayout = (TextInputLayout) findViewById(R.id.password);
        holder.loginButton = findViewById(R.id.login_button);
        return holder;
    }

    @Override
    protected void bindViewHolder(Object viewHolder) {
        holder = (ViewHolder) viewHolder;
    }

    @Override
    protected void onCreateView() {
        holder.emailLayout.setHint("Email");
        holder.passwordLayout.setHint("Password");
    }

}
