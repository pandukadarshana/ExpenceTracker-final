package com.example.expencetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {
    private TextInputLayout emailLayout;
    private TextInputEditText emailInput;
    private Button resetPasswordBtn;
    private ImageButton closeBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset);
        emailLayout = findViewById(R.id.emailLayout);
        emailInput = findViewById(R.id.emailInput);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        closeBtn = findViewById(R.id.closeBtn);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        closeBtn.setOnClickListener(v -> finish());

        resetPasswordBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                emailLayout.setError("Email is required");
            } else {
                emailLayout.setError(null);
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ResetActivity.this, "Error sending password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}