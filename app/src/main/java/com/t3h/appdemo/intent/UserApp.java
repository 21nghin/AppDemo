package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.User;

public class UserApp extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fabEdit;
    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvEmail;
    private CollapsingToolbarLayout ctlName;
    private ImageView imImage;
    private ProgressBar idProgress;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_user);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        getDataFirebase();

    }

    private void getDataFirebase() {
        DatabaseReference mReference = mDatabase.getReference("User");
        mReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvName.setText(user.getName());
                tvEmail.setText(user.getEmail());
                ctlName.setTitle(user.getName());
                Glide.with(UserApp.this).load(user.getImage())
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(imImage);
                idProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserApp.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                idProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViews() {
        ctlName = findViewById(R.id.user_collapsing_toolbar);
        tvName = findViewById(R.id.tv_user_name);
        tvEmail = findViewById(R.id.tv_user_email);
        fabEdit = findViewById(R.id.fab_Edit);
        imImage = findViewById(R.id.user_image);
        idProgress = findViewById(R.id.user_load);
        fabEdit.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, EditUserApp.class));
    }
}
