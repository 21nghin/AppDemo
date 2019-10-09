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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.t3h.appdemo.model.PostJob;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;

public class NewsFragment extends Fragment{

    private RecyclerView rcv;
    private ListJobAdapter adapter;
    private ArrayList<PostJob> data;

    private ValueEventListener mDBListener;
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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initView() {
        rcv = getActivity().findViewById(R.id.lv_news);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        rcv.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        adapter = new ListJobAdapter(getContext(),data);
        rcv.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }
}
