package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class medical_profile extends Fragment {

    Button feedback;

    TextView serviceName,address,contact,category,drname;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_profile, container, false);


        serviceName = view.findViewById(R.id.serviceName);
        address=view.findViewById(R.id.address);
        contact=view.findViewById(R.id.contact);
        category=view.findViewById(R.id.category);
        drname=view.findViewById(R.id.drname);


        feedback=(Button) view.findViewById(R.id.btnFeedbackMedical);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                Rating profile = new Rating();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,profile).addToBackStack(null).commit();

            }
        });

        Bundle args = getArguments();
        if (args != null) {
            serviceName.setText(args.getString("name"));
           drname.setText(args.getString("drname"));
            category.setText(args.getString("category"));
            address.setText(args.getString("address"));
            contact.setText(args.getString("contact"));

        }
        else {
            // Handle the case where arguments are not provided
            serviceName.setText("Service Name Not Available");
            drname.setText("Doctor Name Not Available");
            category.setText("Category Not Available");
            address.setText("Address Not Available");
            contact.setText("Contact Not Available");
        }



        // Inflate the layout for this fragment
        return view;}
}