package com.example.expencetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
private EditText mEmail;
    private EditText mPass;
    private Button btnReg;
    private TextView mSignin;
    private ProgressDialog mDialog;

//    firebase..
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        registration();
    }

    private void registration(){
        mEmail=findViewById(R.id.email_reg);
        mPass=findViewById(R.id.password_reg);
        btnReg=findViewById(R.id.btn_reg);
        mSignin=findViewById(R.id.sing_here);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String pass=mPass.getText().toString().trim();
                if(TextUtils.isEmpty(pass)){
                    mPass.setError("password is required...");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required...");
                    return;
                }
                mDialog.setMessage("processing..");
                mDialog.show();
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else{
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Faill ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }
}