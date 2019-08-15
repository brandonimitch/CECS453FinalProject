package com.example.cecs453finalproject.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DeleteTransactionSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = "TransactionSpinnerAdapter";
    private Context mContext;
    private ArrayList<String> mValues;

    public DeleteTransactionSpinnerAdapter(Context context, int textViewResourceId,
                                     ArrayList<String> values)
    {
        super(context, textViewResourceId, values);
        this.mContext = context;
        this.mValues = values;
    }

    @Override
    public int getCount()
    {
        return mValues.size();
    }

    @Override
    public String getItem(int position)
    {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

}

