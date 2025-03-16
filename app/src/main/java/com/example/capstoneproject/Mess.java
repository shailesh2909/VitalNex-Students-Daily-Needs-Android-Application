package com.example.capstoneproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mess extends Fragment implements MessAdapter.OnItemClickListener{

    // Define a counter to keep track of completed Firebase calls
    private int firebaseCallCounter = 0;

    // Call this method after each Firebase call to increment the counter and check if all calls are complete
    private void checkFirebaseCallsComplete() {
        firebaseCallCounter++;
        if (firebaseCallCounter == 1) { // Change the value to the number of Firebase calls you're making
            adapter.notifyDataSetChanged(); // Update the adapter
        }
    }
    private boolean isDataLoaded = false;
    private RecyclerView recyclerView;
    private MessAdapter adapter;
    private SearchView searchView;
    FloatingActionButton create;
    private ArrayList<MessContactModel> arrContacts = new ArrayList<>();
    private ArrayList<MessContactModel> filteredContacts = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();

    ArrayList<String> owner = new ArrayList<>();

    ArrayList<String> contactNo = new ArrayList<>();

    ArrayList<String> workingDays = new ArrayList<>();

    ArrayList<String> morningTime = new ArrayList<>();

    ArrayList<String> eveningTime = new ArrayList<>();
    @Override
    public void onItemClick(MessContactModel contact) {
        // Handle item click and pass data to the new fragment
        Fragment newFragment = new mess_profile();
        Bundle args = new Bundle();

        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equals(contact.getName())){
                args.putString("name", contact.getName());
                args.putString("address",address.get(i));
                args.putString("price",price.get(i));
                args.putString("category",category.get(i));
                args.putString("owner",owner.get(i));
                args.putString("contact",contactNo.get(i));
                args.putString("workingDays",workingDays.get(i));
                args.putString("morningTime",morningTime.get(i));
                args.putString("eveningTime",eveningTime.get(i));

            }
        }

          newFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mess, container, false);

        searchView = view.findViewById(R.id.sv);
        create = view.findViewById(R.id.addservice);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        recyclerView = view.findViewById(R.id.view);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MessAdapter(getActivity(), arrContacts,this);
        recyclerView.setAdapter(adapter);



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                serviceForm form = new serviceForm();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,form).addToBackStack(null).commit();
                Toast.makeText(getActivity().getApplicationContext(), "Add your service", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isDataLoaded) {
//            firebaseCallCounter = 0; // Reset the counter
//            Retriving the data from data base through getThedata() method
            getTheData();
            firebaseCallCounter = 0; // Reset the counter
            isDataLoaded = true; // Mark data as loaded
        }
    }


    private void getTheData(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("USER").child("MESS");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method will be called once with the data from the database.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve data from each snapshot
                    name.add( snapshot.child("mess_name").getValue(String.class));
                    address.add(snapshot.child("mess_address").getValue(String.class));
                    category.add(snapshot.child("mess_category").getValue(String.class));
                    price.add(snapshot.child("mess_price").getValue(String.class));
                    eveningTime.add(snapshot.child("mess_eveningtime").getValue(String.class));
                    morningTime.add(snapshot.child("mess_moringtime").getValue(String.class));
                    owner.add(snapshot.child("mess_owner").getValue(String.class));
                    contactNo.add(snapshot.child("mess_mobile").getValue(String.class));
                    workingDays.add(snapshot.child("mess_workingday").getValue(String.class));

                }
                for (int i = 0; i < name.size(); i++) {
                    String nm = name.get(i);
                    String add = address.get(i);
                    String cat = category.get(i);
                    String pric = price.get(i);
                    arrContacts.add( new MessContactModel(R.drawable.food, nm, add, cat,"₹"+pric+"/-",(float)3));
                }
                checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

            }
        });
    }

    private void filter(String query) {
        filteredContacts.clear();
        for (MessContactModel contact : arrContacts) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredContacts.add(contact);
            }
        }
        adapter.setData(filteredContacts);
    }
}
