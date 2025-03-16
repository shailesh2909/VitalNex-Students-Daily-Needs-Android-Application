package com.example.capstoneproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    Context context;
    ArrayList<RoomContactModel> arrContacts;
    private ArrayList<RoomContactModel> filteredContacts; // Added for filtering


    // Define an interface to handle item clicks and pass data
    public interface OnItemClickListener {
        void onItemClick(RoomContactModel contact);
    }

    private OnItemClickListener mListener;

    RoomAdapter(Context context, ArrayList<RoomContactModel> arrContacts, OnItemClickListener listener){
        this.context = context;
        this.arrContacts = arrContacts;
        this.filteredContacts = new ArrayList<>(arrContacts); // Initialize filteredContacts with arrContacts
        this.mListener = listener;
    }

    public void setData(ArrayList<RoomContactModel> arrContacts) {
        this.arrContacts = arrContacts;
        this.filteredContacts.clear();
        this.filteredContacts.addAll(arrContacts);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    //This method will create the holder where the card is being hold
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //This method is used to pass the values to the card of information
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomContactModel contact = arrContacts.get(position);
        holder.imageView.setImageResource(arrContacts.get(position).getImg());
        holder.txtName.setText(arrContacts.get(position).getName());
        holder.txtAddress.setText(arrContacts.get(position).getAddress());
        holder.txtavalroom.setText(arrContacts.get(position).getAvalroom());
        holder.txtroommember.setText(arrContacts.get(position).getRoommembers());
        holder.txtprice.setText(arrContacts.get(position).getPrice());
        holder.ratingBar.setRating(arrContacts.get(position).getRate());

        //clickable card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the clicked item data to the activity
                mListener.onItemClick(contact);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtAddress, txtavalroom, txtroommember,txtprice;
        ImageView imageView;

        RatingBar ratingBar;
        public ViewHolder(View itemView){
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtavalroom = itemView.findViewById(R.id.txtavalroom);
            imageView = itemView.findViewById(R.id.imgView);
            txtroommember = itemView.findViewById(R.id.txtroommember);
            txtprice = itemView.findViewById(R.id.txtprice);
            ratingBar = itemView.findViewById(R.id.txtrating);
        }
    }
}