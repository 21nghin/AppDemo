package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.MessageAdapter;
import com.t3h.appdemo.model.Message;
import com.t3h.appdemo.model.User;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageApp extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView civImage;
    private TextView tvName;
    private TextView tvOnlineOrOffline;
    private EditText edtChat;
    private ImageView imSend;
    private ImageView imLike;
    private Toolbar toolbarMessage;
    private RecyclerView lvMessage;

    private MessageAdapter adapter;
    private List<Message> data;

    private FirebaseUser fireUser;
    private FirebaseAuth fireAuth;
    private DatabaseReference dataRef;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_app);

        initViews();
        loadDataUser();
        setUpRecyclerViewMessage();
    }

    private void setUpRecyclerViewMessage() {
        lvMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        lvMessage.setLayoutManager(linearLayoutManager);
    }

    private void loadDataUser() {
        id = getIntent().getStringExtra("userid");
        fireAuth = FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference("Users").child(id);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvName.setText(user.getName());
                Glide.with(getApplicationContext())
                        .load(user.getImageUrl())
                        .skipMemoryCache(true)
                        .error(R.drawable.im_account)
                        .centerCrop()
                        .into(civImage);

                readDataMessage(fireAuth.getUid(), id, user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initViews() {

        toolbarMessage = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbarMessage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMessage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        civImage = findViewById(R.id.civ_avatar_message);
        tvName = findViewById(R.id.tv_name_message);
        tvOnlineOrOffline = findViewById(R.id.tv_status_message);
        edtChat = findViewById(R.id.edt_chat_message);
        imSend = findViewById(R.id.item_message_chat);
        imLike = findViewById(R.id.item_message_like);
        lvMessage = findViewById(R.id.lv_message);

        imLike.setVisibility(View.VISIBLE);
        imSend.setVisibility(View.INVISIBLE);

        imLike.setOnClickListener(this);
        imSend.setOnClickListener(this);
        edtChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_message_chat:
                fireUser = FirebaseAuth.getInstance().getCurrentUser();
                String msg = edtChat.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fireUser.getUid(), id, msg);
                } else {
                    message("you can't send empty message");
                }
                edtChat.setText("");

                break;
            case R.id.edt_chat_message:
                imSend.setVisibility(View.VISIBLE);
                imLike.setVisibility(View.INVISIBLE);
                break;
            case R.id.item_message_like:
                message("Chat");
                break;
        }
    }

    private void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chat").push().setValue(hashMap);
    }

    private void readDataMessage(final String myid, final String userid, final String imageUrl) {
        data = new ArrayList<>();
        dataRef = FirebaseDatabase.getInstance().getReference("Chat");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot pull : dataSnapshot.getChildren()) {
                    Message message = pull.getValue(Message.class);
                    if (message.getReceiver().equals(myid) && message.getSender().equals(userid)
                            ||
                            message.getReceiver().equals(userid) && message.getSender().equals(myid)) {
                        data.add(message);
                    }
                }
                adapter = new MessageAdapter(MessageApp.this, data, imageUrl);
                lvMessage.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
