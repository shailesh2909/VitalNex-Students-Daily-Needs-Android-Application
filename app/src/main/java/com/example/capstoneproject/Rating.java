package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Rating extends Fragment {



    FirebaseAuth fAuth;
    String userID;

    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;
    Button mSendFeedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        mRatingScale= (TextView) view.findViewById(R.id.tvRatingScale);
        mFeedback= (EditText) view.findViewById(R.id.mFeedback);
        mSendFeedback= (Button) view.findViewById(R.id.btnSubmit);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {

                    //add to database
                    Map<String, Object> map =  new HashMap<>();
                    map.put("user_rating",""+(int)mRatingBar.getRating());
                    map.put("user_feedback",mFeedback.getText().toString());

                    //Generate unique id
                    fAuth = FirebaseAuth.getInstance();
                    userID = fAuth.getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference().child("USER").child("REVIEW").child(userID)
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //kindly clear the data
                                    //move to next
                                    Toast.makeText(getContext(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Feedback not submitted", Toast.LENGTH_LONG).show();

                                }
                            });

                    mFeedback.setText("");
                    mRatingBar.setRating(3);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}