package com.t3h.appdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.NotificationAdapter;
import com.t3h.appdemo.model.Comments;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private RecyclerView lvNotification;
    private TextView tvIsEmptyNotifi;
    private ArrayList<Comments> dataCom;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        initViews();
    }

    private void initViews() {
        lvNotification = getActivity().findViewById(R.id.lv_notifi);
        tvIsEmptyNotifi = getActivity().findViewById(R.id.is_empty_notifi);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setReverseLayout(true);
        lvNotification.setLayoutManager(layoutManager);

        dataCom = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(),dataCom);
        lvNotification.setAdapter(adapter);
    }
}
