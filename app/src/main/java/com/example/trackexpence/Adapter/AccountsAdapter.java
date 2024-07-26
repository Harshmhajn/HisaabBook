package com.example.trackexpence.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackexpence.Models.Account;
import com.example.trackexpence.R;
import com.example.trackexpence.databinding.RowAccountsBinding;

import java.util.ArrayList;

public class AccountsAdapter  extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>{
    Context context;
    ArrayList<Account> accounts;

    public  interface AccounsClickListener{
        void onAccountSelected(Account account);
    }
    AccounsClickListener accounsClickListener;
    public AccountsAdapter(Context context, ArrayList<Account> accounts,AccounsClickListener accounsClickListener){
        this.accounts = accounts;
        this.context = context;
        this.accounsClickListener = accounsClickListener;
    }
    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_accounts,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder accountsViewHolder, int i) {
        Account account = accounts.get(i);
        accountsViewHolder.binding.accountName.setText(account.getAccount_name());
        accountsViewHolder.itemView.setOnClickListener(c -> {
            accounsClickListener.onAccountSelected(account);
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public  class AccountsViewHolder extends RecyclerView.ViewHolder{
        RowAccountsBinding binding;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountsBinding.bind(itemView);
        }
    }
}
