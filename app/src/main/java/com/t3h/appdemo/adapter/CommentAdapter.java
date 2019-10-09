package com.t3h.appdemo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.Comments;

import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context context;
    private ArrayList<Comments> dataComments;
    private String myUid, postId;

    public CommentAdapter(Context context, ArrayList<Comments> dataComments, String myUid, String postId) {
        this.context = context;
        this.dataComments = dataComments;
        this.myUid = myUid;
        this.postId = postId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        final String uid = dataComments.get(position).getUid();
        String name = dataComments.get(position).getuName();
        String email = dataComments.get(position).getuEmail();
        String image = dataComments.get(position).getuImageUrl();
        final String cid = dataComments.get(position).getcId();
        String comment = dataComments.get(position).getComment();
        String dateTime = dataComments.get(position).getTimeStamp();

        holder.tvTime.setText(dateTime);
        holder.tvContentCmt.setText(comment);
        holder.tvName.setText(name);
        Glide.with(context).load(image)
                .centerCrop()
                .skipMemoryCache(true)
                .error(R.drawable.im_account)
                .into(holder.civImage);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (myUid.equals(uid)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Delete comment");
                    builder.setMessage("Are you sure to delete this comment?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteComment(cid);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
                return false;
            }
        });

    }

    private void deleteComment(String cid) {
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        dataRef.child("Comments").child(cid).removeValue();
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comments = "" + dataSnapshot.child("pComments").getValue();
                int newCommentVal = Integer.parseInt(comments) - 1;
                dataRef.child("pComments").setValue("" + newCommentVal);
                Toast.makeText(context, "Delete comment successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Can't delete other's comment...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataComments == null ? 0 : dataComments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvContentCmt, tvTime;
        private CircleImageView civImage;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_comment_item);
            tvContentCmt = itemView.findViewById(R.id.tv_content_comment_item);
            tvTime = itemView.findViewById(R.id.tv_hour_ago);
            civImage = itemView.findViewById(R.id.civ_comment);
        }
    }
}
