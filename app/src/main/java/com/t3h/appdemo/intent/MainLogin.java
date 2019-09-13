package com.t3h.appdemo.intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.t3h.appdemo.R;
import com.t3h.appdemo.fragment.LoginFragment;
import com.t3h.appdemo.fragment.RegisterFragment;

public class MainLogin extends AppCompatActivity {

    private LoginFragment login = new LoginFragment();
    private RegisterFragment register = new RegisterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);

        initFragment();
        showFragment(login);
    }

    public void showFragment(Fragment frm) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        transaction.hide(login);
        transaction.hide(register);
        transaction.show(frm);
        transaction.commit();
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.id_panel,login);
        transaction.add(R.id.id_panel,register);
        transaction.commit();
    }

    public LoginFragment getLogin() {
        return login;
    }

    public RegisterFragment getRegister() {
        return register;
    }

}
