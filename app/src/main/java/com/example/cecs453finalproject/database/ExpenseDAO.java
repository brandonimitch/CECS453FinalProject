package com.example.cecs453finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private static final String TAG = "ExpenseDAO";

    private Context mContext;

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_CATEGORY_ID,
            DBHelper.COLUMN_CATEGORY_NAME,
            DBHelper.COLUMN_CATEGORY_USER_ID };


    public ExpenseDAO(Context context) {

        mDbHelper = new DBHelper(context);
        this.mContext = context;

        try {

            openDB();

        }catch (SQLException e) {

            Log.e(TAG, "SQLException while opening database + " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void openDB() {
        mDatabase = mDbHelper.getWritableDatabase();
    }


    public void closeDB() {
        mDbHelper.close();
    }


    public Expense editOrCreateExpense(long userID, String name, Double amount) {

        Expense expense = new Expense();
        boolean newCategory = false;

        CategoryDAO category = new CategoryDAO(mContext);
        List<Category> currentCategories = category.getUserCategories(userID);

        for (Category cat: currentCategories) {

            if (cat.getName().equals(name)) {

                expense.setExpense(amount);

            } else {

                newCategory = true;
            }
        }

        if (newCategory) {

            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_CATEGORY_NAME, name);
            values.put(DBHelper.COLUMN_CATEGORY_USER_ID, userID);
            long insertId = mDatabase
                    .insert(DBHelper.TABLE_CATEGORIES, null, values);
            Cursor cursor = mDatabase.query(DBHelper.TABLE_CATEGORIES, mAllColumns,
                    DBHelper.COLUMN_CATEGORY_ID + " = " + insertId, null, null,
                    null, null);
            cursor.moveToFirst();
            Category newCategory = cursorToCategory(cursor);
            cursor.close();

//            expense.setExpense(amount);

            return newCategory;
        }

        return null;
    }


    public List<Expense> getExpense(String name) {

        List<Expense> listExpense = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT* FROM " + DBHelper.TABLE_CATEGORIES, null);      // TABLE_EXPENSE??? NO OPTION HERE

        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Category category = cursorToCategory(cursor);
                listExpense.add(category);
                cursor.moveToNext();
            }
        }
        else
        {
            listExpense.add(new Expense());
        }

        cursor.close();

        return listExpense;
    }



}
