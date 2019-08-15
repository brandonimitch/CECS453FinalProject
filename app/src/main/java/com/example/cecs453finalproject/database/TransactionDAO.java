package com.example.cecs453finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Transaction;
import com.example.cecs453finalproject.classes.User;

import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static final String TAG = "TransactionDAO";

    private Context mContext;
    private CategoryDAO mCategoryDAO;
    private List<Category> mCategoryList;

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_TRANSACTION_ID,
            DBHelper.COLUMN_TRANSACTION_DATE,
            DBHelper.COLUMN_TRANSACTION_DESCRIPTION,
            DBHelper.COLUMN_TRANSACTION_CATEGORY,
            DBHelper.COLUMN_TRANSACTION_TYPE,
            DBHelper.COLUMN_TRANSACTION_AMOUNT,
            DBHelper.COLUMN_TRANSACTION_USER_ID};

    public TransactionDAO(Context context)
    {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        mCategoryDAO = new CategoryDAO(context);

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

    public Transaction createTransaction(long userID, String date, String description, String category, int type, double amt)
    {
        mCategoryList = mCategoryDAO.getUserCategories(userID);
        ArrayList<String> catStrings = new ArrayList<>();
        for (Category cat : mCategoryList)
        {
            catStrings.add(cat.getName());
        }

        // Create new Category if not in category list
        if (!catStrings.contains(category))
        {
            mCategoryDAO.createCategory(userID, category);
        }

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TRANSACTION_DATE, date);
        values.put(DBHelper.COLUMN_TRANSACTION_DESCRIPTION, description);
        values.put(DBHelper.COLUMN_TRANSACTION_CATEGORY, category);
        values.put(DBHelper.COLUMN_TRANSACTION_TYPE, type);
        values.put(DBHelper.COLUMN_TRANSACTION_AMOUNT, amt);
        values.put(DBHelper.COLUMN_TRANSACTION_USER_ID, userID);
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
        Log.d(TAG, "Transaction deleted with id: " + id);
        mDatabase.delete(DBHelper.TABLE_TRANSACTIONS,
                DBHelper.COLUMN_TRANSACTION_ID + " = " + id, null);
    }

    public void deleteAllUserTransactions(long userID)
    {
        mDatabase.delete(DBHelper.TABLE_TRANSACTIONS, DBHelper.COLUMN_TRANSACTION_USER_ID + "=" + Long.toString(userID), null);
    }

    public List<Transaction> getUserTransactions(long userID)
    {
        List<Transaction> listTransaction = new ArrayList<Transaction>();

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBHelper.TABLE_TRANSACTIONS
                + " WHERE " + DBHelper.COLUMN_TRANSACTION_USER_ID + " = ?",
                new String[]{Long.toString(userID)});

        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Transaction transaction = cursorToTransaction(cursor);
                listTransaction.add(transaction);
                cursor.moveToNext();
            }

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

    public boolean updateTransaction(long transactionId, String date, String desc, String category,
                                     int type, double amount)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_TRANSACTION_DATE, date);
        contentValues.put(DBHelper.COLUMN_TRANSACTION_DESCRIPTION, desc);
        contentValues.put(DBHelper.COLUMN_TRANSACTION_CATEGORY, category);
        contentValues.put(DBHelper.COLUMN_TRANSACTION_TYPE, type);
        contentValues.put(DBHelper.COLUMN_TRANSACTION_AMOUNT, amount);
        try {
            mDatabase.update(DBHelper.TABLE_TRANSACTIONS,
                    contentValues,
                    DBHelper.COLUMN_TRANSACTION_ID + " = ?",
                    new String[]{Long.toString(transactionId)});
            return true;
        } catch (SQLException e)
        {
            Log.e(TAG, "SQLException while updating database + " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}