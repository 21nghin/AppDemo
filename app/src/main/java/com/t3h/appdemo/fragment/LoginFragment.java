package com.t3h.appdemo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.t3h.appdemo.intent.MainApp;
import com.t3h.appdemo.intent.MainLogin;
import com.t3h.appdemo.intent.PasswordApp;
import com.t3h.appdemo.R;


public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private TextInputLayout textUsername;
    private TextInputLayout textPasswrod;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPasswrod;
    private TextView tvForgotpasswrod;
    private TextView tvRegister;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getActivity().finish();
            startActivity(new Intent(getContext(), MainApp.class));
        }

        initView();
    }

    private void initView() {
        textUsername = getActivity().findViewById(R.id.text_lg_username);
        textPasswrod = getActivity().findViewById(R.id.text_lg_password);
        tvForgotpasswrod = getActivity().findViewById(R.id.tv_forgot_password);
        tvRegister = getActivity().findViewById(R.id.id_tv_lg_register);
        btnLogin = getActivity().findViewById(R.id.id_btn_lg_login);
        edtEmail = getActivity().findViewById(R.id.edt_lg_username);
        edtPasswrod = getActivity().findViewById(R.id.edt_lg_passwrod);
        edtPasswrod.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtEmail.addTextChangedListener(this);
        edtPasswrod.addTextChangedListener(this);

        tvForgotpasswrod.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_password:
                startActivity(new Intent(getContext(), PasswordApp.class));
                break;
            case R.id.id_tv_lg_register:
                MainLogin activity = (MainLogin) getActivity();
                activity.showFragment(activity.getRegister());
                break;
            case R.id.id_btn_lg_login:
                String email = edtEmail.getText().toString();
                String pass = edtPasswrod.getText().toString();
                if (email.isEmpty()) {
                    textUsername.setError(getString(R.string.is_empty_user));
                    return;
                }
                if (pass.isEmpty()) {
                    textPasswrod.setError(getString(R.string.is_empty_pass));
                    return;
                }

                validateLogin(email, pass);
                break;
        }
    }

    private void validateLogin(String email, String pass) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            progressDialog.dismiss();
                            startActivity(new Intent(getContext(), MainApp.class));
                            getActivity().finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), getString(R.string.text_login_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setData(String username, String passwrod) {
        textUsername.getEditText().setText(username);
        textPasswrod.getEditText().setText(passwrod);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            textUsername.setErrorEnabled(false);
        } else {
            textUsername.setErrorEnabled(false);
            textUsername.setError(null);
            return;
        }
        if (charSequence.length() == 0) {
            textPasswrod.setErrorEnabled(false);
        } else {
            textPasswrod.setErrorEnabled(false);
            textPasswrod.setError(null);
            return;
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            textUsername.setErrorEnabled(false);
        } else {
            textUsername.setErrorEnabled(false);
            textUsername.setError(null);
            return;
        }
        if (charSequence.length() == 0) {
            textPasswrod.setErrorEnabled(false);
        } else {
            textPasswrod.setErrorEnabled(false);
            textPasswrod.setError(null);
            return;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
