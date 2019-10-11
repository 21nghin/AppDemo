package com.t3h.appdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.t3h.appdemo.intent.DetailNewsApp;
import com.t3h.appdemo.model.Comments;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifiHolder> {

    private Context context;
    private ArrayList<Comments> dataNo;

    private String theLastComments;

    public NotificationAdapter(Context context, ArrayList<Comments> dataNo) {
        this.context = context;
        this.dataNo = dataNo;
    }

    @NonNull
    @Override
    public NotifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotifiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiHolder holder, int position) {
        Comments comments = dataNo.get(position);
        holder.bindData(comments);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetailNewsApp.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataNo == null ? 0 : dataNo.size();
    }

    public class NotifiHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDateTime;
        private CircleImageView civImage;

        public NotifiHolder(@NonNull View itemView) {
            super(itemView);

            tvDateTime = itemView.findViewById(R.id.tv_date_time_notif);
            tvName = itemView.findViewById(R.id.tv_notif_name);
            civImage = itemView.findViewById(R.id.im_notif);
        }

        public void bindData(Comments comments) {
            tvName.setText(comments.getuName());
            tvDateTime.setText(comments.getTimeStamp());
            Glide.with(context).load(comments.getuImageUrl())
                    .skipMemoryCache(true)
                    .error(R.drawable.im_account)
                    .centerCrop()
                    .into(civImage);
        }
    }

    private void lastComment(String uid, TextView last_cmt) {
        theLastComments = "default";
        final FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Comments");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comments comments = snapshot.getValue(Comments.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
