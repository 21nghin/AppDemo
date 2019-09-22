package com.t3h.appdemo.fragment;

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
import com.t3h.appdemo.adapter.CmtAdapter;
import com.t3h.appdemo.model.CmtModel;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    private RecyclerView rcvCmt;
    private CmtAdapter adapter;
    private ArrayList<CmtModel> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        adapter = new CmtAdapter(getContext());
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        adapter.setData(data);
    }

    private void initView() {
        rcvCmt = getActivity().findViewById(R.id.lv_comment);
        rcvCmt.setAdapter(adapter);
    }

}
