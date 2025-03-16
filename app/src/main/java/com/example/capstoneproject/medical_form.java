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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class medical_form extends Fragment {

    String hhname, hdrname;
    String data[];
    DataValidation dataValidation;
    EditText hname, drname;
    CheckBox gen, teach, acc;
    Button submitInfo, imgbtn, imgCerbtn;
    FirebaseAuth fAuth;
    String userID;
    EditText name, add, mob;
    ImageView imgGallery,imgCerGallery;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_form, container, false);

        data = new String[2];
        dataValidation = new DataValidation();
        //text fileds
        name = view.findViewById(R.id.userName);
        add = view.findViewById(R.id.address);
        mob = view.findViewById(R.id.mobNum);
        hname = view.findViewById(R.id.med_name);
        drname = view.findViewById(R.id.dr_name);
        //Checkbox
        gen = view.findViewById(R.id.general);
        teach = view.findViewById(R.id.teaching);
        acc = view.findViewById(R.id.accidental);
        //Buttons
        imgCerbtn = view.findViewById(R.id.imgCerbtn);
        imgbtn = view.findViewById(R.id.imgbtn);
        submitInfo = view.findViewById(R.id.medical_sub);
        //imageView
        imgGallery=view.findViewById(R.id.imgGallery);
        imgCerGallery=view.findViewById(R.id.imgCerGallery);

        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data[0] = hhname = hname.getText().toString().trim();
                data[1] = hdrname = drname.getText().toString().trim();

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

        imgCerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,2);
                Toast.makeText(getActivity().getApplicationContext(), "Work is not done yet", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void addTheData(){
        Map<String, Object> map =  new HashMap<>();
        map.put("hosp_owner", name.getText().toString().trim());
        map.put("hosp_address", add.getText().toString().trim());
        map.put("hosp_mobile", mob.getText().toString().trim());
        map.put("hosp_name",hhname);
        map.put("hosp_doctor",hdrname);
//         Build category based on checkbox selection
                    StringBuilder category = new StringBuilder();
                    if (gen.isChecked()) {
                        category.append("General ");
                    }
                    if (teach.isChecked()) {
                        category.append("Teaching ");
                    }
                    if (acc.isChecked()) {
                        category.append("Accidental ");
                    }
        map.put("hosp_category",category.toString());

        //Generate unique id
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("USER").child("MEDICAL").child(userID)
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
                imgGallery.setImageBitmap(photo);
            } else if(requestCode == 2){
                imgCerGallery.setImageBitmap(photo);
            }
        }
    }





}
