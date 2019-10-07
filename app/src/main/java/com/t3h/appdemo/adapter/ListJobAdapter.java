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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.PostJob;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListJobAdapter extends RecyclerView.Adapter<ListJobAdapter.RcvHolder> {
    private ArrayList<PostJob> data;
    private ItemListener listener;

    private Boolean checklike = false;
    private Boolean checkShare = false;

    private String myUid;

    private Context context;


    public ListJobAdapter(Context context, ArrayList<PostJob> data) {
        this.data = data;
        this.context = context;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false);
        return new RcvHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RcvHolder holder, final int position) {

        final String uid = data.get(position).getUid();
        String uEmail = data.get(position).getuEmail();
        String uName = data.get(position).getuName();
        String uImageUrl = data.get(position).getuImageUrl();
        final String pId = data.get(position).getpId();
        String pTitle = data.get(position).getpTile();
        String pIntroductJob = data.get(position).getpIntroductJob();
        final String pImage = data.get(position).getpImage();
        String pDateNow = data.get(position).getpDateNow();
        String pSomeCompany = data.get(position).getpSomeCompanyInformation();
        String pJobTime = data.get(position).getpJobTime();
        String pRecruitTime = data.get(position).getpRecruitTime();
        String pInfomation = data.get(position).getpInfomationJob();
        String pCompanyAddress = data.get(position).getpCompanyAddress();
        String pCompanyEmail = data.get(position).getpCompanyEmail();

        holder.tvNameUpload.setText(uName);
        holder.tvTitle.setText(pTitle);
        holder.tvSattusJob.setText(pRecruitTime);
        holder.tvIntro.setText(pIntroductJob);
        holder.tvDate.setText(pDateNow);

        try {
            Glide.with(context).load(uImageUrl)
                    .skipMemoryCache(true)
                    .error(R.drawable.im_account)
                    .centerCrop()
                    .into(holder.civAccountUpload);
        } catch (Exception e) {

        }
        try {
            Glide.with(context).load(pImage)
                    .skipMemoryCache(true)
                    .error(R.drawable.media_img)
                    .centerCrop()
                    .into(holder.imageView);
        } catch (Exception e) {

        }

        holder.imbPopubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.imbPopubMenu, uid, myUid, pId, pImage);
            }
        });

        if (listener != null) {
            holder.ln_item_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemOnclickListener(position);
                }
            });
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checklike) {
                    holder.like.setImageResource(R.drawable.ic_like);
                    checklike = false;
                } else {
                    holder.like.setImageResource(R.drawable.ic_likedefault);
                    checklike = true;
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showMoreOptions(ImageButton imbPopubMenu, String uid, String myUid, final String pId, final String pImage) {
        PopupMenu popupMenu = new PopupMenu(context, imbPopubMenu, Gravity.END);
        if (uid.equals(myUid)) {
            imbPopubMenu.setVisibility(View.VISIBLE);
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
        } else {
            imbPopubMenu.setVisibility(View.INVISIBLE);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    beginDelete(pId, pImage);
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void beginDelete(final String pId, final String pImage) {
        if (pImage.equals("noImage")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có muốn xóa bản tin này?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteWithoutImage(pId);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có muốn xóa bản tin này?")
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

    private void deleteWithImage(final String pId, String pImage) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        StorageReference storRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        storRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
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

    private void deleteWithoutImage(String pId) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
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

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class RcvHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, comment;
        private LinearLayout ln_item_comment;
        private ImageButton like, share, imbPopubMenu;
        private CircleImageView civAccountUpload;
        private TextView tvTitle, tvIntro, tvDate, tvSattusJob, tvNameUpload,
                tvNumberLike, tvNumberShare, tvNumberComment;

        public RcvHolder(@NonNull View itemView) {
            super(itemView);
            ln_item_comment = itemView.findViewById(R.id.ln_item_cmt);
            imageView = itemView.findViewById(R.id.imv_image_item_job);
            tvTitle = itemView.findViewById(R.id.tv_title_item_job);
            tvIntro = itemView.findViewById(R.id.tv_intro_item_job);
            tvDate = itemView.findViewById(R.id.tv_date_item_job);
            tvSattusJob = itemView.findViewById(R.id.tv_status_item_job);

            tvNameUpload = itemView.findViewById(R.id.tv_name_upload);
            civAccountUpload = itemView.findViewById(R.id.im_account_upload);
            like = itemView.findViewById(R.id.imgbtn_like_item_job);
            comment = itemView.findViewById(R.id.imgbtn_cmt_item_cmt);
            imbPopubMenu = itemView.findViewById(R.id.popub_menu);
            share = itemView.findViewById(R.id.imgbtn_share_item_job);

            tvNumberLike = itemView.findViewById(R.id.tv_number_like_item_job);
            tvNumberShare = itemView.findViewById(R.id.tv_number_comment_item_job);
            tvNumberComment = itemView.findViewById(R.id.tv_number_share_item_job);
        }

        public void bindData(PostJob item) {
            tvTitle.setText(item.getpTile());
            tvIntro.setText(item.getpIntroductJob());
            Glide.with(imageView).load(item.getpImage())
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.drawable.media_img)
                    .error(R.drawable.media_img)
                    .into(imageView);
            tvSattusJob.setText(item.getpRecruitTime());
            tvDate.setText(item.getpDateNow());
            tvNameUpload.setText(item.getuName());
            Glide.with(context).load(item.getuImageUrl())
                    .skipMemoryCache(true)
                    .error(R.drawable.im_account)
                    .centerCrop()
                    .into(civAccountUpload);

//            numberLike.setText(jobModel.getNumberLike());
//            numberComment.setText(jobModel.getNumberComment());
//            numberShare.setText(jobModel.getNumberShare());
        }
    }

    public interface ItemListener {
        void itemOnclickListener(int position);
    }
}
