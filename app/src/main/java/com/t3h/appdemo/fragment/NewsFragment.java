package com.t3h.appdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class NewsFragment extends Fragment implements ListJobAdapter.ItemListener {

    private RecyclerView rcv;
    private ListJobAdapter adapter;
    private ArrayList<JobModel> data;

    private ValueEventListener mDBListener;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();

    }

    private void initData() {
        firebaseStorage =FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("JobModel");
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot pull:dataSnapshot.getChildren()) {
                    JobModel job = pull.getValue(JobModel.class);
                    job.setId(pull.getKey());
                    data.add(job);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {

        data = new ArrayList<>();
        adapter = new ListJobAdapter(getContext());
        adapter.setData(data);
        adapter.setListener(this);
        rcv = getActivity().findViewById(R.id.lv_news);
        rcv.setAdapter(adapter);

    }

    @Override
    public void itemOnclickListener(int position) {
        Intent intent = new Intent(getContext(), DetailNewsApp.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }
}
