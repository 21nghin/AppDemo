package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.t3h.appdemo.R;

public class PassordActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout textEmail;
    private TextInputEditText edtEmail;
    private Button btnResetPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passord);

        mAuth = FirebaseAuth.getInstance();

        intView();
    }

    private void intView() {
        textEmail = findViewById(R.id.text_forgor_email);
        edtEmail = findViewById(R.id.edt_forgot_email);
        btnResetPass = findViewById(R.id.btn_reset_pass);

        edtEmail.addTextChangedListener(this);

        btnResetPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
                                Toast.makeText(PassordActivity.this, "Đã gửi làm mới mật khẩu về email!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PassordActivity.this, MainLogin.class));
                            }else {
                                Toast.makeText(PassordActivity.this, "lỗi trong việc gửi email đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
