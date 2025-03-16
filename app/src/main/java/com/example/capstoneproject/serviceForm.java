
package com.example.capstoneproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class serviceForm extends Fragment {

    String[] items = {"Hospital", "Mess", "Room"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayItem;
    Intent i1;
    public serviceForm() {
        // Required empty public constructor
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_form, container, false);


        autoCompleteTextView = view.findViewById(R.id.li1);
        arrayItem = new ArrayAdapter<>(requireContext(), R.layout.dropdown, items);
        autoCompleteTextView.setAdapter(arrayItem);



        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).toString().equals("Mess")) {
                    replaceFragment(new mess_form());
                } else if (adapterView.getItemAtPosition(i).toString().equals("Hospital")) {
                    replaceFragment(new medical_form());
                } else {
                    replaceFragment(new room_form());
                }
            }

        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayout, fragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }
}
