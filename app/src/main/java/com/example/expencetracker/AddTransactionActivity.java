package com.example.expencetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expencetracker.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTransactionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button addBtn;
    private EditText mamount;
    private EditText mlable;
    private EditText mdescription;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mUser.getUid();
        db = FirebaseFirestore.getInstance();

        mamount = findViewById(R.id.amountInput);
        mlable = findViewById(R.id.labelInput);
        mdescription = findViewById(R.id.descriptionInput);
        addBtn = findViewById(R.id.addTransactionBtn);
        progressBar = findViewById(R.id.progressBar);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoader();
                String id = db.collection("ExpenceData").document(uid).collection("transactions").document().getId();
                String mdate = DateFormat.getDateInstance().format(new Date());
                int amount = Integer.parseInt(mamount.getText().toString());
                String description = mdescription.getText().toString();
                String lable=mlable.getText().toString();
                Data data = new Data(amount, lable, description, id, mdate);

                db.collection("ExpenceData").document(uid).collection("transactions").document(id).set(data)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                hideLoader();
                                Toast.makeText(getApplicationContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Exception e = task.getException();
                                if (e != null) {
                                    Log.e("FirebaseError", "Failed to add data", e);
                                    Toast.makeText(getApplicationContext(), "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to add data: Unknown error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

    private void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }
}
