package com.example.capstoneproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LoadView extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a new thread to perform background tasks
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Sleep for 5 seconds
                try {
                    Thread.sleep(500); // Sleep for 5 seconds (5000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // After 5 seconds, run code on the main UI thread to show a toast
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Show toast

                        AppCompatActivity activity = (AppCompatActivity)view.getContext();
                        Mess form = new Mess();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,form).addToBackStack(null).commit();
//                        FragmentManager fragmentManager = activity.getSupportFragmentManager(); // Use getSupportFragmentManager() if you're in an Activity
////                        fragmentManager.popBackStack();
////                        fragmentManager.popBackStackImmediate();

                    }
                });
            }
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
