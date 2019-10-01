package com.t3h.appdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private static final int MSG_TYPE_RIGHT = 1;
    private static final int MSG_TYPE_LEFT = 0;
    private Context mContext;
    private List<Message> data;
    private String imageUrl;

    private FirebaseUser fireUser;

    public MessageAdapter(Context mContext, List<Message> data, String imageUrl) {
        this.mContext = mContext;
        this.data = data;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = data.get(position);
        holder.bindData(message);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        private TextView tvShowRight;
        private CircleImageView imImage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            tvShowRight = itemView.findViewById(R.id.tv_show_chat);
            imImage = itemView.findViewById(R.id.im_image_chat);
        }

        public void bindData(Message item) {
            tvShowRight.setText(item.getMessage());
            Glide.with(mContext)
                    .load(imageUrl)
                    .skipMemoryCache(true)
                    .error(R.drawable.im_account)
                    .centerCrop()
                    .into(imImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        if (data.get(position).getSender().equals(fireUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
