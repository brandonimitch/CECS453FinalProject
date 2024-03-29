package com.example.cecs453finalproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = "CategorySpinnerAdapter";
    private Context mContext;
    private ArrayList<String> mValues;

    public CategorySpinnerAdapter(Context context, int textViewResourceId,
                                  ArrayList<String> values)
    {
        super(context, textViewResourceId, values);
        // Ensure category at first spot for hint
        if (!values.contains("Category"))
        {
            values.add(0,"Category");
        }
        else{
            values.remove("Category");
            values.add(0,"Category");
        }
        this.mContext = context;
        Collections.sort(values.subList(1,values.size()));
        // Add **Add New** to back
        values.add("*Add New*");
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

    @Override
    public boolean isEnabled(int position){
        if(position == 0)
        {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        }
        else
        {
            return true;
        }
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        if(position == 0){
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY);
        }
        else
        {
            tv.setTextColor(Color.BLACK);
        }
        return view;
    }

}
