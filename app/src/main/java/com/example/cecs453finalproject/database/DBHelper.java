package com.example.cecs453finalproject.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "CECS453_Final_Expense_Tracker_Rev2.db";
    private static final int DB_VERSION = 1;

    // User Datbase column names
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id_user";
    public static final String COLUMN_USERNAME = "user_name";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_INCOME = "user_income";

    // Transaction database column names
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_TRANSACTION_ID = "id_transaction";
    public static final String COLUMN_TRANSACTION_DATE = "date";
    public static final String COLUMN_TRANSACTION_DESCRIPTION = "description";
    public static final String COLUMN_TRANSACTION_CATEGORY = "category";
    public static final String COLUMN_TRANSACTION_TYPE = "type";
    public static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    public static final String COLUMN_TRANSACTION_USER_ID = "user_id";

    // Category database cloumn names
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "id_category";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_USER_ID = "user_id";

    private static final String SQL_CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERNAME + " TEXT NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_USER_INCOME + " REAL NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS+ "("
            + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TRANSACTION_DATE + " TEXT NOT NULL, "
            + COLUMN_TRANSACTION_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_TRANSACTION_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_TRANSACTION_TYPE + " INTEGER NOT NULL, "
            + COLUMN_TRANSACTION_AMOUNT + " REAL NOT NULL, "
            + COLUMN_TRANSACTION_USER_ID + " INTEGER NOT NULL"
            +");";

    private static final String SQL_CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_NAME+ " TEXT NOT NULL, "
            + COLUMN_CATEGORY_USER_ID + " INTEGER NOT NULL "
            +");";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(SQL_CREATE_TABLE_USERS);
        database.execSQL(SQL_CREATE_TABLE_TRANSACTIONS);
        database.execSQL(SQL_CREATE_TABLE_CATEGORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int next) {
        Log.e(TAG,
                "Upgrading database from version " + old + " to " + next);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        onCreate(db);
    }

}

