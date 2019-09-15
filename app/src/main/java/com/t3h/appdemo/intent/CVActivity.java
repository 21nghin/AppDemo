package com.t3h.appdemo.intent;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.t3h.appdemo.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CVActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText edtFullName,edtDateOfBirth,edtPhonenumber,
            edtEmail,edtAddress,edtIntroduce;
    private ImageView cvImage;
    private ImageButton pickImage;
    private Button btnConfirm,btnPickDate;

    private String[] PERMISSION={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar curentDate = Calendar.getInstance();
     private Uri resultUri;
     private File file;
     private int yearOld =0;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_activity);
        if (checkPermissions()){
            initView();
        }
    }

    private void initView() {


        //toolbar
        toolbar = findViewById(R.id.cv_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.cv_title));
        toolbar.setTitleTextColor(Color.GRAY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        //form
        edtFullName = findViewById(R.id.edt_fullname_cv);
        edtDateOfBirth = findViewById(R.id.edt_date_of_birth_cv);
        edtPhonenumber = findViewById(R.id.edt_phone_number_cv);
        edtEmail = findViewById(R.id.edt_email_cv);
        edtAddress = findViewById(R.id.edt_address_vc);
        edtIntroduce = findViewById(R.id.edt_introduce_cv);

        edtFullName.addTextChangedListener(this);
        edtDateOfBirth.addTextChangedListener(this);
        edtPhonenumber.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtAddress.addTextChangedListener(this);
        edtIntroduce.addTextChangedListener(this);
        //image
        cvImage = findViewById(R.id.cimg_avatar);
        pickImage = findViewById(R.id.imgbtn_camera_pick_image);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnPickDate = findViewById(R.id.btn_date_picker);
        //setOnclick
        btnConfirm.setOnClickListener(this);
        pickImage.setOnClickListener(this);
        btnPickDate.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cv_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_cv:{
                //kiem tra tuoi
                String fullName =edtFullName.getText().toString().trim();
                String dateofbirth =edtDateOfBirth.getText().toString().trim();
                String phonenumer = edtPhonenumber.getText().toString().trim();
                String email =  edtEmail.getText().toString().trim();
                String address =edtAddress.getText().toString().trim();
                String introduce =edtIntroduce.getText().toString().trim();
                if (!dateofbirth.isEmpty()){
                    Calendar calendar = Calendar.getInstance();
                    curentDate.getTime();
                    try {
                        calendar.setTime(format.parse(dateofbirth));
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DATE);
                        //yearold
                        if (curentDate.get(Calendar.YEAR)-year>=12&&
                                curentDate.get(Calendar.MONTH)>month){
                            yearOld = curentDate.get(Calendar.YEAR)-year;
                        }else if (curentDate.get(Calendar.YEAR)-year>=12&&
                                curentDate.get(Calendar.MONTH)==month&&
                                curentDate.get(Calendar.DATE)==day)
                        {
                            yearOld = curentDate.get(Calendar.YEAR)-year;
                        }else{
                            yearOld = curentDate.get(Calendar.YEAR)-year-1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    edtDateOfBirth.setError(getString(R.string.erro404dateofbirth));
                }
                if (fullName.isEmpty()){
                    edtFullName.setError(getString(R.string.erro404fullname));
                }
                if (email.isEmpty()){
                    edtEmail.setError(getString(R.string.is_empty_rg_email));
                }
                if (phonenumer.isEmpty()){
                    edtPhonenumber.setError(getString(R.string.error404phonenumber));
                }
                if (phonenumer.length()>11||phonenumer.length()<9&&!phonenumer.isEmpty()){
                    edtPhonenumber.setError(getString(R.string.phonenumber_to_short));
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()&&!email.isEmpty()){
                    edtEmail.setError(getString(R.string.enter_a_valid_address));
                }
                if (address.isEmpty()){
                    edtAddress.setError(getString(R.string.error404address));
                }
                if (!introduce.isEmpty()){
                    edtIntroduce.setError(getString(R.string.error404introduce));
                }


                if (!fullName.isEmpty()&&
                        !dateofbirth.isEmpty()&&
                        !phonenumer.isEmpty()&&
                        !address.isEmpty()&&
                        !introduce.isEmpty()&&
                        !email.isEmpty()&&
                        phonenumer.length()<11
                        &&phonenumer.length()>=9){

                }
                if (yearOld<18){
                    edtDateOfBirth.setError("You are too young");
                }else {
                    //bat dau upload CV len sever
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermissions(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            for (String p:PERMISSION){
                if (checkSelfPermission(p)== PackageManager.PERMISSION_DENIED){
                    requestPermissions(PERMISSION,0);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!checkPermissions()){
            return;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //chọn ảnh
            case R.id.imgbtn_camera_pick_image:{
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;
            }
            //btn upload ảnh
            case R.id.btn_confirm:{

                break;
            }
            case R.id.btn_date_picker:{
                final Calendar dateOfBirth = Calendar.getInstance();
                curentDate.getTime();
                dateOfBirth.set(1999,1,1);
                int day = dateOfBirth.get(Calendar.DATE);
                int month = dateOfBirth.get(Calendar.MONTH);
                int year = dateOfBirth.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                //i:year i1:month i2:day
                                dateOfBirth.set(i,i1,i2);
                                edtDateOfBirth.setText(format.format(dateOfBirth.getTime()));
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                cvImage.setImageURI(resultUri);
                btnConfirm.setVisibility(View.VISIBLE);
                //lấy dường dẫn ảnh
                file = new File(resultUri.getPath());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String fullName =edtFullName.getText().toString().trim();
        String dateofbirth =edtDateOfBirth.getText().toString().trim();
        String phonenumer = edtPhonenumber.getText().toString().trim();
        String email =  edtEmail.getText().toString().trim();
        String address =edtAddress.getText().toString().trim();
        String introduce =edtIntroduce.getText().toString().trim();
        if (!fullName.isEmpty()){
            edtFullName.setError(null);
        }
        if (!dateofbirth.isEmpty()){
            edtDateOfBirth.setError(null);
        }
        if (!phonenumer.isEmpty()){
            edtPhonenumber.setError(null);
        }
        if (!email.isEmpty()){
            edtEmail.setError(null);
        }
        if (!address.isEmpty()){
            edtAddress.setError(null);
        }
        if (!introduce.isEmpty()){
            edtIntroduce.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
