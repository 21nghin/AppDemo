package com.t3h.appdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.appdemo.R;
import com.t3h.appdemo.model.CmtModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CmtAdapter extends RecyclerView.Adapter<CmtAdapter.CmtHolder> {
    private LayoutInflater inflater;
    private ArrayList<CmtModel> data;
    public CmtAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<CmtModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CmtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_comment,parent,false);
        return new CmtHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CmtHolder holder, final int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class CmtHolder extends RecyclerView.ViewHolder{
        private CircleImageView image;
        private ImageView like,comment;
        private TextView userName,date,cmt;
        public CmtHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imv_image_item_cmt);
            userName = itemView.findViewById(R.id.tv_user_name_item_cmt);
            date = itemView.findViewById(R.id.tv_date_item_cmt);
            like = itemView.findViewById(R.id.imgbtn_like_item_cmt);
            comment = itemView.findViewById(R.id.imgbtn_cmt_item_cmt);
            cmt = itemView.findViewById(R.id.tv_cmt_item_cmt);
        }

        public void bindData(CmtModel cmtModel){
            image.setImageResource(R.drawable.ic_likedefault);
            userName.setText(cmtModel.getUserName());
            date.setText(cmtModel.getDate());
            cmt.setText(cmtModel.getCmt());
        }
    }

}
