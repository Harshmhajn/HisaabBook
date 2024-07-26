package com.example.trackexpence.Views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trackexpence.Adapter.AccountsAdapter;
import com.example.trackexpence.Adapter.CategoryAdapter;
import com.example.trackexpence.Models.Account;
import com.example.trackexpence.Models.Category;
import com.example.trackexpence.Models.Transaction;
import com.example.trackexpence.R;
import com.example.trackexpence.Views.activities.MainActivity;
import com.example.trackexpence.databinding.CategoryDialogBinding;
import com.example.trackexpence.databinding.FragmentAddTransactionBinding;
import com.example.trackexpence.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {


    public AddTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();
        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenceBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenceBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            transaction.setType(Constants.INCOME);
        });

        binding.expenceBtn.setOnClickListener(view -> {
            binding.expenceBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenceBtn.setTextColor(getContext().getColor(R.color.red));
            transaction.setType(Constants.EXPENCE);
        });

        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker,i1,i2,i3) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH,datePicker.getMonth());
                calendar.set(Calendar.YEAR,datePicker.getYear());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY");
                String dateToShow = dateFormat.format(calendar.getTime());
                binding.date.setText(dateToShow);

                transaction.setDate(calendar.getTime());
                transaction.setId(calendar.getTime().getTime());
            });
            datePickerDialog.show();
        });

        binding.category.setOnClickListener(c -> {
            CategoryDialogBinding dialogBinding = CategoryDialogBinding.inflate(inflater);
            AlertDialog categoryAlertDialog = new AlertDialog.Builder(getContext()).create();
            categoryAlertDialog.setView(dialogBinding.getRoot());


            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListner() {
                @Override
                public void onCategotyClick(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryAlertDialog.dismiss();
                }
            });
            dialogBinding.recylcerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recylcerView.setAdapter(categoryAdapter);

            categoryAlertDialog.show();

        });

        binding.account.setOnClickListener(c -> {
            CategoryDialogBinding dialogBinding = CategoryDialogBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0,"Cash"));
            accounts.add(new Account(0,"Card"));
            accounts.add(new Account(0,"UPI"));
            accounts.add(new Account(0,"Other"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccounsClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccount_name());
                    transaction.setAccount(account.getAccount_name());
                    accountDialog.dismiss();
                }
            });
            dialogBinding.recylcerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recylcerView.setAdapter(adapter);

            accountDialog.show();
        });


        binding.saveTransaction.setOnClickListener(c -> {
           try {
               double amount = Double.parseDouble(binding.amount.getText().toString());
               String note = binding.note.getText().toString();

               if (transaction.getType().equals(Constants.EXPENCE)) {
                   transaction.setAmount(amount * -1);
               } else if (transaction.getType().equals(Constants.INCOME)) {
                   transaction.setAmount(amount);
               }
               transaction.setNote(note);
               ((MainActivity) getActivity()).viewModel.addTransactions(transaction);
               ((MainActivity) getActivity()).getTransaction();
               dismiss();
           }catch (Exception e){
               Toast.makeText(getContext(), "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
           }



        });
        return binding.getRoot();
    }
}