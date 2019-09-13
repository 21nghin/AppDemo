package com.t3h.appdemo.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.t3h.appdemo.intent.MainLogin;
import com.t3h.appdemo.R;
import com.t3h.appdemo.intent.ProfileActivity;

public class BottomDialogFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageView imClose;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

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
            //kiểm tra bù trượt và thay đổi mức độ hiển thị của nút đóng
            if (v > 0.5) {
                imClose.setVisibility(View.VISIBLE);
            }else {
                imClose.setVisibility(View.GONE);
            }
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Nhận nội dung Xem
        View v = View.inflate(getContext(),R.layout.fragment_bottom_dialog,null);
        dialog.setContentView(v);
        navigationView = v.findViewById(R.id.navigation_view);

        //thực hiện điều hướng menu nhấp chuột sự kiện
        navigationView.setNavigationItemSelectedListener(this);
        imClose = v.findViewById(R.id.close_image_view);
        imClose.setOnClickListener(this);
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)((View) v.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior instanceof BottomSheetBehavior){
            ((BottomSheetBehavior)behavior).setBottomSheetCallback(callback);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item_info:
                startActivity(new Intent(getContext(), ProfileActivity.class));
                break;
            case R.id.id_nav16:
                mAuth = FirebaseAuth.getInstance();
                logOut();
                break;
        }
        return false;
    }

    private void logOut() {
        mAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(getContext(), MainLogin.class));
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
