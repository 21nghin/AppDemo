package com.t3h.appdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.intent.MessageApp;
import com.t3h.appdemo.model.Message;
import com.t3h.appdemo.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat2Adapter extends RecyclerView.Adapter<Chat2Adapter.Chat2Holder> {

    private Context mContext;
    private ArrayList<User> data;
    private boolean isChat;

    private String theLastMessage;

    public Chat2Adapter(Context mContext, ArrayList<User> data, boolean isChat) {
        this.mContext = mContext;
        this.data = data;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public Chat2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
        return new Chat2Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat2Holder holder, int position) {
        final User user = data.get(position);
        holder.bindData(user);

        if (isChat){
            lastMessage(user.getId(),holder.tvContentChat);
        }else {
            holder.tvContentChat.setVisibility(View.INVISIBLE);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.imOnline.setVisibility(View.VISIBLE);
                holder.imDisplay.setVisibility(View.VISIBLE);
            } else {
                holder.imOnline.setVisibility(View.INVISIBLE);
                holder.imDisplay.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.imOnline.setVisibility(View.INVISIBLE);
            holder.imDisplay.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageApp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", user.getId());
//                intent.putExtra("status",user.getStatus());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class Chat2Holder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CircleImageView civImage;
        private ImageView imOnline;
        private ImageView imDisplay;
        private TextView tvContentChat;

        public Chat2Holder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_item_name_chat);
            civImage = itemView.findViewById(R.id.civ_item_avatar_chat);
            imOnline = itemView.findViewById(R.id.online_item_chat_icon);
            imDisplay = itemView.findViewById(R.id.online_item_display_icon);
            tvContentChat = itemView.findViewById(R.id.tv_item_content_chat);
        }

        public void bindData(User item) {
            tvName.setText(item.getName());
            Glide.with(mContext)
                    .load(item.getImageUrl())
                    .skipMemoryCache(true)
                    .centerCrop()
                    .error(R.drawable.im_account)
                    .into(civImage);
        }
    }

    private void lastMessage(final String userid, final TextView tvLastMessage) {
        theLastMessage = "default";
        final FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Chat");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (fireUser != null && message != null) {
                        if (message.getReceiver().equals(fireUser.getUid()) && message.getSender().equals(userid)
                                || message.getReceiver().equals(userid) && message.getSender().equals(fireUser.getUid())) {
                            theLastMessage = message.getMessage();
                        }
                    }
                }
                switch (theLastMessage) {
                    case "default":
                        tvLastMessage.setText("No message");
                        break;
                    default:
                        tvLastMessage.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
