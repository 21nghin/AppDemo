package com.t3h.appdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.ListJobAdapter;
import com.t3h.appdemo.model.JobModel;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements ListJobAdapter.ItemListener {

    private RecyclerView rcv;
    private ListJobAdapter adapter;
    private ArrayList<JobModel> data;

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
        adapter = new ListJobAdapter(getContext());
        data = new ArrayList<>();
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
        rcv = getActivity().findViewById(R.id.lv_news);
        rcv.setAdapter(adapter);

    }

    @Override
    public void itemOnclickListener(int position) {
        Intent intent = new Intent(getContext(), DetailFragment.class);
        startActivity(intent);
    }
}
