package com.t3h.appdemo.intent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.CommentAdapter;
import com.t3h.appdemo.model.Comments;
import com.t3h.appdemo.model.User;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailNewsApp extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailNewsApp";
    private RecyclerView rcvCmt;
    private ArrayList<Comments> arrData;
    private CommentAdapter adapterCMT;
    private BottomAppBar appBar;
    private EditText edtComment;
    private TextView tvIntroduceJob;
    private TextView tvTitle;
    private TextView tvCompanyAddress;
    private TextView tvCompanyTimeJob;
    private TextView tvCompanyEmail;
    private TextView tvSomeCompanyInfomation;
    private TextView tvInfomationJob;
    private TextView tvRecruiTimePeriod;
    private TextView tvDateNow;
    private CircleImageView civMyImage;
    private ImageView imComment;
    private ImageView imLogo;
    private boolean quaTrinhBinhLuan = false;

    private DatabaseReference databaseReference;
    private FirebaseAuth fireAuth;

    private FirebaseDatabase fireData;

    private Calendar calendar;

    private Toolbar toolbar;

    private String myUid, myEmail, myName, myImage, postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        initView();
        checkUserStatus();
        getIdUsing();
        loadPostJobInfo();
        loadUserInfo();
        loadComments();
    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rcvCmt.setLayoutManager(layoutManager);
        arrData = new ArrayList<>();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrData.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Comments comments = snapshot.getValue(Comments.class);
                    arrData.add(comments);
                    adapterCMT = new CommentAdapter(getApplicationContext(),arrData,myUid,postId);
                    rcvCmt.setAdapter(adapterCMT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {

        fireAuth = FirebaseAuth.getInstance();
        fireData = FirebaseDatabase.getInstance();
        databaseReference = fireData.getReference("Users").child(fireAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext())
                        .load(user.getImageUrl())
                        .skipMemoryCache(true)
                        .error(R.drawable.im_account)
                        .centerCrop()
                        .into(civMyImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailNewsApp.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(myEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    myName = "" + snapshot.child("name").getValue();
                    myImage = "" + snapshot.child("imageUrl").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostJobInfo() {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = dataRef.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String pTile = "" + snapshot.child("pTile").getValue();
                    String pSomeCompany = "" + snapshot.child("pSomeCompanyInformation").getValue();
                    String pIntroductJob = "" + snapshot.child("pIntroductJob").getValue();
                    String pCompanyAddress = "" + snapshot.child("pCompanyAddress").getValue();
                    String pCompanyEmail = "" + snapshot.child("pCompanyEmail").getValue();
                    String pInfomationJob = "" + snapshot.child("pInfomationJob").getValue();
                    String pRecruitTime = "" + snapshot.child("pRecruitTime").getValue();
                    String pJobTime = "" + snapshot.child("pJobTime").getValue();
                    String pDateNow = "" + snapshot.child("pDateNow").getValue();
                    String pImage = "" + snapshot.child("pImage").getValue();
                    String uid = "" + snapshot.child("uid").getValue();
                    String uEmail = "" + snapshot.child("uEmail").getValue();
                    String commentCount = "" + snapshot.child("pComments").getValue();

                    tvTitle.setText(pTile);
                    tvSomeCompanyInfomation.setText(pSomeCompany);
                    tvIntroduceJob.setText(pIntroductJob);
                    tvCompanyAddress.setText(pCompanyAddress);
                    tvCompanyEmail.setText(pCompanyEmail);
                    tvInfomationJob.setText(pInfomationJob);
                    tvRecruiTimePeriod.setText(pRecruitTime);
                    tvCompanyTimeJob.setText(pJobTime);
                    tvDateNow.setText(pDateNow);

                    try {
                        Glide.with(DetailNewsApp.this).load(pImage)
                                .skipMemoryCache(true)
                                .error(R.drawable.media_img)
                                .centerInside()
                                .into(imLogo);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getIdUsing() {
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
    }

    private void initView() {

        toolbar = findViewById(R.id.detail_toolbar);
        tvTitle = findViewById(R.id.detail_tv_title);
        tvIntroduceJob = findViewById(R.id.detail_tv_content_intro);
        tvCompanyAddress = findViewById(R.id.detail_content_company_address);
        tvCompanyTimeJob = findViewById(R.id.detail_content_company_time);
        tvCompanyEmail = findViewById(R.id.detail_tv_content_address);
        tvSomeCompanyInfomation = findViewById(R.id.detail_tv_content_some_infor);
        tvInfomationJob = findViewById(R.id.detail_tv_content_information);
        tvRecruiTimePeriod = findViewById(R.id.detail_tv_content_recrui_time);
        tvDateNow = findViewById(R.id.detail_tv_date);
        imComment = findViewById(R.id.detail_im_comment);
        imLogo = findViewById(R.id.detail_im_image);
        civMyImage = findViewById(R.id.civ_my_image);
        edtComment = findViewById(R.id.edt_comment);
        appBar = findViewById(R.id.id_detail_app_bar);
        rcvCmt = findViewById(R.id.lv_comment);
        rcvCmt.setNestedScrollingEnabled(false);

        toolbar.setTitle(getString(R.string.title_detail_job));
        toolbar.setTitleTextColor(Color.DKGRAY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_24dp);

        imComment.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                checkUserStatus();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_im_comment:
                calendar = Calendar.getInstance();
                String dateNow = DateFormat.getDateInstance().format(calendar.getTime());
                postComment(dateNow);
                break;
        }
    }

    private void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void postComment(String dateNow) {
        String comment = edtComment.getText().toString().trim();
        if (comment.isEmpty()) {
            message("Comment is empty...");
            return;
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timeStamp);
        hashMap.put("comment", comment);
        hashMap.put("timeStamp", dateNow);
        hashMap.put("uid", myUid);
        hashMap.put("uEmail", myEmail);
        hashMap.put("uImageUrl", myImage);
        hashMap.put("uName", myName);

        dataRef.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        message("Add Comment...");
                        edtComment.setText("");
                        updateCommentCount();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailNewsApp.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCommentCount() {
        quaTrinhBinhLuan = true;
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (quaTrinhBinhLuan) {
                    String comments = "" + dataSnapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    dataRef.child("pComments").setValue("" + newCommentVal);
                    quaTrinhBinhLuan = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myEmail = user.getEmail();
            myUid = user.getUid();

        } else {
            startActivity(new Intent(this, MainApp.class));
            finish();
        }
    }

}
