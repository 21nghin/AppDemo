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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.JobModel;

import java.util.ArrayList;
import java.util.List;

public class CreatNews extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner spinnerCheckRecruitment;
    private Spinner spinnerCheckTime;
    private EditText edtTitle;
    private EditText edtIntroduceJob;
    private EditText edtCompanyAddress;
    private EditText edtCompanyEmail;
    private EditText edtSomeCompanyInfor;
    private EditText edtInfomationJob;
    private TextInputLayout textIntroduceJob;
    private TextInputLayout textCompanyAddress;
    private TextInputLayout textCompanyEmail;
    private TextInputLayout textSomeCompanyInfor;
    private TextInputLayout textInformationJob;
    private ImageView imLogo;
    private Toolbar toolbar;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private Uri uri;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_creat_news);

        initFirebase();
        initViews();
        getDataSpinner();
    }

    private void initFirebase() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Logo");
        databaseReference = FirebaseDatabase.getInstance().getReference("JobModel");
    }

    private void getDataSpinner() {
        List<String> time = new ArrayList<>();
        time.add("Full time");
        time.add("Part time ");

        ArrayAdapter<String> dataAdapterTime = new ArrayAdapter<>(this, R.layout.item_spinner_time, time);
        dataAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCheckTime.setAdapter(dataAdapterTime);

        List<String> list = new ArrayList<>();
        list.add("Recruitment end over");
        list.add("Still recruitment ");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCheckRecruitment.setAdapter(dataAdapter);
    }


    private void initViews() {
        toolbar = findViewById(R.id.creat_news_toolbar);
        progressDialog = new ProgressDialog(CreatNews.this);

        textIntroduceJob = findViewById(R.id.creat_text_introduce);
        textCompanyAddress = findViewById(R.id.creat_text_company_address);
        textSomeCompanyInfor = findViewById(R.id.creat_text_some_company_infor);
        textCompanyEmail = findViewById(R.id.creat_text_company_email);
        textInformationJob = findViewById(R.id.creat_text_infor_job);

        edtTitle = findViewById(R.id.creat_edt_title);
        edtIntroduceJob = findViewById(R.id.creat_edt_introduce);
        edtCompanyAddress = findViewById(R.id.creat_edt_company_address);
        edtCompanyEmail = findViewById(R.id.creat_edt_company_email);
        edtSomeCompanyInfor = findViewById(R.id.creat_edt_some_company_infor);
        edtInfomationJob = findViewById(R.id.creat_edt_infor_job);
        imLogo = findViewById(R.id.creat_im_image);

        spinnerCheckRecruitment = findViewById(R.id.creat_spiner);
        spinnerCheckTime = findViewById(R.id.creat_spiner2);

        spinnerCheckTime.setOnItemSelectedListener(this);
        spinnerCheckRecruitment.setOnItemSelectedListener(this);

        edtCompanyAddress.addTextChangedListener(this);
        edtIntroduceJob.addTextChangedListener(this);
        edtCompanyEmail.addTextChangedListener(this);
        edtInfomationJob.addTextChangedListener(this);
        edtSomeCompanyInfor.addTextChangedListener(this);

        toolbar.setTitle(getString(R.string.tite_toolbar_news));
        toolbar.setTitleTextColor(Color.DKGRAY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_create_reset:
                edtTitle.getText().clear();
                edtCompanyAddress.getText().clear();
                edtCompanyEmail.getText().clear();
                edtInfomationJob.getText().clear();
                edtSomeCompanyInfor.getText().clear();
                edtIntroduceJob.getText().clear();
                break;
            case R.id.item_create_job:
                upLoadData();
                break;
            case R.id.item_create_logo:
                openFilePhoto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void upLoadData() {
        final String tile = edtTitle.getText().toString();
        final String introduceJob = edtIntroduceJob.getText().toString();
        final String companyAddress = edtCompanyAddress.getText().toString();
        final String companyEmail = edtCompanyEmail.getText().toString().trim();
        final String someCompanyInformation = edtSomeCompanyInfor.getText().toString();
        final String infomationJob = edtInfomationJob.getText().toString();
        final String recruitTime = spinnerCheckRecruitment.getSelectedItem().toString();
        final String jobTime = spinnerCheckTime.getSelectedItem().toString();

        if (introduceJob.isEmpty()){
            textIntroduceJob.setError("enter information job!");
            return;
        }
        if (companyAddress.isEmpty()){
            textCompanyAddress.setError("enter company address!");
            return;
        }
        if (companyEmail.isEmpty()){
            textCompanyEmail.setError("enter company email!");
            return;
        }
        if (someCompanyInformation.isEmpty()){
            textSomeCompanyInfor.setError("enter some company information!");
            return;
        }
        if (infomationJob.isEmpty()){
            textInformationJob.setError("enter introduce job!");
            return;
        }


        progressDialog.setMessage(getString(R.string.dialog_loading));
        progressDialog.show();
        if (uri != null) {
            final StorageReference filePath = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            filePath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    JobModel job = new JobModel(url,tile,introduceJob,companyAddress,jobTime,companyEmail,someCompanyInformation,infomationJob,recruitTime);
                                    String jobId = databaseReference.push().getKey();
                                    databaseReference.child(jobId).setValue(job);
                                }
                            });
                            Toast.makeText(CreatNews.this, "update thanh cong!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreatNews.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "Chọn tệp trước khi đăng", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFilePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            uri = data.getData();
            Glide.with(this).load(uri).error(R.drawable.infotect_jobs).centerCrop().into(imLogo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //  getdata Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    // addTextchange
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length()==0){
            textSomeCompanyInfor.setErrorEnabled(false);
            textSomeCompanyInfor.setError(null);
        }else {
            textSomeCompanyInfor.setErrorEnabled(false);
            textSomeCompanyInfor.setError(null);
        }

        if (charSequence.length()==0){
            textCompanyEmail.setErrorEnabled(false);
            textCompanyEmail.setError(null);
        }else {
            textCompanyEmail.setErrorEnabled(false);
            textCompanyEmail.setError(null);
        }

        if (charSequence.length()==0){
            textIntroduceJob.setErrorEnabled(false);
            textIntroduceJob.setError(null);
        }else {
            textIntroduceJob.setErrorEnabled(false);
            textIntroduceJob.setError(null);
        }

        if (charSequence.length()==0){
            textInformationJob.setErrorEnabled(false);
            textInformationJob.setError(null);
        }else {
            textInformationJob.setErrorEnabled(false);
            textInformationJob.setError(null);
        }

        if (charSequence.length()==0){
            textCompanyAddress.setErrorEnabled(false);
            textCompanyAddress.setError(null);
        }else {
            textCompanyAddress.setErrorEnabled(false);
            textCompanyAddress.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
