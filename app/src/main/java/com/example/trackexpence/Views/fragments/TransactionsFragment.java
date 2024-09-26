package com.example.trackexpence.Views.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trackexpence.Adapter.TransactionsAdapter;
import com.example.trackexpence.Models.Transaction;
import com.example.trackexpence.R;
import com.example.trackexpence.Views.activities.MainActivity;
import com.example.trackexpence.databinding.ActivityMainBinding;
import com.example.trackexpence.databinding.FragmentAddTransactionBinding;
import com.example.trackexpence.databinding.FragmentTransactionsBinding;
import com.example.trackexpence.utils.Constants;
import com.example.trackexpence.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.RealmResults;

public class TransactionsFragment extends Fragment {
    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentTransactionsBinding binding;
    public MainViewModel viewModel;
    Calendar calendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        calendar = Calendar.getInstance();

        updateDate();
        binding.nextDateBtn.setOnClickListener(c ->{
            if (Constants.SELECTED_TAB == 0)
                calendar.add(Calendar.DATE,1);
            else if (Constants.SELECTED_TAB == 1)
                calendar.add(Calendar.MONTH,1);
            else if (Constants.SELECTED_TAB == 2){
                calendar.add(Calendar.YEAR,1);
            }
            updateDate();
        });




        binding.previousDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB == 0)
                calendar.add(Calendar.DATE,-1);
            else if (Constants.SELECTED_TAB == 1)
                calendar.add(Calendar.MONTH,-1);
            else if (Constants.SELECTED_TAB == 2){
                calendar.add(Calendar.YEAR,-1);
            }
            updateDate();
        });
        binding.floatingActionButton.setOnClickListener(c -> {
            new AddTransactionFragment().show(getParentFragmentManager(),null);
        });



        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Month")){
                    Constants.SELECTED_TAB = 1;
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = 0;
                } else if (tab.getText().equals("All")) {
                    Constants.SELECTED_TAB = 2;
                }

                updateDate();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(),transactions);
                binding.transactionList.setAdapter(transactionsAdapter);

            }
        });

        binding.currentdate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker,i1,i2,i3) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH,datePicker.getMonth());
                calendar.set(Calendar.YEAR,datePicker.getYear());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY");
                String dateToShow = dateFormat.format(calendar.getTime());
                binding.currentdate.setText(dateToShow);

                viewModel.getTransactions(calendar);
            });
            datePickerDialog.show();
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpence.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenceLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalAmt.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);


        return binding.getRoot();
    }
    void updateDate() {

        SimpleDateFormat dateFormat ;
        if(Constants.SELECTED_TAB == 0){
            dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            binding.currentdate.setText(dateFormat.format(calendar.getTime()));
        }
        else if (Constants.SELECTED_TAB == 1){
            dateFormat = new SimpleDateFormat("MMMM, yyyy");
            binding.currentdate.setText(dateFormat.format(calendar.getTime()));
        }else if (Constants.SELECTED_TAB == 2){
            dateFormat = new SimpleDateFormat("yyyy");
            binding.currentdate.setText(dateFormat.format(calendar.getTime()));
        }

        viewModel.getTransactions(calendar);
    }

}