
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

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.ViewHolder> {
    Context context;
    ArrayList<MedicalContactModel> arrContacts;
    private ArrayList<MedicalContactModel> filteredContacts; // Added for filtering

    // Define an interface to handle item clicks and pass data
    public interface OnItemClickListener {
        void onItemClick(MedicalContactModel contact);
    }

    private OnItemClickListener mListener;

    MedicalAdapter(Context context, ArrayList<MedicalContactModel> arrContacts,OnItemClickListener listener){
        this.context = context;
        this.arrContacts = arrContacts;
        this.filteredContacts = new ArrayList<>(arrContacts); // Initialize filteredContacts with arrContacts
        this.mListener = listener;
    }

    public void setData(ArrayList<MedicalContactModel> arrContacts) {
        this.arrContacts = arrContacts;
        this.filteredContacts.clear();
        this.filteredContacts.addAll(arrContacts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    //This method will create the holder where the card is being hold
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.medical_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    //This method is used to pass the values to the card of information
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalContactModel contact = arrContacts.get(position);

        holder.imageView.setImageResource(contact.getImg());
        holder.txtName.setText(contact.getName());
        holder.txtAddress.setText(contact.getAddress());
        holder.txtCat.setText(contact.getCat());
        holder.ratingBar.setRating(contact.getRat());
        holder.txtDRname.setText(contact.getDrname());


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
        TextView txtName, txtAddress,txtCat, txtDRname;
        ImageView imageView;

        RatingBar ratingBar;
        public ViewHolder(View itemView){
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            imageView = itemView.findViewById(R.id.imgView);
            txtCat = itemView.findViewById(R.id.txtCategory);
            ratingBar = itemView.findViewById(R.id.txtrating);
            txtDRname = itemView.findViewById(R.id.txtDRName);
        }
    }
}