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

public class MessAdapter extends RecyclerView.Adapter<MessAdapter.ViewHolder> {
    Context context;
    ArrayList<MessContactModel> arrContacts;
    private ArrayList<MessContactModel> filteredContacts; // Added for filtering

    // Define an interface to handle item clicks and pass data
    public interface OnItemClickListener {
        void onItemClick(MessContactModel contact);
    }

    private OnItemClickListener mListener;

    MessAdapter(Context context, ArrayList<MessContactModel> arrContacts,OnItemClickListener listener){
        this.context = context;
        this.arrContacts = arrContacts;
        this.filteredContacts = new ArrayList<>(arrContacts); // Initialize filteredContacts with arrContacts
        this.mListener = listener;
    }

    public void setData(ArrayList<MessContactModel> arrContacts) {
        this.arrContacts = arrContacts;
        this.filteredContacts.clear();
        this.filteredContacts.addAll(arrContacts);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    //This method will create the holder where the card is being hold
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mess_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //This method is used to pass the values to the card of information
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessContactModel contact = arrContacts.get(position);

        holder.imageView.setImageResource(contact.getImg());
        holder.txtName.setText(contact.getName());
        holder.txtAddress.setText(contact.getAddress());
        holder.txtCategory.setText(contact.getCategory());
        holder.txtprice.setText(contact.getPrice());
        holder.ratingBar.setRating(contact.getRate());

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
        TextView txtName, txtAddress, txtCategory,txtprice;
        ImageView imageView;
        RatingBar ratingBar;
        public ViewHolder(View itemView){
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            imageView = itemView.findViewById(R.id.imgView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtprice = itemView.findViewById(R.id.txtprice);
            ratingBar = itemView.findViewById(R.id.txtrating);
        }
    }
}