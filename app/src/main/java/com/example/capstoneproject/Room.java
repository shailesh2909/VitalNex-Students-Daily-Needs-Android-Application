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

public class Room extends Fragment implements RoomAdapter.OnItemClickListener{
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
    private RoomAdapter adapter;
    private SearchView searchView;
    FloatingActionButton create;

    // ArrayList for recyclerview
    private ArrayList<RoomContactModel> arrContacts = new ArrayList<>();
    private ArrayList<RoomContactModel> filteredContacts = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> avlroom = new ArrayList<>();
    ArrayList<String> member = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();

    ArrayList<String> contactNo = new ArrayList<>();

    ArrayList<String> bednonbed = new ArrayList<>();

    ArrayList<String> owner = new ArrayList<>();


    @Override
    public void onItemClick(RoomContactModel contact) {
        // Handle item click and pass data to the new fragment
        Fragment newFragment = new room_profile();
        Bundle args = new Bundle();

        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equals(contact.getName())){
                args.putString("name", contact.getName());
                args.putString("address",address.get(i));
                args.putString("price",price.get(i));
                args.putString("owner",owner.get(i));
                args.putString("contact",contactNo.get(i));
                args.putString("bednonbed",bednonbed.get(i));
                args.putString("member",member.get(i));
                args.putString("avlroom",avlroom.get(i));


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
        View view = inflater.inflate(R.layout.activity_room, container, false);


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

        adapter = new RoomAdapter(getActivity(), arrContacts,this);
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
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("USER").child("ROOM");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method will be called once with the data from the database.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve data from each snapshot
                    name.add( snapshot.child("room_owner").getValue(String.class));
                    address.add(snapshot.child("room_address").getValue(String.class));
                    avlroom.add(snapshot.child("room_name").getValue(String.class));
                    member.add(snapshot.child("room_members").getValue(String.class));
                    price.add(snapshot.child("room_price").getValue(String.class));
                    owner.add(snapshot.child("room_owner").getValue(String.class));
                    contactNo.add(snapshot.child("room_mobile").getValue(String.class));
                    bednonbed.add(snapshot.child("room_category").getValue(String.class));
                }
                for (int i = 0; i < name.size(); i++) {
                    String nm = name.get(i);
                    String add = address.get(i);
                    String rm = avlroom.get(i);
                    String mm = member.get(i);
                    String pric = price.get(i);

                   arrContacts.add( new RoomContactModel(R.drawable.house, nm, add, rm+" Available Room",mm+" Members/Room","₹"+pric+"/-",(float)3));

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
        for (RoomContactModel contact : arrContacts) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredContacts.add(contact);
            }
        }
        adapter.setData(filteredContacts);
    }
}
