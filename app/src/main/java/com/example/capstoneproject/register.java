package com.example.capstoneproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;

public class register extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button btnRegister;
    private TextView goLogin;
    private CheckBox checkBoxTC;
    private MaterialAlertDialogBuilder alertDialogBuilder;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        editTextEmail = findViewById(R.id.userName);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.cpassword);
        btnRegister = findViewById(R.id.btn_reg);
        goLogin = findViewById(R.id.Loginnow);
        checkBoxTC = findViewById(R.id.check_TC);

        btnRegister.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        alertDialogBuilder = new MaterialAlertDialogBuilder(this);

        // Set up the Terms and Conditions checkbox listener
        checkBoxTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleTermsAndConditionsChecked(isChecked);
            }
        });

        // Set up the Login button listener
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });

        // Set up the Register button listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void handleTermsAndConditionsChecked(boolean isChecked) {
        if (isChecked) {
            showTermsAndConditionsDialog();
        } else {
            btnRegister.setEnabled(false);
        }
    }



    private void showTermsAndConditionsDialog() {
        alertDialogBuilder.setTitle("Terms And Conditions");

        // Create a WebView to render HTML content
        WebView webView = new WebView(this);
        webView.loadDataWithBaseURL(null, getHtmlFromAsset("Terms_And_Conditions.html"), "text/html", "utf-8", null);

        // Set the WebView as the message content
        alertDialogBuilder.setView(webView);

        alertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btnRegister.setEnabled(true);
                checkBoxTC.setChecked(true); // Set the checkbox as checked when terms are accepted
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkBoxTC.setChecked(false);
            }
        });

        alertDialogBuilder.show();
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



    private void navigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("All Fields are Required");
        } else if (password.length() < 7) {
            showToast("Password Should be at Least 7 Characters");
        } else if (!confirmPassword.equals(password)) {
            showToast("Passwords do not match");
        } else {
            // Register the user with Firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            handleRegistrationResult(task);
                        }
                    });
        }
    }

    private void handleRegistrationResult(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            showToast("Registration Successful");
            sendEmailVerification();
        } else {
            showToast("Failed To Register. Please try again.");
        }
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    showToast("Verification Email is Sent, Verify and Log In Again");
                    firebaseAuth.signOut();
                    navigateToLogin();
                }
            });
        } else {
            showToast("Failed To Send Verification Email");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
