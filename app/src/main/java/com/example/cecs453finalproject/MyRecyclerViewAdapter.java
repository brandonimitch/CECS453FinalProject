package com.example.cecs453finalproject;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cecs453finalproject.database.Transaction;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Transaction> mData;
    private ItemClickListener mClickListener;

    public MyRecyclerViewAdapter(ArrayList<Transaction> data, ItemClickListener clickListener) {
        this.mData = data;
        this.mClickListener = clickListener;
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

        holder.mDateText.setText(data.getDate());
        holder.mDescrText.setText(data.getDescr());
        holder.mCategoryText.setText(data.getCategory());
        holder.mAmountText.setText(Double.toString(amount));

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