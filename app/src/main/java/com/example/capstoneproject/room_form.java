package com.example.capstoneproject;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class room_form extends Fragment {

    String data[];
    String ravailroom, rroommember, rprice, rcategory;

    DataValidation dataValidation;
    EditText availroom, roommember, price;
    RadioButton bed, nonbed;
    Button submitInfo, imgbtn;

    FirebaseAuth fAuth;
    String userID;
    EditText name, add, mob;
    DatabaseReference databaseReference;
    FirebaseFirestore fstore;
    ImageView imgGallary;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_form, container, false);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER").child(userID).child("SERVICES").child("ROOM");

        dataValidation = new DataValidation();
        data = new String[4];

        name = view.findViewById(R.id.userName);
        add = view.findViewById(R.id.address);
        mob = view.findViewById(R.id.mobNum);
        availroom = view.findViewById(R.id.roomavai);
        roommember = view.findViewById(R.id.room_mem);
        price = view.findViewById(R.id.Price);

        bed = view.findViewById(R.id.bed);
        nonbed = view.findViewById(R.id.non_bed);

        submitInfo = view.findViewById(R.id.room_sub);
        imgbtn = view.findViewById(R.id.imgbtn);

        imgGallary=view.findViewById(R.id.imgGallery);

        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data[0] = ravailroom = availroom.getText().toString().trim();
                data[1] = rprice = price.getText().toString().trim();
                data[2] = rroommember = roommember.getText().toString().trim();
                data[3] = "";

                if (bed.isChecked()) {
                    data[3] = rcategory = "Bed";
                }
                if (nonbed.isChecked()) {
                    data[3] = rcategory = "Non-Bed";
                }

                if (dataValidation.validateEmptyData(data)) {
                    addTheData();
                } else {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,1);
                Toast.makeText(getActivity().getApplicationContext(), "Work is not done yet", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addTheData(){
        Map<String, Object> map =  new HashMap<>();
        map.put("room_owner", name.getText().toString().trim());
        map.put("room_address", add.getText().toString().trim());
        map.put("room_mobile", mob.getText().toString().trim());
        map.put("room_name",ravailroom);
        map.put("room_price",rprice);
        map.put("room_members",rroommember);
        map.put("room_category",rcategory);

        //Generate unique id
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("USER").child("ROOM").child(userID)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //kindly clear the data
                        //move to next
                        Toast.makeText(getActivity().getApplicationContext(), "Data submitted", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Data not submitted", Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if(requestCode == 1){
                imgGallary.setImageBitmap(photo);
            }
        }
    }
}

