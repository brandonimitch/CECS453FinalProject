package com.example.cecs453finalproject.adapters;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyRecyclerViewAdapter";
    private NumberFormat nf = NumberFormat.getCurrencyInstance();

    private List<Transaction> mData;
    private List<Category> mCategories;
    private ItemClickListener mClickListener;
    private Long mUserId;
    private CategorySpinnerAdapter spinnerAdapter;
    private Fragment mFragment;

    public MyRecyclerViewAdapter(Fragment fragment, List<Transaction> data, List<Category> categories,
                                 ItemClickListener clickListener) {
        this.mFragment = fragment;
        this.mClickListener = clickListener;
        this.mCategories = categories;
        this.mUserId = ((MainActivity) fragment.getActivity()).getLoggedInUserId();

        // Sort Data by Date by descending order
        Collections.sort(data, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDateObject().compareTo(o1.getDateObject());
            }
        });

        this.mData = data;
    }

    private HashMap<Long, Date> sortListbyDate(HashMap<Long, Date> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Long, Date> > list =
                new LinkedList<Map.Entry<Long, Date> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Long, Date> >() {
            public int compare(Map.Entry<Long, Date> o1,
                               Map.Entry<Long, Date> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Long, Date> temp = new LinkedHashMap<Long, Date>();
        for (Map.Entry<Long, Date> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
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

        holder.mDateText.setText(data.getDate());
        holder.mDescrText.setText(data.getDescr());
        holder.mAmountText.setText(nf.format(amount));
        holder.mCategoryText.setText(data.getCategory());
/*        // Check if category has been established
        if (categories.contains(data.getCategory()))
        {
            int index = categories.indexOf(data.getCategory());
            holder.mCategoryText.setSelection(index);
            holder.mCategoryText.setEnabled(false);
        }*/


        /*holder.mCategoryText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });*/

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
        TextView mCategoryText;
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