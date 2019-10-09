package com.t3h.appdemo.adapter;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.t3h.appdemo.R;
import com.t3h.appdemo.intent.DetailNewsApp;
import com.t3h.appdemo.model.PostJob;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListJobAdapter extends RecyclerView.Adapter<ListJobAdapter.RcvHolder> {
    private ArrayList<PostJob> data;

    private Boolean checklike = false;

    private DatabaseReference likeRef;
    private DatabaseReference postRef;

    private String myUid;

    private Context context;


    public ListJobAdapter(Context context, ArrayList<PostJob> data) {
        this.data = data;
        this.context = context;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
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
        final String pTitle = data.get(position).getpTile();
        final String pIntroductJob = data.get(position).getpIntroductJob();
        final String pImage = data.get(position).getpImage();
        String pDateNow = data.get(position).getpDateNow();
        String pSomeCompany = data.get(position).getpSomeCompanyInformation();
        String pJobTime = data.get(position).getpJobTime();
        String pRecruitTime = data.get(position).getpRecruitTime();
        String pInfomation = data.get(position).getpInfomationJob();
        String pCompanyAddress = data.get(position).getpCompanyAddress();
        String pCompanyEmail = data.get(position).getpCompanyEmail();
        String pLikes = data.get(position).getpLikes();
        String pComments = data.get(position).getpComments();

        holder.tvNameUpload.setText(uName);
        holder.tvTitle.setText(pTitle);
        holder.tvSattusJob.setText(pRecruitTime);
        holder.tvIntro.setText(pIntroductJob);
        holder.tvDate.setText(pDateNow);
        holder.tvNumberLike.setText(pLikes);
        holder.tvNumberComment.setText(pComments);

        setLikes(holder, pId, pLikes);

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
                    .centerInside()
                    .into(holder.imageView);
        } catch (Exception e) {

        }

        if (uid.equals(myUid)) {
            holder.imbPopubMenu.setVisibility(View.VISIBLE);
            holder.imbPopubMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMoreOptions(holder.imbPopubMenu, uid, myUid, pId, pImage);
                }
            });
        } else {
            holder.imbPopubMenu.setVisibility(View.INVISIBLE);
        }

        holder.ln_item_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailNewsApp.class);
                intent.putExtra("postId", pId);
                context.startActivity(intent);
            }
        });
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailNewsApp.class);
                intent.putExtra("postId", pId);
                context.startActivity(intent);
            }
        });
        holder.imComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailNewsApp.class);
                intent.putExtra("postId", pId);
                context.startActivity(intent);
            }
        });


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pLikes = Integer.parseInt(data.get(position).getpLikes());
                final String postId = data.get(position).getpId();
                checklike = true;
                likeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (checklike) {
                            if (dataSnapshot.child(postId).hasChild(myUid)) {
                                postRef.child(postId).child("pLikes").setValue("" + (pLikes - 1));
                                likeRef.child(postId).child(myUid).removeValue();
                                checklike = false;
                            } else {
                                postRef.child(postId).child("pLikes").setValue("" + (pLikes + 1));
                                likeRef.child(postId).child(myUid).setValue("Liked");
                                checklike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        holder.tvNumberShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imageView.getDrawable();
                if (bitmapDrawable == null) {
                    sheraTextOnly(pIntroductJob, pTitle);
                } else {
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageAndText(pIntroductJob, pTitle, bitmap);
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imageView.getDrawable();
                if (bitmapDrawable == null) {
                    sheraTextOnly(pTitle, pIntroductJob);
                } else {
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageAndText(pTitle, pIntroductJob, bitmap);
                }
            }
        });
    }

    private void sheraTextOnly(String pIntroductJob, String pTitle) {
        String shareBody = pTitle + "\n" + pIntroductJob;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Chủ đề");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private void shareImageAndText(String pIntroductJob, String pTitle, Bitmap bitmap) {
        String shareBody = pIntroductJob + "\n" + pTitle;
        Uri uri = saveImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Chủ đề");
        intent.setType("image/png");
        context.startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imgFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imgFolder.mkdirs();
            File file = new File(imgFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context, "com.t3h.appdemo.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }


    private void setLikes(final RcvHolder holder, final String postKey, final String pLikes) {
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postKey).hasChild(myUid)) {
                    holder.like.setImageResource(R.drawable.icons_thumbs_black_24);
                    holder.tvNumberLike.setText(pLikes);
                } else {
                    holder.like.setImageResource(R.drawable.icons_thumbs_24);
                    holder.tvNumberLike.setText(pLikes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMoreOptions(ImageButton imbPopubMenu, String uid, String myUid, final String pId, final String pImage) {
        PopupMenu popupMenu = new PopupMenu(context, imbPopubMenu, Gravity.END);
        if (uid.equals(myUid)) {
            imbPopubMenu.setVisibility(View.VISIBLE);
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
//            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");
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
//                else if (id == 1){
//                    Intent intent = new Intent(context, CreatNews.class);
//                    intent.putExtra("key","editPost");
//                    intent.putExtra("editPostId",pId);
//                    context.startActivity(intent);
//                }
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
        progressDialog.setCancelable(false);
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
        private ImageView imageView ,imComment;
        private LinearLayout ln_item_comment;
        private ImageButton like, share, imbPopubMenu;
        private CircleImageView civAccountUpload;
        private TextView tvComment, tvTitle, tvIntro, tvDate, tvSattusJob, tvNameUpload,
                tvNumberLike, tvNumberShare, tvNumberComment;

        public RcvHolder(@NonNull View itemView) {
            super(itemView);
            ln_item_comment = itemView.findViewById(R.id.ln_item_cmt);
            imageView = itemView.findViewById(R.id.imv_image_item_job);
            tvTitle = itemView.findViewById(R.id.tv_title_item_job);
            tvIntro = itemView.findViewById(R.id.tv_intro_item_job);
            tvDate = itemView.findViewById(R.id.tv_date_item_job);
            tvSattusJob = itemView.findViewById(R.id.tv_status_item_job);
            tvComment = itemView.findViewById(R.id.tv_comment_item);

            tvNameUpload = itemView.findViewById(R.id.tv_name_upload);
            civAccountUpload = itemView.findViewById(R.id.im_account_upload);
            like = itemView.findViewById(R.id.imgbtn_like_item_job);
            imbPopubMenu = itemView.findViewById(R.id.popub_menu);
            share = itemView.findViewById(R.id.imgbtn_share_item_job);
            imComment = itemView.findViewById(R.id.imv_cmt_item_job);

            tvNumberLike = itemView.findViewById(R.id.tv_number_like_item_job);
            tvNumberShare = itemView.findViewById(R.id.tv_share_item_job);
            tvNumberComment = itemView.findViewById(R.id.tv_number_comment_item_job);
        }

    }

}
