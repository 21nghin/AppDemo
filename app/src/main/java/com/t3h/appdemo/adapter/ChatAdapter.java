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

import com.t3h.appdemo.R;

import com.t3h.appdemo.intent.MessageApp;
import com.t3h.appdemo.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private Context mContext;
    private ArrayList<User> data;
    private boolean isChat;

    public ChatAdapter(Context mContext, ArrayList<User> data, boolean isChat) {
        this.mContext = mContext;
        this.data = data;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_horizontal, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, final int position) {
        final User user = data.get(position);
        holder.bindData(user);

        if (isChat){
            if (user.getStatus().equals("online")){
                holder.imOnline.setVisibility(View.VISIBLE);
                holder.imDisplay.setVisibility(View.VISIBLE);
            }else {
                holder.imOnline.setVisibility(View.INVISIBLE);
                holder.imDisplay.setVisibility(View.INVISIBLE);
            }
        }else {
            holder.imOnline.setVisibility(View.INVISIBLE);
            holder.imDisplay.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageApp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CircleImageView civImage;
        private ImageView imOnline;
        private ImageView imDisplay;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_item_name_chat_horizontal);
            civImage = itemView.findViewById(R.id.civ_item_avatar_chat_horizontal);
            imOnline = itemView.findViewById(R.id.online_item_chat_icon);
            imDisplay = itemView.findViewById(R.id.online_item_display_icon);
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

}
