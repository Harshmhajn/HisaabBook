package com.example.trackexpence.Views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.trackexpence.Models.Category;
import com.example.trackexpence.Models.Transaction;
import com.example.trackexpence.R;
import com.example.trackexpence.databinding.FragmentStatsBinding;
import com.example.trackexpence.utils.Constants;
import com.example.trackexpence.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;


public class StatsFragment extends Fragment {

    public StatsFragment() {
        // Required empty public constructor
    }

    FragmentStatsBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MainViewModel viewModel;
    Calendar calendar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);


        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        calendar = Calendar.getInstance();

        updateDate();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenceBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenceBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            Constants.SELECTED_TAB_TYPE = Constants.INCOME;
            updateDate();
        });

        binding.expenceBtn.setOnClickListener(view -> {
            binding.expenceBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenceBtn.setTextColor(getContext().getColor(R.color.red));
            Constants.SELECTED_TAB_TYPE = Constants.EXPENCE;
            updateDate();
        });


        binding.nextDateBtn.setOnClickListener(c ->{
            if (Constants.SELECTED_TAB_STATS == 0)
                calendar.add(Calendar.DATE,1);
            else if (Constants.SELECTED_TAB_STATS == 1)
                calendar.add(Calendar.MONTH,1);
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c ->{
            if (Constants.SELECTED_TAB_STATS == 0)
                calendar.add(Calendar.DATE,-1);
            else if (Constants.SELECTED_TAB_STATS == 1)
                calendar.add(Calendar.MONTH,-1);
            updateDate();
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Month")){
                    Constants.SELECTED_TAB_STATS = 1;
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB_STATS = 0;
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





        Pie pie = AnyChart.pie();


        viewModel.categoriesTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                if (transactions.size() > 0){
                    binding.anychart.setVisibility(View.VISIBLE);
                    List<DataEntry> data = new ArrayList<>();
                    Map<String, Double> categoryList = new HashMap<>();

                    for (Transaction transaction: transactions) {
                        String category = transaction.getCategory();
                        double amount = transaction.getAmount();
                        if (categoryList.containsKey(category)){
                            double   curr = categoryList.get(category).doubleValue();
                            curr += amount;
                            categoryList.put(category,Math.abs(curr));
                        }
                        else {
                            categoryList.put(category,Math.abs(amount));
                        }
                    }
                    for (Map.Entry<String,Double> map : categoryList.entrySet()){
                        data.add(new ValueDataEntry(map.getKey(),map.getValue()));
                    }
                        pie.data(data);
                    binding.anychart.setVisibility(View.VISIBLE);
                }
                else {
                    binding.anychart.setVisibility(View.GONE);
                }
            }
        });



        if(pie != null) {
            binding.anychart.setChart(pie);
        }
        return binding.getRoot();
    }

    void updateDate() {

        SimpleDateFormat dateFormat ;
        if(Constants.SELECTED_TAB_STATS == 0){
            dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            binding.currentdate.setText(dateFormat.format(calendar.getTime()));
        }
        else{
            dateFormat = new SimpleDateFormat("MMMM, yyyy");
            binding.currentdate.setText(dateFormat.format(calendar.getTime()));
        }

        viewModel.getTransactions(calendar,Constants.SELECTED_TAB_TYPE);
    }

}