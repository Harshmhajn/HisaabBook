package com.example.trackexpence.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackexpence.Models.Category;
import com.example.trackexpence.Models.Transaction;
import com.example.trackexpence.Views.fragments.TransactionsFragment;
import com.example.trackexpence.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    public  MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public  MutableLiveData<RealmResults<Transaction>> categoriesTransactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpence = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount= new MutableLiveData<>();


    Realm realm;

    Calendar calendar;
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setUpDatabase();
    }


    public  void getTransactions(Calendar calendar,String type) {
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date temp = calendar.getTime();

        RealmResults<Transaction> newTransactions = null;
        if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {

            newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type",Constants.SELECTED_TAB_TYPE).findAll();


        } else if (Constants.SELECTED_TAB_STATS == Constants.MONTH) {

            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();
            newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).equalTo("type",Constants.SELECTED_TAB_TYPE).findAll();



        }
        calendar.setTime(temp);
        categoriesTransactions.setValue(newTransactions);

    }
    public  void getTransactions(Calendar calendar) {
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date temp = calendar.getTime();
        double income = 0;
        double expence = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;
        if (Constants.SELECTED_TAB == Constants.DAILY) {

             newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).findAll();

             income = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type", Constants.INCOME).sum("amount").doubleValue();
             expence = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type", Constants.EXPENCE).sum("amount").doubleValue();
            total = realm.where(Transaction.class).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).sum("amount").doubleValue();

        } else if (Constants.SELECTED_TAB == Constants.MONTH) {

            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();
             newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).findAll();


            income =realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).equalTo("type", Constants.INCOME).sum("amount").doubleValue();
            expence = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).equalTo("type", Constants.EXPENCE).sum("amount").doubleValue();
            total = realm.where(Transaction.class).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).sum("amount").doubleValue();

        }
        else if (Constants.SELECTED_TAB == Constants.All){
            calendar.set(Calendar.DAY_OF_YEAR,0);
            Date start = calendar.getTime();
            calendar.add(Calendar.YEAR,1);
            Date end = calendar.getTime();
            newTransactions = realm.where(Transaction.class).greaterThanOrEqualTo("date", start).lessThan("date", end).findAll();
            income = realm.where(Transaction.class).greaterThanOrEqualTo("date", start).lessThan("date", end).equalTo("type", Constants.INCOME).sum("amount").doubleValue();
            expence = realm.where(Transaction.class).greaterThanOrEqualTo("date", start).lessThan("date", end).equalTo("type", Constants.EXPENCE).sum("amount").doubleValue();
            total = realm.where(Transaction.class).greaterThanOrEqualTo("date", start).lessThan("date", end).sum("amount").doubleValue();

        }


        calendar.setTime(temp);
        totalIncome.setValue(income);
        totalExpence.setValue(expence);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);

    }

    public void getTransactionForSearch(Calendar calendar, Category category){
        if (category.getCategoryName() == "Nothing"){
            getTransactions(calendar);
            return;
        }
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date temp = calendar.getTime();
        double income = 0;
        double expence = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;
        if (Constants.SELECTED_TAB == Constants.DAILY) {

            newTransactions = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).findAll();
            income = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type", Constants.INCOME).sum("amount").doubleValue();
            expence = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).equalTo("type", Constants.EXPENCE).sum("amount").doubleValue();
            total = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", calendar.getTime()).lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000))).sum("amount").doubleValue();

        } else if (Constants.SELECTED_TAB == Constants.MONTH) {

            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();
            newTransactions = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).findAll();


            income =realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).equalTo("type", Constants.INCOME).sum("amount").doubleValue();
            expence = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).equalTo("type", Constants.EXPENCE).sum("amount").doubleValue();
            total = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", startTime).lessThan("date", endTime).sum("amount").doubleValue();

        }
        else if (Constants.SELECTED_TAB == Constants.All){
            calendar.set(Calendar.DAY_OF_YEAR,0);
            Date start = calendar.getTime();
            calendar.add(Calendar.YEAR,1);
            Date end = calendar.getTime();
            newTransactions = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", start).lessThan("date", end).findAll();
            income = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", start).lessThan("date", end).equalTo("type", Constants.INCOME).sum("amount").doubleValue();
            expence = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", start).lessThan("date", end).equalTo("type", Constants.EXPENCE).sum("amount").doubleValue();
            total = realm.where(Transaction.class).equalTo("category",category.getCategoryName()).greaterThanOrEqualTo("date", start).lessThan("date", end).sum("amount").doubleValue();

        }


        calendar.setTime(temp);
        totalIncome.setValue(income);
        totalExpence.setValue(expence);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);

    }
    public void addTransactions(Transaction transaction){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
       realm.commitTransaction();

    }
    public  void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }
    void setUpDatabase(){
        realm = Realm.getDefaultInstance();
    }


}
