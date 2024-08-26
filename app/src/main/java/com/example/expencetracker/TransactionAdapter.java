package com.example.expencetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expencetracker.Model.Data;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Data> transactionList;

    public TransactionAdapter(List<Data> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data transaction = transactionList.get(position);

        // Set the amount and adjust color based on value
        double amount = transaction.getAmount();
        holder.amountTextView.setText(String.format("Rs %.2f", amount));
        if (amount < 0) {
            holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        } else {
            holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        }

        // Set other fields
        holder.typeTextView.setText(transaction.getType());
        holder.descriptionTextView.setText(transaction.getNote());
        holder.dateTextView.setText(transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amountTextView;
        public TextView typeTextView;
        public TextView descriptionTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
