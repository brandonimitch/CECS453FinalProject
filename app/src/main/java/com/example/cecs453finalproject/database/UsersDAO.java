package com.example.cecs453finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cecs453finalproject.classes.Transaction;
import com.example.cecs453finalproject.classes.User;

import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    public static final String TAG = "UsersDAO";

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_USER_ID,
            DBHelper.COLUMN_USERNAME,
            DBHelper.COLUMN_USER_PASSWORD,
            DBHelper.COLUMN_USER_EMAIL };

    public UsersDAO(Context context)
    {

        this.mContext = context;
        mDbHelper = new DBHelper(context);

        try {
            openDB();
        } catch (SQLException e)
        {
            Log.e(TAG, "SQLite Exception on opening database " + e.getMessage());
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

    public User createUser(String username, String password, String email)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USERNAME, username);
        values.put(DBHelper.COLUMN_USER_PASSWORD, password);
        values.put(DBHelper.COLUMN_USER_EMAIL, email);
        long insertId = mDatabase
                .insert(DBHelper.TABLE_USERS, null, values);
        Cursor cursor = mDatabase
                .query(DBHelper.TABLE_USERS, mAllColumns,
                        DBHelper.COLUMN_USER_ID + " = " + insertId,
                        null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;

    }

    public void deleteUser(User user)
    {
        long id = user.getId();

        TransactionDAO transactionDAO = new TransactionDAO(mContext);
        List<Transaction> listTransaction = transactionDAO.getUserTransactions(id);

        if(listTransaction != null && !listTransaction.isEmpty())
        {
            for(Transaction t : listTransaction)
            {
                transactionDAO.deleteTransaction(t);
            }
        }

        Log.d(TAG, "User deleted with id " + id);
        mDatabase.delete(DBHelper.TABLE_USERS, DBHelper.COLUMN_USER_ID
                + " = " + id, null);
    }

    public List<User> getAllUsers()
    {

        List<User> listUser = new ArrayList<User>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBHelper.TABLE_USERS, null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                User user = cursorToUser(cursor);
                listUser.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return listUser;

    }

    public User getUserByID(long id)
    {

        Cursor cursor = mDatabase.query(DBHelper.TABLE_USERS, mAllColumns,
                DBHelper.COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);

        User user;

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            user = cursorToUser(cursor);
        }
        else
        {
            user = null;
        }

        return user;

    }

    public User getUserByUsername(String username)
    {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_USERS, mAllColumns,
                DBHelper.COLUMN_USERNAME + " = ?",
                new String[] { username }, null, null, null);
        User user;

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            user = cursorToUser(cursor);
        }
        else
        {
            user = null;
        }

        return user;
    }

    protected User cursorToUser(Cursor cursor)
    {

        User user = new User();
        user.setId(cursor.getLong(0));
        user.setUsername(cursor.getString(1));
        user.setPassword(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        return user;

    }


}


