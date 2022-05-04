package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.ACCOUNT_TABLE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_ACCOUNT_HOLDER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_ACCOUNT_NUMBER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_BALANCE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_BANK_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_EXPENSE_TYPE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler;

public class PersistentAccountDAO implements AccountDAO {

    private final DatabaseHandler dbHandler;

    public PersistentAccountDAO(Context context){
        dbHandler = DatabaseHandler.getInstance(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();
        String selectAccountNumberQuery = "SELECT " + COLUMN_ACCOUNT_NUMBER +" FROM " + ACCOUNT_TABLE;

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectAccountNumberQuery, null);

        if(cursor.moveToFirst()){
            do{
                accountNumbers.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                int balance = cursor.getInt(3);

                accounts.add(new Account(accountNo, bankName, accountHolderName, balance));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        Account returnAccount = null;

        String selectAccountQuery = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NUMBER + "=" + "'" + accountNo + "'";

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectAccountQuery, null);

        if(cursor.moveToFirst()){
            returnAccount = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) );
        }

        cursor.close();
        db.close();

        return returnAccount;
    }

    @Override
    public void addAccount(Account account){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NUMBER, account.getAccountNo());
        cv.put(COLUMN_BANK_NAME, account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE, account.getBalance());

        db.insert(ACCOUNT_TABLE, null, cv);

        db.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String[] accountNumbers = {accountNo};

        db.delete(ACCOUNT_TABLE, "ACCOUNT_NUMBER=?", accountNumbers);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = getAccount(accountNo);

        switch (expenseType) {
            case EXPENSE:
                amount = account.getBalance() - amount;
                break;
            case INCOME:
                amount = account.getBalance() + amount;
                break;
        }

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String updateQuery = "UPDATE " + ACCOUNT_TABLE + " SET " + COLUMN_BALANCE + " = "+ amount + " WHERE " + COLUMN_ACCOUNT_NUMBER + " = " + "'" + accountNo + "'";

        db.execSQL(updateQuery);
    }
}
