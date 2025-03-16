package com.example.capstoneproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity  {
    DrawerLayout drawerlayout;
    NavigationView navigationview;
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    MaterialAlertDialogBuilder alertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerlayout = findViewById(R.id.drawerlay);
        navigationview = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        // This causes to the chaniging email of header_lay file text view
        View hearderView = navigationview.getHeaderView(0);
        ImageView userImage = hearderView.findViewById(R.id.dlprofimg);
        TextView textUsername = hearderView.findViewById(R.id.dlprofname);
        TextView textUserEmail = hearderView.findViewById(R.id.dlprofemail);
        loadFirstFragment(new Home());


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser theUser = firebaseAuth.getCurrentUser();
        if (theUser != null){
            String email = (theUser.getEmail().toString());
            Log.d("APPLICATION SAYs", email.toString());
            //from here to change user app header_lay profile information
            textUserEmail.setText(email);



        }else {
            textUserEmail.setText("Guest");
        }




        // Setting up the navigation drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        toolbar.setTitleTextColor(getColor(R.color.white));
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();



        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Load the initial fragment (Home)
        loadFirstFragment(new Home());

        // Set up navigation item click listener
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                drawerlayout.closeDrawer(GravityCompat.START);
                if(id==R.id.dl_home){
                    loadFirstFragment(new Home());
                } else if (id==R.id.dl_feedback) {
                    if (theUser != null) {
                        loadFragment(new Rating());
                    }
                    else
                    {
                        showFeedbackDialog();
                    }
                }else if (id==R.id.dl_addService) {
                    if (theUser != null) {
                        loadFragment(new serviceForm());
                    }
                    else
                    {
                        showAddServiceDialog();
                    }

                }else if(id==R.id.dl_user_profile){
                    if (theUser != null) {
                        loadFragment(new User_Profile());
                    }
                    else
                    {
                        showAddServiceDialog();
                    }
                }else if(id==R.id.dl_terms_conditions){
                    showTermsAndConditionsDialog();
                }else if (id==R.id.dl_logout){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(MainActivity.this, login.class));
                }

                return true;
            }
        });
    }

    private void showTermsAndConditionsDialog() {
        alertDialogBuilder.setTitle("Terms And Conditions");

        // Create a WebView to render HTML content
        WebView webView = new WebView(this);
        webView.loadDataWithBaseURL(null, getHtmlFromAsset("Terms_And_Conditions.html"), "text/html", "utf-8", null);

        // Set the WebView as the message content
        alertDialogBuilder.setView(webView);


        alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.show();
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder feedbackDialog = new AlertDialog.Builder(this);
        feedbackDialog.setTitle("Feedback");
        feedbackDialog.setMessage("Please loging it!");
        feedbackDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Start the FeedbackActivity here or perform any action you want
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });

        feedbackDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = feedbackDialog.create();
        dialog.show();
    }

    private void showAddServiceDialog() {
        // Check if the user is logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is logged in, replace the current fragment with the serviceForm fragment
            loadFragment(new serviceForm());
        } else {
            // User is not logged in, prompt them to log in
            AlertDialog.Builder loginDialog = new AlertDialog.Builder(this);
            loginDialog.setTitle("Login Required");
            loginDialog.setMessage("You need to log in to add a service. Do you want to log in now?");
            loginDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                }
            });
            loginDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            loginDialog.show();
        }
    }

    // Method to load the first fragment
    private void loadFirstFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    // Method to load a fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private String getHtmlFromAsset(String filename) {
        try {
            // Read the HTML content from the asset file
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to load HTML content";
        }
    }
}
