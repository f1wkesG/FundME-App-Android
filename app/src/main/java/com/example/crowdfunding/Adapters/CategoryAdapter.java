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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context mContext;
    List<Project> mData;


    public CategoryAdapter(Context mContext, List<Project> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);

        return new CategoryAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {

        holder.title.setText(mData.get(position).getTitle());
        System.out.println(mData.toString());
        //Glide.with(mContext).load(mData.get(position).getUs).into(holder.imgUser);
        Glide.with(mContext).load(mData.get(position).getImages().split(";")[0]).into(holder.imgProject);
        holder.percentageF.setText((mData.get(position).getMontant_r()*100  / mData.get(position).getMontant())+ "%");
        holder.financedProgress.setProgress((mData.get(position).getMontant_r()*100  / mData.get(position).getMontant()));

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
            //nbrContribution = itemView.findViewById(R.id.row_post_nbrContribution);
            dayLeft = itemView.findViewById(R.id.row_post_dayLeft);
            financedProgress = itemView.findViewById(R.id.row_post_financedProgress);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("title", mData.get(position).getTitle());
                    postDetailActivity.putExtra("postKey", mData.get(position).getProjectId());
                    postDetailActivity.putExtra("postImage", mData.get(position).getImages().split(";")[0]);
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("montant", mData.get(position).getMontant());
                    postDetailActivity.putExtra("montant_r", mData.get(position).getMontant_r());

                    long timestamp = (long)mData.get(position).getTimestamp();
                    postDetailActivity.putExtra("postDate", timestamp);

                    mContext.startActivity(postDetailActivity);


                }
            });
        }

        /*

        GET username from user_id


         */
    }
}
