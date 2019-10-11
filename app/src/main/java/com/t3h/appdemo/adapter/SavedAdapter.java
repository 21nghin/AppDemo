package com.t3h.appdemo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.Saved;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedHolder> {

    private ArrayList<Saved> dataSave;
    private Context context;

    public SavedAdapter(ArrayList<Saved> dataSave, Context context) {
        this.dataSave = dataSave;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_saved, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedHolder holder, int position) {

        String pComments = dataSave.get(position).getpComments();
        String pDateNow = dataSave.get(position).getpDateNow();
        final String pId = dataSave.get(position).getpId();
        final String pImage = dataSave.get(position).getpImage();
        String pIntroductJob = dataSave.get(position).getpIntroductJob();
        String pLikes = dataSave.get(position).getpLikes();
        String uImageUrl = dataSave.get(position).getuImageUrl();
        String uName = dataSave.get(position).getuName();
        String pTile = dataSave.get(position).getpTile();
        String pRecruitTime = dataSave.get(position).getpRecruitTime();

        holder.tvSattusJob.setText(pRecruitTime);
        holder.tvTitle.setText(pTile);
        holder.tvNumberComment.setText(pComments);
        holder.tvDate.setText(pDateNow);
        holder.tvIntro.setText(pIntroductJob);
        holder.tvNumberLike.setText(pLikes);
        holder.tvNameUpload.setText(uName);
        Glide.with(context).load(pImage)
                .skipMemoryCache(true)
                .error(R.drawable.infotect_jobs)
                .centerInside()
                .placeholder(R.drawable.infotect_jobs)
                .into(holder.imageView);
        Glide.with(context).load(uImageUrl)
                .skipMemoryCache(true)
                .error(R.drawable.im_account)
                .centerInside()
                .placeholder(R.drawable.im_account)
                .into(holder.civAccountUpload);

        holder.imbPopubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.imbPopubMenu,pId,pImage);
            }
        });
    }

    private void showMoreOptions(ImageButton imbPopubMenu, final String pId, final String pImage) {
        PopupMenu popupMenu = new PopupMenu(context, imbPopubMenu, Gravity.END);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:
                        beginDelete(pId,pImage);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void beginDelete(final String pId, final String pImage) {
        if (pImage.equals("noImage")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có muốn xóa tin dẫ lưu này?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteWithoutImage(pId);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có muốn xóa bản đã lưu này?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteWithImage(pId, pImage);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

            builder.show();
        }
    }

    private void deleteWithoutImage(String pId) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("Saved").orderByChild("pId").equalTo(pId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteWithImage(final String pId, String pImage) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference storRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        storRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query query = FirebaseDatabase.getInstance().getReference("Saved").orderByChild("pId").equalTo(pId);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                }
                                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataSave == null ? 0 : dataSave.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {

        private ImageView imageView ,imComment;
        private LinearLayout ln_item_comment;
        private ImageButton like, share, imbPopubMenu;
        private CircleImageView civAccountUpload;
        private TextView tvComment, tvTitle, tvIntro, tvDate, tvSattusJob, tvNameUpload,
                tvNumberLike, tvNumberShare, tvNumberComment;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);

            ln_item_comment = itemView.findViewById(R.id.ln_item_cmt_save);
            imageView = itemView.findViewById(R.id.imv_image_item_job_save);
            tvTitle = itemView.findViewById(R.id.tv_title_item_job_save);
            tvIntro = itemView.findViewById(R.id.tv_intro_item_job_save);
            tvDate = itemView.findViewById(R.id.tv_date_item_job_save);
            tvSattusJob = itemView.findViewById(R.id.tv_status_item_job_save);
            tvComment = itemView.findViewById(R.id.tv_comment_item_save);

            tvNameUpload = itemView.findViewById(R.id.tv_name_upload_save);
            civAccountUpload = itemView.findViewById(R.id.im_account_upload_save);
            like = itemView.findViewById(R.id.imgbtn_like_item_job_save);
            imbPopubMenu = itemView.findViewById(R.id.popub_menu_save);
            share = itemView.findViewById(R.id.imgbtn_share_item_job_save);
            imComment = itemView.findViewById(R.id.imv_cmt_item_job_save);

            tvNumberLike = itemView.findViewById(R.id.tv_number_like_item_job_save);
            tvNumberShare = itemView.findViewById(R.id.tv_share_item_job_save);
            tvNumberComment = itemView.findViewById(R.id.tv_number_comment_item_job_save);
        }
    }
}
