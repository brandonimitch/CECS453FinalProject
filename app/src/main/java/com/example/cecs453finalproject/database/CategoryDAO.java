package com.example.cecs453finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.User;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private static final String TAG = "CategoryDAO";

    private Context mContext;

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_CATEGORY_ID,
            DBHelper.COLUMN_CATEGORY_NAME,
            DBHelper.COLUMN_CATEGORY_USER_ID};

    public CategoryDAO(Context context)
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

    public Category createCategory(long userID, String name)
    {
        List<Category> currentCategories = getUserCategories(userID);
         for (Category cat : currentCategories)
         {
             Log.e(TAG, cat.getName());
             if (cat.getName().equals(name))
             {
                 Log.e(TAG, "Category Exists already");
                 return null;
             }
         }

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
        return newCategory;
    }

    public void deleteCategory(Category category)
    {
        long id = category.getId();
        Log.d(TAG, "Transaction deleted with id: " + id);
        mDatabase.delete(DBHelper.TABLE_CATEGORIES,
                DBHelper.COLUMN_CATEGORY_ID + " = " + id, null);
    }

    public void deleteAllUserCategories(long userID)
    {
        mDatabase.delete(DBHelper.TABLE_CATEGORIES, DBHelper.COLUMN_CATEGORY_USER_ID + "=" + Long.toString(userID), null);
    }

    public List<Category> getUserCategories(long userID)
    {
        List<Category> listCategory = new ArrayList<Category>();

        Cursor cursor = mDatabase.rawQuery("SELECT* FROM " + DBHelper.TABLE_CATEGORIES, null);

        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Category category = cursorToCategory(cursor);
                listCategory.add(category);
                cursor.moveToNext();
            }
        }
        else
        {
            listCategory.add(new Category("Category"));
        }

        cursor.close();

        return listCategory;

    }

    private Category cursorToCategory(Cursor cursor)
    {
        Category category = new Category();
        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));

        long userID = cursor.getLong(2);
        UsersDAO dao = new UsersDAO(mContext);
        User user = dao.getUserByID(userID);
        if (user != null)
        {
            category.setUser(user);
        }
        return category;
    }

    public boolean updateCategory(long userId, String oldCategory, String newCategory)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CATEGORY_NAME, newCategory);
        try {
            mDatabase.update(DBHelper.TABLE_CATEGORIES,
                    contentValues,
                    DBHelper.COLUMN_CATEGORY_NAME + " = ? AND " +
                            DBHelper.COLUMN_CATEGORY_USER_ID + " = " + userId,
                    new String[]{oldCategory});
            return true;
        } catch (SQLException e)
        {
            Log.e(TAG, "SQLException while updating database + " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
