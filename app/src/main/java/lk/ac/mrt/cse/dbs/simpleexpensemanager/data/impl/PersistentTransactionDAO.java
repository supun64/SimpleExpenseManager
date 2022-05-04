package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_ACCOUNT_NUMBER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_AMOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_DATE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.COLUMN_EXPENSE_TYPE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler.TRANSACTION_TABLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHandler;

public class PersistentTransactionDAO implements TransactionDAO {

    private final DatabaseHandler dbHandler;

    public PersistentTransactionDAO(Context context){
        dbHandler = DatabaseHandler.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        cv.put(COLUMN_DATE, dateFormat.format(date));
        cv.put(COLUMN_ACCOUNT_NUMBER, accountNo);
        cv.put(COLUMN_EXPENSE_TYPE, String.valueOf(expenseType));
        cv.put(COLUMN_AMOUNT, String.valueOf(amount));

        db.insert(TRANSACTION_TABLE, null, cv);

        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        List<Transaction> allTransaction = new ArrayList<>();
        String getAllQuery = "SELECT * FROM " + TRANSACTION_TABLE;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Cursor cursor = db.rawQuery(getAllQuery, null);
        Date date = null;


        if(cursor.moveToFirst()){

            do{

                try {
                    date = dateFormat.parse(cursor.getString(1).toString());
                } catch (ParseException e) {
                }

                allTransaction.add(new Transaction(date, cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), cursor.getDouble(4)));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return allTransaction;


    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = this.getAllTransactionLogs();
        int size = transactions.size();

        if(size <= limit){
            return transactions;
        }

        return transactions.subList(size - limit, size);
    }
}
