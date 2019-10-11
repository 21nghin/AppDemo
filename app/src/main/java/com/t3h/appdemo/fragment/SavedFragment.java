package com.t3h.appdemo.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.SavedAdapter;
import com.t3h.appdemo.model.Saved;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private RecyclerView rcvSave;
    private SavedAdapter adapter;
    private ArrayList<Saved> data;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Saved");
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot pull : dataSnapshot.getChildren()) {
                    Saved save = pull.getValue(Saved.class);
                    save.setpId(pull.getKey());
                    data.add(save);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //kiểm tra đã lưu chưa
    //phải viết trong popupmenu để khi ấn vào save nó thông báo ra
    private void checkSave(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Saves");
    }


    private void initView() {
        rcvSave = getActivity().findViewById(R.id.rcv_save);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        rcvSave.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        adapter = new SavedAdapter(data,getContext());
        rcvSave.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }
}
