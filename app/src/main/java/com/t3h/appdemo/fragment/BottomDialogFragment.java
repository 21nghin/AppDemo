package com.t3h.appdemo.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.intent.CVApp;
import com.t3h.appdemo.intent.ChatApp;
import com.t3h.appdemo.intent.MainLogin;
import com.t3h.appdemo.R;
import com.t3h.appdemo.intent.UserApp;
import com.t3h.appdemo.model.User;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomDialogFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ValueEventListener {

    private DatabaseReference dataRef;
    private FirebaseUser fireUser;

    private ImageView imClose;
    private NavigationView navigationView;
    private TextView tvName;
    private TextView tvEmail;
    private CircleImageView imImage;
    
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ProgressDialog progressDialog;
    private DatabaseReference reference;

    public static BottomDialogFragment newInstance() {
        Bundle bundle = new Bundle();

        BottomDialogFragment frm = new BottomDialogFragment();
        frm.setArguments(bundle);
        return frm;
    }

    private BottomSheetBehavior.BottomSheetCallback callback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int i) {
            if (i == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {
            if (v > 0.5) {
                imClose.setVisibility(View.VISIBLE);
            } else {
                imClose.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("Users").child(mAuth.getUid());
        reference.addValueEventListener(this);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View v = View.inflate(getContext(), R.layout.fragment_bottom_dialog, null);
        dialog.setContentView(v);

        navigationView = v.findViewById(R.id.navigation_view);

//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference reference = mDatabase.getReference("User");
//        reference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                tvEmail.setText(user.getEmail());
//                tvName.setText(user.getName());
//                Glide.with(getActivity()).load(user.getImage())
//                        .skipMemoryCache(true)
//                        .error(R.drawable.ic_account_circle_black_24dp)
//                        .placeholder(R.drawable.ic_account_circle_black_24dp)
//                        .centerCrop()
//                        .into(imImage);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
//            }
//        });

        tvEmail = v.findViewById(R.id.tv_email);
        tvName = v.findViewById(R.id.tv_Name);
        imClose = v.findViewById(R.id.close_image_view);
        imImage = v.findViewById(R.id.profile_image);

        navigationView.setNavigationItemSelectedListener(this);
        imClose.setOnClickListener(this);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) v.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(callback);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_info:
                startActivity(new Intent(getContext(), CVApp.class));
                break;
            case R.id.item_profile:
                startActivity(new Intent(getContext(), UserApp.class));
                break;
            case R.id.item_logout:
                logOut();
                break;
            case R.id.item_chat:
                startActivity(new Intent(getContext(), ChatApp.class));
                break;
            case R.id.id_nav03:

                break;

        }
        return false;
    }

    private void logOut() {
        mAuth.signOut();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getActivity().finish();
        startActivity(new Intent(getContext(), MainLogin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(this);
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        tvEmail.setText(user.getEmail());
        tvName.setText(user.getName());
        Glide.with(getActivity()).load(user.getImageUrl())
                .skipMemoryCache(true)
                .error(R.drawable.ic_account_circle_black_24dp)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .centerCrop()
                .into(imImage);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
    }

}
