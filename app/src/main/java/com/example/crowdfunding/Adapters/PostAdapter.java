package com.example.crowdfunding.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crowdfunding.Activities.PostDetailActivity;
import com.example.crowdfunding.Models.Project;
import com.example.crowdfunding1.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Project> mData;


    public PostAdapter(Context mContext, List<Project> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.title.setText(mData.get(position).getTitle());
        //Glide.with(mContext).load(mData.get(position).getUs).into(holder.imgUser);
        Glide.with(mContext).load(mData.get(position).getImages().split(";")[0]).into(holder.imgProject);
        holder.percentageF.setText((mData.get(position).getMontant_r()*100  / mData.get(position).getMontant())+ "% Financed");
        holder.financedProgress.setProgress((mData.get(position).getMontant_r()*100  / mData.get(position).getMontant()));
        holder.dayLeft.setText("Expiration date : "+(mData.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, percentageF, nbrContribution, dayLeft;
        ImageView imgProject, imgUser;
        ProgressBar financedProgress;

        public MyViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.row_post_title);
            imgProject = itemView.findViewById(R.id.row_post_image);
            imgUser = itemView.findViewById(R.id.row_post_userImage);
            percentageF = itemView.findViewById(R.id.row_post_percentageF);
            dayLeft = itemView.findViewById(R.id.row_post_dayLeft);
            financedProgress = itemView.findViewById(R.id.row_post_financedProgress);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("postUser", mData.get(position).getUserid());
                    postDetailActivity.putExtra("title", mData.get(position).getTitle());
                    postDetailActivity.putExtra("postKey", mData.get(position).getProjectId());
                    postDetailActivity.putExtra("postImage", mData.get(position).getImages().split(";")[0]);
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("montant", mData.get(position).getMontant());
                    postDetailActivity.putExtra("montant_r", mData.get(position).getMontant_r());
                    postDetailActivity.putExtra("dateExp", mData.get(position).getDate());

                    long timestamp = (long)mData.get(position).getTimestamp();
                    postDetailActivity.putExtra("postDate", timestamp);
                    mContext.startActivity(postDetailActivity);

                }
            });
        }


    }
    /*private String getUsernameFromUid(String uid, String username){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference d_ref = database.getReference("User");

    }*/
    /*public void getUserImg(String uid){
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fbdb.getReference("");
    }*/
}
