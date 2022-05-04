package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DatabaseHandler extends SQLiteOpenHelper {


    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String COLUMN_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String COLUMN_BANK_NAME = "BANK_NAME";
    public static final String COLUMN_ACCOUNT_HOLDER = "ACCOUNT_HOLDER";
    public static final String COLUMN_BALANCE = "BALANCE";

    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String COLUMN_TRANSACTION_ID = "TRANSACTION_ID";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_EXPENSE_TYPE = "EXPENSE_TYPE";
    public static final String COLUMN_AMOUNT = "AMOUNT";

    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHandler(context);
        }
        return instance;
    }


    public DatabaseHandler(@Nullable Context context) {
        super(context, "190375K.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccountTableQuery = "CREATE TABLE " + ACCOUNT_TABLE + " (" + COLUMN_ACCOUNT_NUMBER + " VARCHAR PRIMARY KEY, " + COLUMN_BANK_NAME + " VARCHAR, " + COLUMN_ACCOUNT_HOLDER + " VARCHAR, " + COLUMN_BALANCE + " INTEGER)";
        String createTransactionTableQuery = "CREATE TABLE " + TRANSACTION_TABLE + " (" + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " DATE, " + COLUMN_ACCOUNT_NUMBER + " INT, " + COLUMN_EXPENSE_TYPE + " VARCHAR, " + COLUMN_AMOUNT + " INTEGER, FOREIGN KEY (" + COLUMN_ACCOUNT_NUMBER + ") REFERENCES "+ ACCOUNT_TABLE +"(" + COLUMN_ACCOUNT_NUMBER + "))";

        db.execSQL(createAccountTableQuery);
        db.execSQL(createTransactionTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
