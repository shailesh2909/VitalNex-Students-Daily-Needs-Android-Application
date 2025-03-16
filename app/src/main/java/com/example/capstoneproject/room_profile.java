package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class room_profile extends Fragment {
    Button feedback;

    TextView serviceName,price,address,contact,owner,avlRoom,member,bednonbed;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_profile, container, false);

        serviceName = view.findViewById(R.id.serviceName);
        price =view.findViewById(R.id.price);
        address=view.findViewById(R.id.address);
        contact=view.findViewById(R.id.contact);
        owner=view.findViewById(R.id.owner);
        avlRoom=view.findViewById(R.id.availrooms);
        member=view.findViewById(R.id.roommember);
        bednonbed=view.findViewById(R.id.bednonbed);



        feedback=view.findViewById(R.id.btnFeedbackRoom);
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
            address.setText(args.getString("address"));
            contact.setText(args.getString("contact"));
            owner.setText(args.getString("owner"));
            bednonbed.setText(args.getString("bednonbed"));
            avlRoom.setText(args.getString("avlroom"));
            member.setText(args.getString("member"));
        }

        // Inflate the layout for this fragment
        return view; }
}