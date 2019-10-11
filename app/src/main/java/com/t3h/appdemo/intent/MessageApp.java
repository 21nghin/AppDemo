package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.MessageAdapter;
import com.t3h.appdemo.api.ApiService;
import com.t3h.appdemo.model.Message;
import com.t3h.appdemo.model.User;
import com.t3h.appdemo.notification.Client;
import com.t3h.appdemo.notification.Data;
import com.t3h.appdemo.notification.Response;
import com.t3h.appdemo.notification.Sender;
import com.t3h.appdemo.notification.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageApp extends AppCompatActivity implements View.OnClickListener, TextWatcher, View.OnKeyListener {

    private CircleImageView civImage;
    private TextView tvName;
    private TextView tvOnlineOrOffline;
    private EditText edtChat;
    private ImageView imSend;
    private ImageView imLike;
    private ImageView imOnlile;
    private ImageView imOffline;
    private Toolbar toolbarMessage;
    private RecyclerView lvMessage;

    private MessageAdapter adapter;
    private List<Message> data;

    private FirebaseUser fireUser;
    private FirebaseAuth fireAuth;
    private DatabaseReference dataRef;

    private DatabaseReference databaseReference;
    private FirebaseDatabase fireData;

    private String id;
//    private String status;

    private ApiService api;
    private boolean notify = false;

    private String myUid, myEmail, myName, myImage;
    private String msg;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_message_app);

        initViews();
        //TODO
        checkUserStatus();
        loadUserInfo();
//        -----------------------------
        loadDataUser();

        api = Client.getRetrofit("https://fcm.googleapis.com/").create(ApiService.class);

        setUpRecyclerViewMessage();
    }


    private void loadUserInfo() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(myEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    myName = "" + snapshot.child("name").getValue();
                    myImage = "" + snapshot.child("imageUrl").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myEmail = user.getEmail();
            myUid = user.getUid();

        } else {
            startActivity(new Intent(this, MainApp.class));
            finish();
        }
    }

    private void setUpRecyclerViewMessage() {
        lvMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        lvMessage.setLayoutManager(linearLayoutManager);
    }

    private void loadDataUser() {
        Intent intent = getIntent();
        id = intent.getStringExtra("userid");

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
                if (user.getStatus().equals("online")) {
                    tvOnlineOrOffline.setText("Đang hoạt động");
                    imOffline.setVisibility(View.VISIBLE);
                    imOnlile.setVisibility(View.VISIBLE);
                } else {
                    tvOnlineOrOffline.setText("Không hoạt động");
                    imOffline.setVisibility(View.INVISIBLE);
                    imOnlile.setVisibility(View.INVISIBLE);
                }
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
                startActivity(new Intent(MessageApp.this, ChatApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        civImage = findViewById(R.id.civ_avatar_message);
        tvName = findViewById(R.id.tv_name_message);
        tvOnlineOrOffline = findViewById(R.id.tv_status_message);
        edtChat = findViewById(R.id.edt_chat_message);
        imSend = findViewById(R.id.item_message_chat);
        imLike = findViewById(R.id.item_message_like);
        imOnlile = findViewById(R.id.im_online_message);
        imOffline = findViewById(R.id.im_display_message);
        lvMessage = findViewById(R.id.lv_message);

        imLike.setVisibility(View.VISIBLE);
        imSend.setVisibility(View.INVISIBLE);

        edtChat.addTextChangedListener(this);
        edtChat.setOnKeyListener(this);

        imLike.setOnClickListener(this);
        imSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_message_chat:
                notify = true;
                fireUser = FirebaseAuth.getInstance().getCurrentUser();
                 msg = edtChat.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fireUser.getUid(), id, msg);
                } else {
                    message("you can't send empty message");
                }
                edtChat.setText("");

                break;
            case R.id.item_message_like:
                message("Chat");
                break;
        }
    }

    private void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chat").push().setValue(hashMap);

        //add user to chat
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(fireUser.getUid()).child(id);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotification(receiver, myName, msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String myName, final String msg) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fireUser.getUid(), R.mipmap.ic_launcher, myName + ": " + msg, "New message", id);
                    Sender sender = new Sender(data, token.getToken());
                    api.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(MessageApp.this, "" + response.message(), Toast.LENGTH_SHORT).show();
//                                    if (response.code() == 200) {
//                                        if (response.body().success != 1) {
//                                            message("Failed");
//                                        }
//                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void status(String status) {
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        dataRef = FirebaseDatabase.getInstance().getReference("Users").child(fireUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        dataRef.updateChildren(hashMap);
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        imSend.setVisibility(View.VISIBLE);
        imLike.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            imLike.setVisibility(View.VISIBLE);
            imSend.setVisibility(View.INVISIBLE);
        }
        return false;
    }
}
