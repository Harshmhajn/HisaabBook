package com.example.trackexpence.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackexpence.Models.Category;
import com.example.trackexpence.Models.Transaction;
import com.example.trackexpence.R;
import com.example.trackexpence.Views.activities.MainActivity;
import com.example.trackexpence.databinding.RowTransactionBinding;
import com.example.trackexpence.utils.Constants;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    Context context;
    RealmResults<Transaction> transactions;
    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions){
        this.context = context;
        this.transactions = transactions;
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {

        Transaction transaction = transactions.get(i);
        transactionViewHolder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        transactionViewHolder.binding.transactionDate.setText(String.valueOf(transaction.getDate()));
        transactionViewHolder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategoty = Constants.getCategoryDetails(transaction.getCategory());

        transactionViewHolder.binding.categoryicon.setImageResource(transactionCategoty.getCategoryImage());

        if (transaction.getType().equals(Constants.INCOME)){
            transactionViewHolder.binding.transactionCategory.setTextColor(context.getColor(R.color.sky));
            transactionViewHolder.binding.transactionAmount.setTextColor(context.getColor(R.color.sky));
            transactionViewHolder.binding.categoryicon.setBackgroundTintList(context.getColorStateList(R.color.sky));
        }else if (transaction.getType().equals(Constants.EXPENCE)){
            transactionViewHolder.binding.transactionCategory.setTextColor(context.getColor(R.color.red));
            transactionViewHolder.binding.transactionAmount.setTextColor(context.getColor(R.color.red));
            transactionViewHolder.binding.categoryicon.setBackgroundTintList(context.getColorStateList(R.color.red));
            transactionViewHolder.binding.account.setBackgroundTintList(context.getColorStateList(R.color.red));
        }

        transactionViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are You Sure to Delete The Transaction");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity)context).viewModel.deleteTransaction(transaction);
                    }
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
