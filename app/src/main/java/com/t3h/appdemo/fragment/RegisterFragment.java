package com.t3h.appdemo.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.appdemo.intent.MainLogin;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.User;

import es.dmoral.toasty.Toasty;

public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private TextInputLayout textUsername;
    private TextInputLayout textEmail;
    private TextInputLayout textPasswrod;
    private TextInputLayout textConfirmPasswrod;

    private TextInputEditText edtUsername;
    private TextInputEditText edtPasswrod;
    private TextInputEditText edtEmail;
    private TextInputEditText edtConfirmPasswrod;

    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        initView();
    }

    private void initView() {
        textUsername = getActivity().findViewById(R.id.text_rg_username);
        textEmail = getActivity().findViewById(R.id.text_rg_email);
        textPasswrod = getActivity().findViewById(R.id.text_rg_password);
        textConfirmPasswrod = getActivity().findViewById(R.id.text_rg_confirm_password);

        edtUsername = getActivity().findViewById(R.id.edt_rg_username);
        edtEmail = getActivity().findViewById(R.id.edt_rg_email);
        edtPasswrod = getActivity().findViewById(R.id.edt_rg_passwrod);
        edtConfirmPasswrod = getActivity().findViewById(R.id.edt_rg_confirm_passwrod);

        edtPasswrod.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtConfirmPasswrod.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtUsername.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtPasswrod.addTextChangedListener(this);
        edtConfirmPasswrod.addTextChangedListener(this);

        btnRegister = getActivity().findViewById(R.id.id_btn_rg_register);
        tvLogin = getActivity().findViewById(R.id.id_tv_rg_login);

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_rg_login:
                MainLogin activityBack = (MainLogin) getActivity();
                activityBack.showFragment(activityBack.getLogin());
                break;
            case R.id.id_btn_rg_register:
                final String name = textUsername.getEditText().getText().toString().trim();
                final String email = textEmail.getEditText().getText().toString().trim();
                final String passwrod = textPasswrod.getEditText().getText().toString().trim();
                String confirmPasswrod = textConfirmPasswrod.getEditText().getText().toString().trim();

                if (name.isEmpty()) {
                    textUsername.setError(getString(R.string.is_empty_rg_username));
                    return;
                }
                if (email.isEmpty()) {
                    textEmail.setError(getString(R.string.is_empty_rg_email));
                    return;
                }
                if (!isValidEmail(edtEmail.getText().toString().trim())) {
                    textEmail.setError(getString(R.string.enter_a_valid_address));
                    return;
                }
                if (passwrod.isEmpty()) {
                    textPasswrod.setError(getString(R.string.is_empty_rg_passwrod));
                    return;
                }
                if ((passwrod == null || passwrod.trim().length() <= 0) && confirmPasswrod.trim().length() > 0) {
                    textConfirmPasswrod.setError(getString(R.string.is_empty_rg_confirm_passwrod));
                } else if (passwrod != null && passwrod.trim().length() > 0) {
                    if (passwrod.equals(confirmPasswrod)) {
                        textConfirmPasswrod.setError(null);
                    } else {
                        textConfirmPasswrod.setError(getString(R.string.password_incorrect));
                        return;
                    }
                }
                mAuth.createUserWithEmailAndPassword(email,passwrod)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    DatabaseReference reference = mDatabase.getReference("User");
                                    User user = new User(name,email,passwrod);
                                    reference.child(mAuth.getUid()).setValue(user);

                                    Toasty.success(getContext(), getString(R.string.register_success), Toasty.LENGTH_SHORT).show();
                                    MainLogin activityLogin = (MainLogin) getActivity();
                                    activityLogin.showFragment(activityLogin.getLogin());
                                    activityLogin.getLogin().setData(email, passwrod);
                                }else {
                                    Toasty.error(getContext(), getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }

    private boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
            textEmail.setErrorEnabled(false);
        } else {
            textEmail.setErrorEnabled(false);
            textEmail.setError(null);
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
        String passwrod = textPasswrod.getEditText().getText().toString().trim();
        if (passwrod.length()<6){
            textPasswrod.setError(getString(R.string.password_to_short));
        }else{
            textPasswrod.setError(null);
        }

        if (charSequence.length() == 0) {
            textUsername.setErrorEnabled(false);
        } else {
            textUsername.setErrorEnabled(false);
            textUsername.setError(null);
            return;
        }
        if (charSequence.length() == 0) {
            textEmail.setErrorEnabled(false);
        } else {
            textEmail.setErrorEnabled(false);
            textEmail.setError(null);
            return;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
