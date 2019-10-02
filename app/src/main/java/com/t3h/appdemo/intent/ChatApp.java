package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.ChatAdapter;
import com.t3h.appdemo.model.ChatList;
import com.t3h.appdemo.model.Message;
import com.t3h.appdemo.model.User;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatApp extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView civAvatarChat;
    private TextView tvNotify;
    private RecyclerView lvChatHorizontal;
    private RecyclerView lvHaveMessage;

    private FirebaseUser mUser;
    private DatabaseReference mDataRef;
    private ValueEventListener mDBListener;

    private List<ChatList> chatList;

    private ArrayList<User> mData;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_chat);

        initViews();
        initFirebase();
        getData();
        getDataLvChatHorizontal();
        setUPRecyclerViewHaveMessage();
    }

    private void setUPRecyclerViewHaveMessage() {
        lvHaveMessage.setHasFixedSize(true);
        lvHaveMessage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        chatList = new ArrayList<>();

        mDataRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(mUser.getUid());
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot pullDataSnapshot:dataSnapshot.getChildren()) {
                    ChatList list = pullDataSnapshot.getValue(ChatList.class);
                    chatList.add(list);
                }

                loadChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void loadChatList() {
        mData = new ArrayList<>();
        mDataRef = FirebaseDatabase.getInstance().getReference("Users");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (ChatList list : chatList){
                        if (user.getId().equals(list.getId())){
                            mData.add(user);
                        }
                    }
                }
                adapter = new ChatAdapter(ChatApp.this,mData,true);
                lvHaveMessage.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void getDataLvChatHorizontal() {
        lvChatHorizontal.setHasFixedSize(true);
        LinearLayoutManager lvHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        lvChatHorizontal.setLayoutManager(lvHorizontal);

        mData = new ArrayList<>();
        adapter = new ChatAdapter(getApplicationContext(), mData,false);
        lvChatHorizontal.setAdapter(adapter);

        mDataRef = FirebaseDatabase.getInstance().getReference("Users");
        mDBListener = mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();

                for (DataSnapshot pull : dataSnapshot.getChildren()) {
                    User user = pull.getValue(User.class);
                    assert user != null;
                    assert mUser != null;
                    if (!user.getEmail().equals(mUser.getEmail())) {
                        mData.add(user);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






    private void initFirebase() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDataRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
    }

    private void getData() {
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext())
                        .load(user.getImageUrl())
                        .centerCrop()
                        .error(R.drawable.ic_account_circle)
                        .skipMemoryCache(true)
                        .into(civAvatarChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initViews() {
        civAvatarChat = findViewById(R.id.civ_avatar_chat);
        tvNotify = findViewById(R.id.tv_notify);
        lvHaveMessage = findViewById(R.id.lv_have_message);
        lvChatHorizontal = findViewById(R.id.lv_chat_horizontal);

        civAvatarChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_avatar_chat:
                startActivity(new Intent(getApplicationContext(), UserApp.class));
                break;
        }
    }

    private void status(String status){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDataRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        mDataRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status(getString(R.string.user_online));
    }

    @Override
    protected void onPause() {
        super.onPause();
        status(getString(R.string.user_offline));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
