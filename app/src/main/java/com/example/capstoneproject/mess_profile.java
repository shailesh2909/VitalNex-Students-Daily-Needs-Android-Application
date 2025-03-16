package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class mess_profile extends Fragment {
    Button feedback;

    TextView serviceName,price,address,contact,owner,workingDays,category,morningTime,eveningTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mess_profile, container, false);

        serviceName = view.findViewById(R.id.serviceName);
        price =view.findViewById(R.id.price);
        address=view.findViewById(R.id.address);
        contact=view.findViewById(R.id.contact);
        owner=view.findViewById(R.id.owner);
        workingDays=view.findViewById(R.id.workingdays);
        category=view.findViewById(R.id.category);
        morningTime=view.findViewById(R.id.morningtime);
        eveningTime=view.findViewById(R.id.eveningtime);

        feedback = view.findViewById(R.id.btnFeedbackMess);


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
            price.setText(args.getString("price"));
            category.setText(args.getString("category"));
            address.setText(args.getString("address"));
            contact.setText(args.getString("contact"));
            owner.setText(args.getString("owner"));
            workingDays.setText(args.getString("workingDays"));
            morningTime.setText(args.getString("morningTime"));
            eveningTime.setText(args.getString("eveningTime"));
        }


        // Inflate the layout for this fragment
        return view;
    }
}
