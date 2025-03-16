package com.example.capstoneproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class User_Profile extends Fragment {

    // Define a counter to keep track of completed Firebase calls
    private int firebaseCallCounter = 0;

    // Call this method after each Firebase call to increment the counter and check if all calls are complete
    private void checkFirebaseCallsComplete() {
        firebaseCallCounter++;
        if (firebaseCallCounter == 3) { // Change the value to the number of Firebase calls you're making
            adapter.notifyDataSetChanged(); // Update the adapter
        }
    }

    private TextView nameTextView;
    private TextView emailTextView;

    private Button deleteButton;
    private Button editButton;


    private RecyclerView recyclerView;
    private User_ProfileAdapter adapter;
    private boolean isDataLoaded = false;
    FirebaseFirestore fstoremess;
    FirebaseAuth fAuthmess;
    String userID;

    // ArrayList for recyclerview
    private ArrayList<User_ProfileContactModel> arrContacts = new ArrayList<>();
    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        // Set user data
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser theUser = firebaseAuth.getCurrentUser();
        if (theUser != null){
            String email = (theUser.getEmail().toString());
            emailTextView.setText(email);
        }else {
            emailTextView.setText("Guest");
        }

        nameTextView.setText("User"); // Set user name

        recyclerView = view.findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new User_ProfileAdapter(getActivity(), arrContacts);
        recyclerView.setAdapter(adapter);


        fstoremess = FirebaseFirestore.getInstance();
        fAuthmess = FirebaseAuth.getInstance();
        userID = fAuthmess.getCurrentUser().getUid();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isDataLoaded) {
            firebaseCallCounter = 0; // Reset the counter
            getMessData();
            getMedicalData();
            getRoomData();
            isDataLoaded = true; // Mark data as loaded
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if data is already loaded when fragment is resumed
        if (!isDataLoaded) {
            firebaseCallCounter = 0; // Reset the counter
            getMessData();
            getMedicalData();
            getRoomData();
            isDataLoaded = true; // Mark data as loaded
        }
    }

    private void getMessData(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("USER").child("MESS").child(userID);
        if (databaseRef!=null) {
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();

                        String mess_address = (String) userData.get("mess_address");
                        String mess_category = (String) userData.get("mess_category");
                        String mess_eveningtime = (String) userData.get("mess_eveningtime");
                        String mess_mobile = (String) userData.get("mess_mobile");
                        String mess_moringtime = (String) userData.get("mess_moringtime");
                        String mess_name = (String) userData.get("mess_name");
                        String mess_owner = (String) userData.get("mess_owner");
                        String mess_price = (String) userData.get("mess_price");
                        String mess_workingday = (String) userData.get("mess_workingday");
                        arrContacts.add( new User_ProfileContactModel(R.drawable.gear, mess_name, "Mess", mess_address));

                    } else {
                       // Toast.makeText(getActivity().getApplicationContext(), "Data does not exist at this path", Toast.LENGTH_SHORT).show();
                    }
                    checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error retrieving data: " + databaseError.getMessage());
                    checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

                }
            });

        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Mess Service not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMedicalData(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("USER").child("MEDICAL").child(userID);
        if (databaseRef!=null) {
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();

                        String hosp_address = (String) userData.get("hosp_address");
                        String hosp_category = (String) userData.get("hosp_category");
                        String hosp_doctor = (String) userData.get("hosp_doctor");
                        String hosp_mobile = (String) userData.get("hosp_mobile");
                        String hosp_name = (String) userData.get("hosp_name");
                        String hosp_owner = (String) userData.get("hosp_owner");
                        arrContacts.add( new User_ProfileContactModel(R.drawable.gear, hosp_name, "Hospital", hosp_address));

                    } else {
                        //Toast.makeText(getActivity().getApplicationContext(), "Data does not exist at this path", Toast.LENGTH_SHORT).show();
                    }
                    checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error retrieving data: " + databaseError.getMessage());
                    checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

                }
            });

        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Mess Service not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRoomData(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("USER").child("ROOM").child(userID);
        if (databaseRef!=null) {
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();

                        String room_address = (String) userData.get("room_address");
                        String room_category = (String) userData.get("room_category");
                        String room_members = (String) userData.get("room_members");
                        String room_mobile = (String) userData.get("room_mobile");
                        String room_name = (String) userData.get("room_name");
                        String room_owner = (String) userData.get("room_owner");
                        String room_price = (String) userData.get("room_price");
                        arrContacts.add( new User_ProfileContactModel(R.drawable.gear, room_owner, "Room", room_address));
                    } else {
                      //  Toast.makeText(getActivity().getApplicationContext(), "Data does not exist at this path", Toast.LENGTH_SHORT).show();
                    }
                    checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error retrieving data: " + databaseError.getMessage());
                    checkFirebaseCallsComplete(); // Call the method to check if all Firebase calls are complete

                }
            });

        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Mess Service not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void addIfNotPresent(ArrayList<User_ProfileContactModel> arrContacts, User_ProfileContactModel contact) {
        boolean isCardAlreadyPresent = false;
        for (User_ProfileContactModel existingContact : arrContacts) {
            if (existingContact.getName().equals(contact.getName())) {
                isCardAlreadyPresent = true;
                break;
            }
        }

        if (!isCardAlreadyPresent) {
            arrContacts.add(contact);
        }
    }

}
