package com.example.cecs453finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static final String TAG = "TransactionDAO";

    private Context mContext;

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_TRANSACTION_ID,
            DBHelper.COLUMN_TRANSACTION_DATE,
            DBHelper.COLUMN_TRANSACTION_DESCRIPTION,
            DBHelper.COLUMN_TRANSACTION_CATEGORY,
            DBHelper.COLUMN_TRANSACTION_TYPE,
            DBHelper.COLUMN_TRANSACTION_AMOUNT };

    public TransactionDAO(Context context)
    {

        mDbHelper = new DBHelper(context);
        this.mContext = context;

        try
        {
            openDB();
        }catch (SQLException e)
        {
            Log.e(TAG, "SQLException while opening database + " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openDB() throws SQLException
    {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void closeDB()
    {
        mDbHelper.close();
    }

    public Transaction createTransaction(String date, String descr, String category, int type, double amt)
    {

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TRANSACTION_DATE, date);
        values.put(DBHelper.COLUMN_TRANSACTION_DESCRIPTION, descr);
        values.put(DBHelper.COLUMN_TRANSACTION_CATEGORY, category);
        values.put(DBHelper.COLUMN_TRANSACTION_TYPE, type);
        values.put(DBHelper.COLUMN_TRANSACTION_AMOUNT, amt);
        long insertId = mDatabase
                .insert(DBHelper.TABLE_TRANSACTIONS, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_TRANSACTIONS, mAllColumns,
                DBHelper.COLUMN_TRANSACTION_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Transaction newTransaction = cursorToTransaction(cursor);
        cursor.close();
        return newTransaction;
    }

    public void deleteTransaction(Transaction transaction)
    {
        long id = transaction.getId();
        Log.d(TAG, "Transactoin deleted with id: " + id);
        mDatabase.delete(DBHelper.TABLE_TRANSACTIONS,
                DBHelper.COLUMN_TRANSACTION_ID + " = " + id, null);

    }

    public List<Transaction> getUserTransactions(long userID)
    {
        List<Transaction> listTransaction = new ArrayList<Transaction>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_TRANSACTIONS, mAllColumns,
                null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Transaction transaction = cursorToTransaction(cursor);
            listTransaction.add(transaction);
            cursor.moveToNext();
        }
        cursor.close();
        return listTransaction;

    }

    private Transaction cursorToTransaction(Cursor cursor)
    {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getLong(0));
        transaction.setDate(cursor.getString(1));
        transaction.setDescr(cursor.getString(2));
        transaction.setCategory(cursor.getString(3));
        transaction.setType(cursor.getInt(4));
        transaction.setAmount(cursor.getDouble(5));

        long userID = cursor.getLong(6);
        UsersDAO dao = new UsersDAO(mContext);
        User user = dao.getUserByID(userID);
        if (user != null)
        {
            transaction.setUser(user);
        }
        return transaction;
    }

}