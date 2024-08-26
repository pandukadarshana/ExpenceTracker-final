package com.example.expencetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expencetracker.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView balanceTextView;
    private TextView budgetTextView;
    private TextView expenseTextView;
    private FloatingActionButton addBtn;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Data> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mUser.getUid();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        balanceTextView = findViewById(R.id.balance);
        budgetTextView = findViewById(R.id.budget);
        expenseTextView = findViewById(R.id.expense);
        addBtn=findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddTransactionActivity.class));
            }
        });

        fetchTransactions(uid);

    }

    private void fetchTransactions(String uid) {
        db.collection("ExpenceData").document(uid).collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double totalAmount = 0.0;
                        double budgetAmount = 0.0;
                        double expenseAmount = 0.0;

                        QuerySnapshot querySnapshot = task.getResult();
                        transactionList.clear(); // Clear the existing list

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Data transaction = document.toObject(Data.class);
                            transactionList.add(transaction);

                            double amount = transaction.getAmount();
                            totalAmount += amount;
                            if (amount > 0) {
                                budgetAmount += amount;
                            }
                        }

                        expenseAmount = totalAmount - budgetAmount;

                        balanceTextView.setText(String.format("Rs %.2f", totalAmount));
                        budgetTextView.setText(String.format("Rs %.2f", budgetAmount));
                        expenseTextView.setText(String.format("Rs %.2f", expenseAmount));

                        adapter.notifyDataSetChanged(); // Notify the adapter about data changes

                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        Toast.makeText(getApplicationContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
