package com.example.cecs453finalproject.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Transaction;
import com.example.cecs453finalproject.classes.User;
import com.example.cecs453finalproject.fragments.AddEditCategory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyRecyclerViewAdapter";
    private NumberFormat nf = NumberFormat.getCurrencyInstance();

    private List<Transaction> mData;
    private List<Category> mCategories;
    private ItemClickListener mClickListener;
    private User mUser;
    private CategorySpinnerAdapter spinnerAdapter;
    private Fragment mFragment;

    public MyRecyclerViewAdapter(Fragment fragment, List<Transaction> data, List<Category> categories,
                                 ItemClickListener clickListener) {
        this.mFragment = fragment;
        this.mData = data;
        this.mClickListener = clickListener;
        this.mCategories = categories;
        this.mUser = categories.get(0).getUser();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction,
                parent, false);

        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
        Transaction data = mData.get(position);
        double amount = data.getAmount() * (double) data.getType();

        ArrayList<String> categories = new ArrayList<>();

        for (Category cat : mCategories)
        {
            categories.add(cat.getName());
        }

        // Place Add new at back of list
        if (categories.contains("Add new"))
        {
            categories.remove("Add new");
            categories.add(categories.size()-1,"Add new");
        }
        else
        {
            categories.add("Add new");
        }

        spinnerAdapter = new CategorySpinnerAdapter(holder.mCategoryText.getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);

        holder.mDateText.setText(data.getDate());
        holder.mDescrText.setText(data.getDescr());
        holder.mAmountText.setText(nf.format(data.getAmount()*data.getType()));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mCategoryText.setAdapter(spinnerAdapter);
        holder.mCategoryText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = (String) adapterView.getItemAtPosition(i);
                if (item.equals("Add new")){
                    FragmentManager fragmentManager = mFragment.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer,
                            new AddEditCategory()).addToBackStack("Expenses").commit();
                }
                else
                {
                    //TODO: Make spinner non editable!
                    Log.e(TAG,adapterView.getParent().getClass().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

    }

    @Override
    public int getItemCount() {
        if(mData != null)
        {
            return mData.size();
        }
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        TextView mDateText;
        TextView mDescrText;
        Spinner mCategoryText;
        TextView mAmountText;
        ItemClickListener mClickListener;

        public ViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);

            mView = itemView;
            mDateText = itemView.findViewById(R.id.trans_date);
            mDescrText = itemView.findViewById(R.id.trans_descr);
            mCategoryText = itemView.findViewById(R.id.trans_category);
            mAmountText = itemView.findViewById(R.id.trans_amount);

            mClickListener = clickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
            {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }


}