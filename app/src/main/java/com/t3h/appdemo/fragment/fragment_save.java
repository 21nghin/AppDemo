package com.t3h.appdemo.fragment;

import android.content.Intent;
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
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.ListJobAdapter;
import com.t3h.appdemo.intent.DetailNewsApp;
import com.t3h.appdemo.model.PostJob;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;

public class fragment_save extends Fragment implements ListJobAdapter.ItemListener {
    private RecyclerView rcvSave;
    private ListJobAdapter adapter;
    private ArrayList<PostJob> data;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_save,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    //dua data vao day
    private void initData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot pull : dataSnapshot.getChildren()) {
                    PostJob job = pull.getValue(PostJob.class);
                    job.setpId(pull.getKey());
                    data.add(job);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    //kiểm tra đã lưu chưa
    //phải viết trong popupmenu để khi ấn vào save nó thông báo ra
    private void checkSave(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Save");
    }


    private void initView() {
        rcvSave = getActivity().findViewById(R.id.rcv_save);
        progressBar = getActivity().findViewById(R.id.progress_load_save);
        data = new ArrayList<>();
        adapter = new ListJobAdapter(getContext(),data);
        adapter.setListener(this);
        rcvSave.setAdapter(adapter);
    }

    @Override
    public void itemOnclickListener(int position) {
        PostJob clickItemJob = data.get(position);
        String[] readData = {clickItemJob.getpImage()
                , clickItemJob.getpTile()
                , clickItemJob.getpIntroductJob()
                , clickItemJob.getpCompanyAddress()
                , clickItemJob.getpJobTime()
                , clickItemJob.getpCompanyEmail()
                , clickItemJob.getpSomeCompanyInformation()
                , clickItemJob.getpInfomationJob()
                , clickItemJob.getpRecruitTime()
                , clickItemJob.getpDateNow()
        };
        openJobExtra(readData);
    }

    private void openJobExtra(String[] data) {
        Intent readData = new Intent(getContext(), DetailNewsApp.class);
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
}
