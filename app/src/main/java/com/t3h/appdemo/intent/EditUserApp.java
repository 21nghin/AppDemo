package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserApp extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private static final int PICK_IMAGE = 123;
    private Toolbar toolbar;
    private TextInputLayout textName;
    private TextInputLayout textPass;
    private TextInputLayout textCofirmPass;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtCofirmPass;
    private String name, email, pass, confirmPass;
    private CircleImageView imAddImage;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    private FirebaseStorage mFireSt;
    private StorageReference mStorageRef;

    private Uri uri;
    private User user;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_edit_user);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mFireSt = FirebaseStorage.getInstance();
        mStorageRef = mFireSt.getReference("Images");
        mDatabaseRef = mDatabase.getReference("User");

        toolbar = findViewById(R.id.id_edit_toolbar);
        toolbar.setTitle(getString(R.string.tv_edit_user));
        toolbar.setTitleTextColor(Color.DKGRAY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        initViews();
        getDataFirebase();
    }


    private void getDataFirebase() {
        mDatabaseRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                edtName.setText(user.getName());
                edtEmail.setText(user.getEmail());
                edtPass.setText(user.getPasswrod());
                edtCofirmPass.setText(user.getPasswrod());
                Picasso.with(EditUserApp.this).load(user.getImage())
                        .skipMemoryCache()
                        .error(R.drawable.ic_account_circle)
                        .placeholder(R.drawable.ic_account_circle)
                        .fit()
                        .centerCrop()
                        .into(imAddImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditUserApp.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initViews() {
        textName = findViewById(R.id.text_user_name);
        textPass = findViewById(R.id.text_user_change_password);
        textCofirmPass = findViewById(R.id.text_user_retype_pass);
        imAddImage = findViewById(R.id.cim_avatar);

        progressDialog = new ProgressDialog(this);

        edtName = findViewById(R.id.edt_user_name);
        edtEmail = findViewById(R.id.edt_user_email);
        edtPass = findViewById(R.id.edt_user_change_password);
        edtCofirmPass = findViewById(R.id.edt_user_retype_password);

        edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtCofirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtName.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtPass.addTextChangedListener(this);
        edtCofirmPass.addTextChangedListener(this);

        imAddImage.setOnClickListener(this);

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
                email = edtEmail.getText().toString();
                pass = edtPass.getText().toString().trim();
                confirmPass = edtCofirmPass.getText().toString().trim();

                if (name.isEmpty()) {
                    textName.setError(getString(R.string.is_empty_user));
                    return false;
                }
                if (pass.isEmpty()) {
                    textPass.setError(getString(R.string.is_empty_pass));
                    return false;
                }
                if ((pass == null || pass.trim().length() <= 0) && confirmPass.trim().length() > 0) {
                    textCofirmPass.setError(getString(R.string.is_empty_rg_confirm_passwrod));
                } else if (pass != null && pass.trim().length() > 0) {
                    if (pass.equals(confirmPass)) {
                        textCofirmPass.setError(null);
                    } else {
                        textCofirmPass.setError(getString(R.string.password_incorrect));
                        return false;
                    }

                }
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (uri != null) {
                    final StorageReference filePath = mStorageRef.child("User" + "." + getFileExtension(uri));
                    filePath.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                            //TODO
                                            Toast.makeText(EditUserApp.this, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                                            User user = new User(name, email, pass, url);
                                            doUpdateUser(user);
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditUserApp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    user.setName(name);
                    user.setEmail(email);
                    user.setPasswrod(pass);
                    doUpdateUser(user);
                }

                break;
        }
        return true;
    }

    private void doUpdateUser(User user) {
        mDatabaseRef.child(mAuth.getUid()).setValue(user);
        mUser.updatePassword(pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // TODO
                        } else {
                            //TODO
                        }
                    }
                });
        finish();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
        if (charSequence.length() == 0) {
            textPass.setErrorEnabled(false);
            textPass.setError(null);
        } else {
            textPass.setErrorEnabled(false);
            textPass.setError(null);
        }
        if (charSequence.length() == 0) {
            textCofirmPass.setErrorEnabled(false);
            textCofirmPass.setError(null);
        } else {
            textCofirmPass.setErrorEnabled(false);
            textCofirmPass.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null && data.getData() != null) {
            uri = data.getData();
            Picasso.with(this).load(uri).into(imAddImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        openFileChoose();
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
    }
}
