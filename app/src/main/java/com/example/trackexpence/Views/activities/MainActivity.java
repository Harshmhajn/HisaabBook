package com.example.trackexpence.Views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.trackexpence.Adapter.CategoryAdapter;
import com.example.trackexpence.Models.Category;
import com.example.trackexpence.R;
import com.example.trackexpence.Views.fragments.StatsFragment;
import com.example.trackexpence.Views.fragments.TransactionsFragment;
import com.example.trackexpence.databinding.ActivityMainBinding;
import com.example.trackexpence.databinding.CategoryDialogBinding;
import com.example.trackexpence.utils.Constants;
import com.example.trackexpence.viewmodel.MainViewModel;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Calendar calendar;


    
    
    public  MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transaction");
        Constants.setCategories();
        calendar = Calendar.getInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,new TransactionsFragment());

        transaction.commit();


        binding.bottomNavigationView2.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.transaction){
                    Constants.SELECTED_TAB = 0;
                    Constants.SELCETED_TAB_BOTTOM = 0;
                    getSupportFragmentManager().popBackStack();
                } if (item.getItemId() == R.id.stats){
                    Constants.SELCETED_TAB_BOTTOM = 1;
                    transaction.replace(R.id.content,new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });



    }


    public  void getTransaction(){
        viewModel.getTransactions(calendar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);


        MenuItem searchItem  = menu.findItem(R.id.search);
        if (Constants.SELCETED_TAB_BOTTOM == 0) {
            searchItem.setOnMenuItemClickListener(c -> {
                CategoryDialogBinding dialogBinding = CategoryDialogBinding.inflate(getLayoutInflater());
                AlertDialog categoryAlertDialog = new AlertDialog.Builder(this).create();
                categoryAlertDialog.setView(dialogBinding.getRoot());


                CategoryAdapter categoryAdapter = new CategoryAdapter(this, Constants.categories, new CategoryAdapter.CategoryClickListner() {
                    @Override
                    public void onCategotyClick(Category category) {

                        viewModel.getTransactionForSearch(calendar, category);
                        categoryAlertDialog.dismiss();
                    }
                });
                dialogBinding.recylcerView.setLayoutManager(new GridLayoutManager(this, 2));
                dialogBinding.recylcerView.setAdapter(categoryAdapter);

                categoryAlertDialog.show();
                return false;
            });
        }

        return super.onCreateOptionsMenu(menu);
    }
}