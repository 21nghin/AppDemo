package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.User;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Toolbar toolbar;
    private TextInputLayout textName;
    private TextInputLayout textEmail;
    private TextInputLayout textPass;
    private TextInputLayout textCofirmPass;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtCofirmPass;
    private Button btnChangePass;
    private String name, email, pass, confirmPass;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.id_edit_toolbar);
        toolbar.setTitle(getString(R.string.tv_edit_user));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        initViews();
        getDataFirebase();
    }

    private void getDataFirebase() {
        mReference = mDatabase.getReference("User");
        mReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                edtName.setText(user.getName());
                edtEmail.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditUserActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initViews() {
        textName = findViewById(R.id.text_user_name);
        textEmail = findViewById(R.id.text_user_email);
        textPass = findViewById(R.id.text_user_change_password);
        textCofirmPass = findViewById(R.id.text_user_retype_pass);

        edtName = findViewById(R.id.edt_user_name);
        edtEmail = findViewById(R.id.edt_user_email);
        edtPass = findViewById(R.id.edt_user_change_password);
        edtCofirmPass = findViewById(R.id.edt_user_retype_password);
        edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtCofirmPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtName.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtPass.addTextChangedListener(this);
        edtCofirmPass.addTextChangedListener(this);

        btnChangePass = findViewById(R.id.btn_change_pass);
        btnChangePass.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_add:
                name = edtName.getText().toString();
                pass = edtPass.getText().toString();
                if (name.isEmpty()) {
                    textName.setError(getString(R.string.is_empty_user));
                    break;
                }
                if (pass.isEmpty()){
                    textPass.setError(getString(R.string.is_empty_pass));
                    break;
                }
                User user = new User(name,pass,email);
                mReference.setValue(user);
                finish();
                Toast.makeText(this, "add Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() == 0) {
            textName.setErrorEnabled(false);
            textName.setError(null);
        } else {
            textName.setErrorEnabled(false);
            textName.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
