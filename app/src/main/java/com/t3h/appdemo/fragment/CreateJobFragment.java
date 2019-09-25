package com.t3h.appdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.t3h.appdemo.R;

import java.util.ArrayList;
import java.util.List;

public class CreateJobFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerCheckRecruitment;
    private Spinner spinnerCheckTime;
    private EditText edtTitle;
    private EditText edtIntroduceJob;
    private EditText edtCompanyAddress;
    private EditText edtCompanyEmail;
    private EditText edtSomeCompanyInfor;
    private EditText edtInfomationJob;
    private ImageView imLogo;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creat_news,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getDataSpinner();
    }

    private void getDataSpinner() {
        List<String> time = new ArrayList<>();
        time.add("Full time");
        time.add("Part time ");

        ArrayAdapter<String> dataAdapterTime = new ArrayAdapter<>(getContext(), R.layout.item_spinner_time, time);
        dataAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCheckTime.setAdapter(dataAdapterTime);

        List<String> list = new ArrayList<>();
        list.add("Recruitment end over");
        list.add("Still recruitment ");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCheckRecruitment.setAdapter(dataAdapter);
    }

    private void initViews() {
        edtTitle = getActivity().findViewById(R.id.creat_edt_title);
        edtIntroduceJob = getActivity().findViewById(R.id.creat_edt_introduce);
        edtCompanyAddress = getActivity().findViewById(R.id.creat_edt_company_address);
        edtCompanyEmail = getActivity().findViewById(R.id.creat_edt_company_email);
        edtSomeCompanyInfor = getActivity().findViewById(R.id.creat_edt_some_company_infor);
        edtInfomationJob = getActivity().findViewById(R.id.creat_edt_infor_job);
        imLogo = getActivity().findViewById(R.id.creat_im_image);
        toolbar = getActivity().findViewById(R.id.creat_news_toolbar);



        spinnerCheckRecruitment = getActivity().findViewById(R.id.creat_spiner);
        spinnerCheckTime = getActivity().findViewById(R.id.creat_spiner2);
        edtTitle = getActivity().findViewById(R.id.creat_edt_title);

        spinnerCheckTime.setOnItemSelectedListener(this);
        spinnerCheckRecruitment.setOnItemSelectedListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_create_news,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
