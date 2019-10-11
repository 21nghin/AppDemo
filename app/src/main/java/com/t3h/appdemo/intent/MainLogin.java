package com.t3h.appdemo.intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.appdemo.R;
import com.t3h.appdemo.fragment.LoginFragment;
import com.t3h.appdemo.fragment.RegisterFragment;

import java.util.HashMap;

public class MainLogin extends AppCompatActivity {

    private LoginFragment login = new LoginFragment();
    private RegisterFragment register = new RegisterFragment();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
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
