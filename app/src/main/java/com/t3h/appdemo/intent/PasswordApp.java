package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.t3h.appdemo.R;

public class PasswordApp extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout textEmail;
    private TextInputEditText edtEmail;
    private Button btnResetPass;
    private FirebaseAuth mAuth;
    private TextView tvLogin;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_password);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mAuth = FirebaseAuth.getInstance();

        intView();
    }

    private void intView() {
        textEmail = findViewById(R.id.text_forgor_email);
        edtEmail = findViewById(R.id.edt_forgot_email);
        btnResetPass = findViewById(R.id.btn_reset_pass);
        tvLogin = findViewById(R.id.tv_sign_up);

        edtEmail.addTextChangedListener(this);

        tvLogin.setOnClickListener(this);
        btnResetPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sign_up:
                finish();
                break;
            case R.id.btn_reset_pass:
                String email = edtEmail.getText().toString();
                if (email.isEmpty()) {
                    textEmail.setError(getString(R.string.import_email));
                    return;
                }
                if (!isValiEmail(edtEmail.getText().toString().trim())) {
                    textEmail.setError(getString(R.string.enter_a_valid_address));
                    return;
                } else {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PasswordApp.this, "Đã gửi làm mới mật khẩu về email!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(PasswordApp.this, MainLogin.class));
                                    }else {
                                        Toast.makeText(PasswordApp.this, "lỗi trong việc gửi email đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
        }
    }

    private boolean isValiEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() == 0) {
            textEmail.setErrorEnabled(false);
        } else {
            textEmail.setErrorEnabled(false);
            textEmail.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
