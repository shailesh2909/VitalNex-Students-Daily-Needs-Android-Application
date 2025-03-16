package com.example.capstoneproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class User_ProfileAdapter extends RecyclerView.Adapter<User_ProfileAdapter.ViewHolder>{
    Context context;
    FirebaseAuth fAuth;
    ArrayList<User_ProfileContactModel> arrContacts;
    private ArrayList<User_ProfileContactModel> filteredContacts; // Added for filtering
    User_ProfileAdapter(Context context, ArrayList<User_ProfileContactModel> arrContacts){
        this.context = context;
        this.arrContacts = arrContacts;
        this.filteredContacts = new ArrayList<>(arrContacts); // Initialize filteredContacts with arrContacts
    }

    public void setData(ArrayList<User_ProfileContactModel> arrContacts) {
        this.arrContacts = arrContacts;
        this.filteredContacts.clear();
        this.filteredContacts.addAll(arrContacts);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    //This method will create the holder where the card is being hold
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //This method is used to pass the values to the card of information
    @Override
    public void onBindViewHolder(@NonNull User_ProfileAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageResource(arrContacts.get(position).getImg());
        holder.txtName.setText(arrContacts.get(position).getName());
        holder.txtService.setText(arrContacts.get(position).getService());
        holder.txtAddres.setText(arrContacts.get(position).getsAddress());
        holder.menuPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.user_profile_pop_up_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.edit:
                                AppCompatActivity activity = (AppCompatActivity) context;
                                serviceForm form = new serviceForm();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, form).addToBackStack(null).commit();
                                break;
                            case R.id.delete:
                                String ser = arrContacts.get(position).getService().toUpperCase();
                                fAuth = FirebaseAuth.getInstance();
                                String userID = fAuth.getCurrentUser().getUid();

                                FirebaseDatabase.getInstance().getReference().child("USER").child(ser).child(userID).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Service Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Deletion failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });



        //clickable card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppCompatActivity activity = (AppCompatActivity)view.getContext();
//                mess_profile profile = new mess_profile();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,profile).addToBackStack(null).commit();
            }
        });

    }

    private void deleteService(String service) {



    }



    @Override
    public int getItemCount() {
        return arrContacts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtService, txtAddres;
        ImageView imageView,menuPopUp;
        public ViewHolder(View itemView){
            super(itemView);
            txtService=itemView.findViewById(R.id.txtService);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddres = itemView.findViewById(R.id.txtAdd);
            imageView = itemView.findViewById(R.id.imgView);
            menuPopUp= itemView.findViewById(R.id.menuMore);
        }
    }
}
