package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Home extends Fragment {

    ViewFlipper flipper;

    ImageButton room, mess, medical;
    FrameLayout fragmentContainer;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        flipper = view.findViewById(R.id.flippermain);
        room = view.findViewById(R.id.room);
        mess = view.findViewById(R.id.mess);
        medical = view.findViewById(R.id.medical);

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Medical());
            }
        });

        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Mess());
            }
        });

        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Room());
            }
        });

        // Initialize the ViewFlipper
        flipper = view.findViewById(R.id.flippermain);

        // Ensure that the ViewFlipper is not null before proceeding
        if (flipper != null) {
            int[] imgarr = {R.drawable.img2, R.drawable.img3, R.drawable.img1};

            for (int j : imgarr) {
                showimage(j);
            }
        } else {
            // Handle the case where flipper is null, perhaps log an error or show a message
            Toast.makeText(requireContext(), "ViewFlipper is null", Toast.LENGTH_SHORT).show();
        }

        // Inflate the layout for this fragment
        return view;
    }

    public void showimage(int img) {
        ImageView imageview = new ImageView(requireContext());
        imageview.setBackgroundResource(img);

        flipper.addView(imageview);
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);

        flipper.setInAnimation(requireContext(), android.R.anim.slide_in_left);
        flipper.setOutAnimation(requireContext(), android.R.anim.slide_out_right);
    }

    private void loadFragment(Fragment fragment) {
        // Set visibility to GONE when the fragment is loaded
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
