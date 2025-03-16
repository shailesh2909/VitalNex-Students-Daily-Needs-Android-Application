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

public class Medical extends Fragment implements MedicalAdapter.OnItemClickListener{

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
    private MedicalAdapter adapter;
    private SearchView searchView;
    FloatingActionButton create;
    // ArrayList for recyclerview
    private ArrayList<MedicalContactModel> arrContacts = new ArrayList<>();
    private ArrayList<MedicalContactModel> filteredContacts = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    ArrayList<String> drname = new ArrayList<>();
    ArrayList<String> contactNo = new ArrayList<>();

    @Override
    public void onItemClick(MedicalContactModel contact) {
        // Handle item click and pass data to the new fragment


        Fragment newFragment = new medical_profile();
        Bundle args = new Bundle();

        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equals(contact.getName())){
                args.putString("name", contact.getName());
                args.putString("address",address.get(i));
                args.putString("category",category.get(i));
                args.putString("contact",contactNo.get(i));
                args.putString("drname",drname.get(i));
            }
        }

        newFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_medical, container, false);

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

        adapter = new MedicalAdapter(getActivity(), arrContacts,this);
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
    @Override
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
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("USER").child("MEDICAL");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method will be called once with the data from the database.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve data from each snapshot
                    name.add(snapshot.child("hosp_name").getValue(String.class));
                    address.add(snapshot.child("hosp_address").getValue(String.class));
                    category.add(snapshot.child("hosp_category").getValue(String.class));
                    drname.add(snapshot.child("hosp_doctor").getValue(String.class));
                    contactNo.add(snapshot.child("hosp_mobile").getValue(String.class));
                }

                for (int i = 0; i < name.size(); i++) {
                    String nm = name.get(i);
                    String add = address.get(i);
                    String cat = category.get(i);
                    String dr = drname.get(i);
                    arrContacts.add( new MedicalContactModel(R.drawable.pills, nm, add, cat,dr,(float)3));
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
        for (MedicalContactModel contact : arrContacts) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredContacts.add(contact);
            }
        }
        adapter.setData(filteredContacts);
    }
}
