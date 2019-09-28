package com.t3h.appdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.appdemo.R;
import com.t3h.appdemo.model.JobModel;

import java.util.ArrayList;
import java.util.List;

public class ListJobAdapter extends RecyclerView.Adapter<ListJobAdapter.RcvHolder> {
    private LayoutInflater inflater;
    private ArrayList<JobModel> data;
    private ItemListener listener;
    private Animation fade;
    private Boolean checklike = false;
    private Boolean checkShare = false;

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<JobModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ListJobAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        fade = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation);
    }

    @NonNull
    @Override
    public RcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_job, parent, false);
        return new RcvHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RcvHolder holder, final int position) {
        holder.bindData(data.get(position));
        holder.itemView.startAnimation(fade);
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

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setFilter(List<JobModel>jobList){
        data = new ArrayList<>();
        data.addAll(jobList);
        notifyDataSetChanged();
    }

    public class RcvHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, comment;
        private LinearLayout ln_item_comment;
        private ImageButton like, share;
        private TextView tvTitle, tvIntro, tvDate,tvSattusJob,
                tvNumberLike, tvNumberShare, tvNumberComment;

        public RcvHolder(@NonNull View itemView) {
            super(itemView);
            ln_item_comment = itemView.findViewById(R.id.ln_item_cmt);
            imageView = itemView.findViewById(R.id.imv_image_item_job);
            tvTitle = itemView.findViewById(R.id.tv_title_item_job);
            tvIntro = itemView.findViewById(R.id.tv_intro_item_job);
            tvDate = itemView.findViewById(R.id.tv_date_item_job);
            tvSattusJob = itemView.findViewById(R.id.tv_status_item_job);

            like = itemView.findViewById(R.id.imgbtn_like_item_job);
            comment = itemView.findViewById(R.id.imgbtn_cmt_item_cmt);
            share = itemView.findViewById(R.id.imgbtn_share_item_job);

            tvNumberLike = itemView.findViewById(R.id.tv_number_like_item_job);
            tvNumberShare = itemView.findViewById(R.id.tv_number_comment_item_job);
            tvNumberComment = itemView.findViewById(R.id.tv_number_share_item_job);
        }

        public void bindData(JobModel item) {
            tvTitle.setText(item.getTitle());
            tvIntro.setText(item.getIntroduceJob());
            Glide.with(imageView).load(item.getImage())
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.media_img)
                    .centerCrop()
                    .error(R.drawable.media_img)
                    .into(imageView);
            tvSattusJob.setText(item.getRecruiTime());
            tvDate.setText(item.getDate());
//            numberLike.setText(jobModel.getNumberLike());
//            numberComment.setText(jobModel.getNumberComment());
//            numberShare.setText(jobModel.getNumberShare());
        }
    }

    public interface ItemListener {
        void itemOnclickListener(int position);
    }
}
