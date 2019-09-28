package com.t3h.appdemo.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.ListJobAdapter;
import com.t3h.appdemo.intent.DetailNewsApp;
import com.t3h.appdemo.model.JobModel;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements ListJobAdapter.ItemListener {

    private RecyclerView rcv;
    private ListJobAdapter adapter;
    private ArrayList<JobModel> data;

    private ValueEventListener mDBListener;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = getActivity().findViewById(R.id.progress_load_news);
        ProgressBar idProgress = new android.widget.ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        idProgress.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        initView();
        initData();

    }

    private void initData() {
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("JobModel");
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot pull : dataSnapshot.getChildren()) {
                    JobModel job = pull.getValue(JobModel.class);
                    job.setId(pull.getKey());
                    data.add(job);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void itemOnclickListener(int position) {
        JobModel clickItemJob = data.get(position);
        String[] readData = {clickItemJob.getImage()
                , clickItemJob.getTitle()
                , clickItemJob.getIntroduceJob()
                , clickItemJob.getCompanyAddress()
                , clickItemJob.getJobTime()
                , clickItemJob.getCompanyEmail()
                , clickItemJob.getSomeCompanyInformation()
                , clickItemJob.getInfomationJob()
                , clickItemJob.getRecruiTime()
                , clickItemJob.getDate()
        };
        openJobExtra(readData);
    }

    private void openJobExtra(String[] data) {
        Intent readData = new Intent(getContext(),DetailNewsApp.class);
        readData.putExtra(Const.IMAGE_KEY,data[0]);
        readData.putExtra(Const.TITLE_KEY,data[1]);
        readData.putExtra(Const.INTRDUCE_JOB_KEY,data[2]);
        readData.putExtra(Const.COMPANY_ADDRESS_KEY,data[3]);
        readData.putExtra(Const.JOB_TIME_KEY,data[4]);
        readData.putExtra(Const.COMPANY_EMAIL_KEY,data[5]);
        readData.putExtra(Const.SOME_COMPANY_INFOMATION_KEY,data[6]);
        readData.putExtra(Const.INFOMATION_JOB_KEY,data[7]);
        readData.putExtra(Const.RECRUI_TIME_KEY,data[8]);
        readData.putExtra(Const.DATE_NOW_KEY,data[9]);
        startActivity(readData);
    }

    private void initView() {

        rcv = getActivity().findViewById(R.id.lv_news);

        data = new ArrayList<>();
        adapter = new ListJobAdapter(getContext());
        adapter.setData(data);
        adapter.setListener(this);
        rcv.setAdapter(adapter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }
}
