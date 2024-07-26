package com.example.trackexpence.utils;

import com.example.trackexpence.Models.Category;
import com.example.trackexpence.R;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "Income";
    public static  String EXPENCE = "Expence";

    public static int SELECTED_TAB  = 0;
    public static int SELCETED_TAB_BOTTOM = 0;
    public static int SELECTED_TAB_STATS  = 0;
    public static  String SELECTED_TAB_TYPE = INCOME;
    public static int DAILY  = 0;
    public static int MONTH  = 1;
    public static int All  = 2;

    public  static ArrayList<Category> categories;
    public static void  setCategories(){

         categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.ic_salary));
        categories.add(new Category("Bussiness",R.drawable.ic_business));
        categories.add(new Category("Investment",R.drawable.ic_investment));
        categories.add(new Category("Loan",R.drawable.ic_loan));
        categories.add(new Category("Rent",R.drawable.ic_rent));
        categories.add(new Category("Other",R.drawable.ic_other));
        categories.add(new Category("Nothing",R.drawable.ic_other));
    }

    public static Category getCategoryDetails(String name){
        for (Category cat: categories) {
            if (cat.getCategoryName().equals(name)){
                return cat;
            }
        }
        return null;
    }
}
