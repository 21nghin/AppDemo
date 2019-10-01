package com.t3h.appdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.appdemo.R;

import com.t3h.appdemo.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private Context mContext;
    private ArrayList<User> data;
    private ItemListener listener;

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    public ChatAdapter(Context mContext, ArrayList<User> data) {
        this.mContext = mContext;
        this.data = data;
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
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ItemOnclickListener(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CircleImageView civImage;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_item_name_chat_horizontal);
            civImage = itemView.findViewById(R.id.civ_item_avatar_chat_horizontal);
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

    public interface ItemListener {
        void ItemOnclickListener(int position);
    }
}
