package com.t3h.appdemo.intent;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.CmtAdapter;
import com.t3h.appdemo.model.CmtModel;
import com.t3h.appdemo.model.JobModel;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;

public class DetailNewsApp extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rcvCmt;
    private CmtAdapter adapter;
    private ArrayList<CmtModel> data = new ArrayList<>();
    private BottomAppBar appBar;
    private EditText edtComment;
    private ImageButton btnSaved;
    private ImageButton btnDelete;
    private ImageButton btnChat;
    private TextView tvIntroduceJob;
    private TextView tvTitle;
    private TextView tvCompanyAddress;
    private TextView tvCompanyTimeJob;
    private TextView tvCompanyEmail;
    private TextView tvSomeCompanyInfomation;
    private TextView tvInfomationJob;
    private TextView tvRecruiTimePeriod;
    private TextView tvDateNow;
    private ImageView imComment;
    private ImageView imLogo;
    private Button btnSendCV;

    private Toolbar toolbar;
    private AlertDialog.Builder mDialog;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    private JobModel job;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        initView();
        initData();
        loadDataJob();
    }

    private void loadDataJob() {

        Intent loadData = this.getIntent();

        String image = loadData.getExtras().getString(Const.IMAGE_KEY);
        String title = loadData.getExtras().getString(Const.TITLE_KEY);
        String introduceJob = loadData.getExtras().getString(Const.INTRDUCE_JOB_KEY);
        String companyAddress = loadData.getExtras().getString(Const.COMPANY_ADDRESS_KEY);
        String jobTime = loadData.getExtras().getString(Const.JOB_TIME_KEY);
        String companyEmail = loadData.getExtras().getString(Const.COMPANY_EMAIL_KEY);
        String someCompanyInformation = loadData.getExtras().getString(Const.SOME_COMPANY_INFOMATION_KEY);
        String informationJob = loadData.getExtras().getString(Const.INFOMATION_JOB_KEY);
        String recruiTime = loadData.getExtras().getString(Const.RECRUI_TIME_KEY);
        String dateNow = loadData.getExtras().getString(Const.DATE_NOW_KEY);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.infotect_jobs)
                .centerCrop()
                .skipMemoryCache(true)
                .into(imLogo);
        tvTitle.setText(title);
        tvIntroduceJob.setText(introduceJob);
        tvCompanyAddress.setText(companyAddress);
        tvCompanyTimeJob.setText(jobTime);
        tvCompanyEmail.setText(companyEmail);
        tvSomeCompanyInfomation.setText(someCompanyInformation);
        tvInfomationJob.setText(informationJob);
        tvRecruiTimePeriod.setText(recruiTime);
        tvDateNow.setText(dateNow);
    }


    private void initData() {
        adapter = new CmtAdapter(this);

        adapter.setData(data);
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
        btnSendCV = findViewById(R.id.detail_btn_send_cv);
        btnChat = findViewById(R.id.item_detail_chat);
        btnSaved = findViewById(R.id.item_saved);
        btnDelete = findViewById(R.id.item_detail_delete);
        btnChat = findViewById(R.id.item_detail_chat);
        edtComment = findViewById(R.id.edt_comment);
        appBar = findViewById(R.id.id_detail_app_bar);
        rcvCmt = findViewById(R.id.lv_comment);

        toolbar.setTitle(getString(R.string.title_detail_job));
        toolbar.setTitleTextColor(Color.DKGRAY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_24dp);

        rcvCmt.setAdapter(adapter);
        btnDelete.setOnClickListener(this);
        btnSaved.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnSendCV.setOnClickListener(this);
        imComment.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_detail_chat:
                Toast.makeText(this, "chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_detail_delete:
                mDialog = new AlertDialog.Builder(this);
                mDialog.setMessage(getString(R.string.dialog_message_delete_detel))
                        .setPositiveButton(getString(R.string.message_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.message_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                mDialog.show();
                break;
            case R.id.item_saved:
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                break;
            case R.id.detail_btn_send_cv:
                break;
            case R.id.detail_im_comment:
                break;
        }
    }
}
