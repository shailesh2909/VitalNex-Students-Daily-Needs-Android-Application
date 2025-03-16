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
import android.widget.TimePicker;
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

public class mess_form extends Fragment {

    private String mname,mprice,mworkingday,mcategory,mmoringtime,meveiningtime;

    String[] data;

    boolean flag=false;
    DataValidation dataValidation;
    TimePicker morningtime,eveingtime;

    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    String userID;
    EditText name, add, mob;
    EditText messNameEditText, priceEditText, workingDayEditText;
    RadioButton vegRadioButton, nonVegRadioButton, bothRadioButton;
    Button submitInfo, imgbtn;

    DatabaseReference databaseReference;
    ImageView imgGallary;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mess_form, container, false);

        data = new String[6];
        dataValidation = new DataValidation();

        name = view.findViewById(R.id.userName);
        add = view.findViewById(R.id.address);
        mob = view.findViewById(R.id.mobNum);
        messNameEditText = view.findViewById(R.id.mess_name);
        priceEditText = view.findViewById(R.id.mess_price);
        workingDayEditText = view.findViewById(R.id.working_day);
        vegRadioButton = view.findViewById(R.id.veg);
        nonVegRadioButton = view.findViewById(R.id.non_veg);
        bothRadioButton = view.findViewById(R.id.both);

        morningtime= (TimePicker) view.findViewById(R.id.morning_time);
        eveingtime= (TimePicker) view.findViewById(R.id.evening_time);

        submitInfo = view.findViewById(R.id.mess_sub);
        imgbtn = view.findViewById(R.id.imgbtn);

        imgGallary= view.findViewById(R.id.imgGallery);

        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data[0]=mname = messNameEditText.getText().toString().trim();
                data[1]=mprice = priceEditText.getText().toString().trim();
                data[2]=mworkingday = workingDayEditText.getText().toString().trim();
                if (mworkingday.length()==1){
                    flag = true;
                }
                data[3]="";
                if (vegRadioButton.isChecked()) {
                    data[3]=mcategory="Veg";
                } else if (nonVegRadioButton.isChecked()) {
                    data[3]=mcategory="Nonveg";
                } else if (bothRadioButton.isChecked()) {
                    data[3]=mcategory="Veg/Nonveg";
                }
                data[4]=mmoringtime=morningtime.getHour()+"/"+morningtime.getMinute();
                data[5]=meveiningtime=eveingtime.getHour()+"/"+eveingtime.getMinute();

                if (dataValidation.validateEmptyData(data)) {
                    if(!flag)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid data in working day field", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        addTheData();
                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
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
        map.put("mess_owner", name.getText().toString().trim());
        map.put("mess_address", add.getText().toString().trim());
        map.put("mess_mobile", mob.getText().toString().trim());
        map.put("mess_name",mname);
        map.put("mess_price",mprice);
        map.put("mess_workingday",mworkingday);
        map.put("mess_category",mcategory);
        map.put("mess_moringtime",mmoringtime);
        map.put("mess_eveningtime",meveiningtime);

        //Generate unique id
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("USER").child("MESS").child(userID)
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
