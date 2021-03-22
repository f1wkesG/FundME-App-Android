package com.example.crowdfunding.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowdfunding.Activities.RemoveActivity;
import com.example.crowdfunding.Models.CardInsert;
import com.example.crowdfunding1.R;

import java.util.List;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.MyViewHolder> {

    Context mContext;
    List<CardInsert> mData;

    public MenuCardAdapter(Context mContext, List<CardInsert> mData){
        this.mContext = mContext;
        this.mData    = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_menu_card, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.number.setText(String.valueOf(mData.get(position).getNumber()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView number;
        ImageView cardIm;
        Button remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            number  = itemView.findViewById(R.id.cardNumber);
            cardIm  = itemView.findViewById(R.id.cardImg);
            remove  = itemView.findViewById(R.id.removebtn);


            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent remove = new Intent(mContext, RemoveActivity.class);
                    int position = getAdapterPosition();
                    remove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    remove.putExtra("card", mData.get(position).getNumber());
                    mContext.startActivity(remove);
                }
            });
        }
    }
}