package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.Chat2Adapter;
import com.t3h.appdemo.adapter.ChatAdapter;
import com.t3h.appdemo.model.ChatList;
import com.t3h.appdemo.model.Message;
import com.t3h.appdemo.model.User;
import com.t3h.appdemo.notification.Token;
import com.t3h.appdemo.push_data.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatApp extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView civAvatarChat;
    //    private TextView tvNotify;
    private RecyclerView lvChatHorizontal;
    private RecyclerView lvHaveMessage;
    private EditText edtSearchChat;
//    private ImageView imNotification;

    private FirebaseUser mUser;
    private DatabaseReference mDataRef;
    private ValueEventListener mDBListener;

    private String mUID;

    private List<ChatList> chatList;

    private ArrayList<User> mData;
    private ArrayList<User> mData2;
    private ChatAdapter adapter;
    //TODO
    private Chat2Adapter adapter2;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.app_chat);

        initViews();
        loadSearchChatUser();
        initFirebase();
        getData();
        getDataLvChatHorizontal();
        setUPRecyclerViewHaveMessage();
        checkUserStatus();
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void loadSearchChatUser() {
        edtSearchChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchUsers(String s) {
        final FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert fireUser != null;
                    if (!user.getId().equals(fireUser.getUid())) {
                        mData.add(user);
                    }
                }

                adapter = new ChatAdapter(ChatApp.this, mData, true);
                lvChatHorizontal.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                for (DataSnapshot pullDataSnapshot : dataSnapshot.getChildren()) {
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

    private void updateToken(String token) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Tokens");
        Token tk = new Token(token);
        dataRef.child(mUID).setValue(tk);
    }


    private void loadChatList() {
        mData2 = new ArrayList<>();
        mDataRef = FirebaseDatabase.getInstance().getReference("Users");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (ChatList list : chatList) {
                        if (user.getId().equals(list.getId())) {
                            mData2.add(user);
                        }
                    }
                }
                adapter2 = new Chat2Adapter(ChatApp.this, mData2, true);
                lvHaveMessage.setAdapter(adapter2);
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
        adapter = new ChatAdapter(getApplicationContext(), mData, true);
        lvChatHorizontal.setAdapter(adapter);

        mDataRef = FirebaseDatabase.getInstance().getReference("Users");
        mDBListener = mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (edtSearchChat.getText().toString().equals("")) {
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
        edtSearchChat = findViewById(R.id.search_chat);
        civAvatarChat = findViewById(R.id.civ_avatar_chat);
//        tvNotify = findViewById(R.id.tv_notify);
//        imNotification = findViewById(R.id.im_notification);
        lvHaveMessage = findViewById(R.id.lv_have_message);
        lvHaveMessage.setNestedScrollingEnabled(false);
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

    private void status(String status) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDataRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        mDataRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status(getString(R.string.user_online));
        checkUserStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        status(getString(R.string.user_offline));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void checkUserStatus() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUID = mUser.getUid();

            SharedPreferences sp = getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
        } else {
            startActivity(new Intent(ChatApp.this, MainApp.class));
            finish();
        }
    }
}
