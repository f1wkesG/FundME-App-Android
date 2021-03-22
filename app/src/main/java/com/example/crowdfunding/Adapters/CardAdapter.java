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

import com.example.crowdfunding.Activities.ConfirmActivity;
import com.example.crowdfunding.Models.Card;
import com.example.crowdfunding1.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    Context mContext;
    List<Card> mData;

    public CardAdapter(Context mContext, List<Card> mData){
        this.mContext = mContext;
        this.mData    = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_card, parent, false);
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
        Button select;

        public MyViewHolder(View itemView) {
            super(itemView);

            number  = itemView.findViewById(R.id.cardNumber);
            cardIm  = itemView.findViewById(R.id.cardImg);
            select  = itemView.findViewById(R.id.selectbtn);


            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent confirm = new Intent(mContext, ConfirmActivity.class);

                    confirm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    int position = getAdapterPosition();

                    confirm.putExtra("card", mData.get(position).getNumber());
                    confirm.putExtra("iduser", mData.get(position).getUserID());
                    confirm.putExtra("idproject", mData.get(position).getProjectID());
                    confirm.putExtra("title", mData.get(position).getTitle());
                    confirm.putExtra("amount", mData.get(position).getAmount());


                    mContext.startActivity(confirm);
                }
            });
        }
    }
}