 package com.example.capstoneproject;

 import android.annotation.SuppressLint;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;

 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.FirebaseAuth;

 public class ForgotActivity extends AppCompatActivity {

     private EditText mforgotpassword;
     private Button mpasswordrecoverbutton;
     private TextView mgobacktologin;

     FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton);
        mgobacktologin=findViewById(R.id.gobacktologin);

        firebaseAuth= FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotActivity.this,login.class);
                startActivity(intent);
                finish();
            }
        });


        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your mail first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //we have to send password recover email

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Mail Sent , You can recover your password using mail",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotActivity.this,login.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Email is Wrong or Account Not Exist",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            }
        });

    }

}