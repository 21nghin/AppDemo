package com.t3h.appdemo.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.t3h.appdemo.intent.CVApp;
import com.t3h.appdemo.intent.ChatApp;
import com.t3h.appdemo.intent.MainLogin;
import com.t3h.appdemo.R;
import com.t3h.appdemo.intent.UserApp;
import com.t3h.appdemo.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomDialogFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageView imClose;
    private NavigationView navigationView;
    private TextView tvName;
    private TextView tvEmail;
    private CircleImageView imImage;
    
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ProgressDialog progressDialog;

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

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View v = View.inflate(getContext(), R.layout.fragment_bottom_dialog, null);
        dialog.setContentView(v);

        navigationView = v.findViewById(R.id.navigation_view);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase.getReference("User");
        reference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvEmail.setText(user.getEmail());
                tvName.setText(user.getName());
                Picasso.with(getContext()).load(user.getImage())
                        .skipMemoryCache()
                        .error(R.drawable.ic_account_circle_black_24dp)
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .fit()
                        .centerCrop()
                        .into(imImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

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
        }
        return false;
    }

    private void logOut() {
        mAuth.signOut();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getActivity().finish();
        startActivity(new Intent(getContext(), MainLogin.class));
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
