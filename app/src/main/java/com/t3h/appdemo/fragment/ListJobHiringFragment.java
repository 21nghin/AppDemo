package com.t3h.appdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.ListJobAdapter;
import com.t3h.appdemo.model.JobModel;

import java.util.ArrayList;

public class ListJobHiringFragment extends Fragment implements ListJobAdapter.ItemListener {
    private RecyclerView rcv;
    private ListJobAdapter adapter;
    private ArrayList<JobModel> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_job,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initData() {
        data = new ArrayList<JobModel>();
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
        data.add(new JobModel("Mua nha","adasdadadad","12/2/2123","01","01","01"));
    }

    private void initView() {

        adapter = new ListJobAdapter(getContext());
        adapter.setData(data);
        adapter.setListener(this);
        rcv = getActivity().findViewById(R.id.rcv);
        rcv.setAdapter(adapter);
    }

    @Override
    public void itemOnclickListener(int position) {
        Intent intent = new Intent(getContext(), DetailFragment.class);
        startActivity(intent);
    }
}
