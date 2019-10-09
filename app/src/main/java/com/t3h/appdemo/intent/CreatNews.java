package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatNews extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "CreatNews";
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
    private CircleImageView civImageAccount;

    private DatabaseReference databaseReference;
    private FirebaseAuth fireAuth;
    private FirebaseDatabase fireData;

    private String email;
    private String name;
    private String uid;
    private String imageUrl;
    private Uri image_uri;
    private Calendar calendar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_creat_news);

        initViews();
        checkUserStatus();
        getDataSpinner();
        loadCurentUserImage();
    }

    private void loadCurentUserImage() {
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
                        .into(civImageAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CreatNews.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    name = "" + snapshot.child("name").getValue();
                    email = "" + snapshot.child("email").getValue();
                    imageUrl = "" + snapshot.child("imageUrl").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .skipMemoryCache(true)
                .error(R.drawable.im_account)
                .centerCrop()
                .into(civImageAccount);

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
        progressDialog = new ProgressDialog(this);

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

        civImageAccount = findViewById(R.id.im_create_upload);
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

        calendar = Calendar.getInstance();

        final String tile = edtTitle.getText().toString();
        final String introduceJob = edtIntroduceJob.getText().toString();
        final String companyAddress = edtCompanyAddress.getText().toString();
        final String companyEmail = edtCompanyEmail.getText().toString().trim();
        final String someCompanyInformation = edtSomeCompanyInfor.getText().toString();
        final String infomationJob = edtInfomationJob.getText().toString();
        final String recruitTime = spinnerCheckRecruitment.getSelectedItem().toString();
        final String jobTime = spinnerCheckTime.getSelectedItem().toString();
        final String dateNow = DateFormat.getDateInstance().format(calendar.getTime());

        if (introduceJob.isEmpty()) {
            textIntroduceJob.setError("enter information job!");
            return;
        }
        if (companyAddress.isEmpty()) {
            textCompanyAddress.setError("enter company address!");
            return;
        }
        if (companyEmail.isEmpty()) {
            textCompanyEmail.setError("enter company email!");
            return;
        }
        if (someCompanyInformation.isEmpty()) {
            textSomeCompanyInfor.setError("enter some company information!");
            return;
        }
        if (infomationJob.isEmpty()) {
            textInformationJob.setError("enter introduce job!");
            return;
        }


        if (image_uri == null) {
            uploadData(tile, introduceJob,
                    companyAddress, companyEmail,
                    someCompanyInformation,
                    infomationJob, recruitTime,
                    jobTime, dateNow, "noImage");
        }else {
            uploadData(tile, introduceJob,
                    companyAddress, companyEmail,
                    someCompanyInformation,
                    infomationJob, recruitTime,
                    jobTime, dateNow, String.valueOf(image_uri));
        }
    }


    private void uploadData(final String tile, final String introduceJob, final String companyAddress, final String companyEmail, final String someCompanyInformation, final String infomationJob, final String recruitTime, final String jobTime, final String dateNow, final String uri) {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePath = "Posts/" + "post_" + timeStamp;
        if (!uri.equals("noImage")) {
            StorageReference storRef = FirebaseStorage.getInstance().getReference().child(filePath);
            storRef.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String dowloadUri = uriTask.getResult().toString();
                            if (uriTask.isSuccessful()) {
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid", uid);
                                hashMap.put("uName", name);
                                hashMap.put("uEmail", email);
                                hashMap.put("uImageUrl", imageUrl);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pTile", tile);
                                hashMap.put("pIntroductJob", introduceJob);
                                hashMap.put("pCompanyAddress", companyAddress);
                                hashMap.put("pCompanyEmail", companyEmail);
                                hashMap.put("pSomeCompanyInformation", someCompanyInformation);
                                hashMap.put("pLikes","0");
                                hashMap.put("pComments","0");
                                hashMap.put("pInfomationJob", infomationJob);
                                hashMap.put("pRecruitTime", recruitTime);
                                hashMap.put("pJobTime", jobTime);
                                hashMap.put("pDateNow", dateNow);
                                hashMap.put("pImage", dowloadUri);

                                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts");
                                dataRef.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Toast.makeText(CreatNews.this, "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                                                edtTitle.setText("");
                                                edtIntroduceJob.setText("");
                                                edtCompanyAddress.setText("");
                                                edtCompanyEmail.setText("");
                                                edtSomeCompanyInfor.setText("");
                                                edtInfomationJob.setText("");
                                                imLogo.setImageURI(null);
                                                image_uri = null;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(CreatNews.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreatNews.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uid", uid);
            hashMap.put("uName", name);
            hashMap.put("uEmail", email);
            hashMap.put("uImageUrl", imageUrl);
            hashMap.put("pId", timeStamp);
            hashMap.put("pTile", tile);
            hashMap.put("pIntroductJob", introduceJob);
            hashMap.put("pCompanyAddress", companyAddress);
            hashMap.put("pCompanyEmail", companyEmail);
            hashMap.put("pSomeCompanyInformation", someCompanyInformation);
            hashMap.put("pLikes","0");
            hashMap.put("pComments","0");
            hashMap.put("pInfomationJob", infomationJob);
            hashMap.put("pRecruitTime", recruitTime);
            hashMap.put("pJobTime", jobTime);
            hashMap.put("pDateNow", dateNow);
            hashMap.put("pImage", "noImage");

            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts");
            dataRef.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(CreatNews.this, "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                            edtTitle.setText("");
                            edtIntroduceJob.setText("");
                            edtCompanyAddress.setText("");
                            edtCompanyEmail.setText("");
                            edtSomeCompanyInfor.setText("");
                            edtInfomationJob.setText("");
                            imLogo.setImageURI(null);
                            image_uri = null;
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreatNews.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void openFilePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            image_uri = data.getData();
            Glide.with(this).load(image_uri).error(R.drawable.infotect_jobs).centerCrop().into(imLogo);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            textSomeCompanyInfor.setErrorEnabled(false);
            textSomeCompanyInfor.setError(null);
        } else {
            textSomeCompanyInfor.setErrorEnabled(false);
            textSomeCompanyInfor.setError(null);
        }

        if (charSequence.length() == 0) {
            textCompanyEmail.setErrorEnabled(false);
            textCompanyEmail.setError(null);
        } else {
            textCompanyEmail.setErrorEnabled(false);
            textCompanyEmail.setError(null);
        }

        if (charSequence.length() == 0) {
            textIntroduceJob.setErrorEnabled(false);
            textIntroduceJob.setError(null);
        } else {
            textIntroduceJob.setErrorEnabled(false);
            textIntroduceJob.setError(null);
        }

        if (charSequence.length() == 0) {
            textInformationJob.setErrorEnabled(false);
            textInformationJob.setError(null);
        } else {
            textInformationJob.setErrorEnabled(false);
            textInformationJob.setError(null);
        }

        if (charSequence.length() == 0) {
            textCompanyAddress.setErrorEnabled(false);
            textCompanyAddress.setError(null);
        } else {
            textCompanyAddress.setErrorEnabled(false);
            textCompanyAddress.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        } else {
            startActivity(new Intent(this, MainApp.class));
            finish();
        }
    }
}
