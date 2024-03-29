package com.example.cecs453finalproject.fragments;


/*
 *
 * Created on 07/10/19
 * By Tylar Simone and Brandon Mitchell
 * Califonia State University Long Beach.
 * CECS 453
 * Professor Arjang Fahim.
 *
 * Expense Tracker
 *
 * */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.adapters.MyRecyclerViewAdapter;
import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Transaction;
import com.example.cecs453finalproject.database.CategoryDAO;
import com.example.cecs453finalproject.database.TransactionDAO;
import com.example.cecs453finalproject.database.UsersDAO;

import java.util.List;


/**
 * Allows the user to edit existing expenses and to add new expenses.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEditExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEditExpense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditExpense extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "param1";
    private static final String USERNAME = "param2";

    // TODO: Rename and change types of parameters
    private Long mUserID;
    private String mUsername;

    private OnFragmentInteractionListener mListener;
    private TransactionDAO mTransactionDAO;
    private UsersDAO mUserDAO;
    private CategoryDAO mCategoryDAO;
    private List<Transaction> mTransactionList;
    private List<Category> mCategoryList;
    private RecyclerView mItemsList;

    public AddEditExpense() {
        // Required empty public constructor
    }

    /**
     * AddEditExpense class adds functionality to the layout of fragemnt_addedit_expense.xml.
     * Allows users to add new expenses and incomes, edit existing expenses and incomes, and
     * add new expense categories.
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditExpense.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditExpense newInstance(long param1, String param2) {
        AddEditExpense fragment = new AddEditExpense();
        Bundle args = new Bundle();
        args.putLong(USER_ID, param1);
        args.putString(USERNAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserID = ((MainActivity) getActivity()).getLoggedInUserId();
        mUsername = ((MainActivity) getActivity()).getLoggedInUsername();

        mTransactionDAO = new TransactionDAO(getActivity());
        mUserDAO = new UsersDAO(getActivity());
        mCategoryDAO = new CategoryDAO(getActivity());

        mTransactionList = mTransactionDAO.getUserTransactions(mUserID);
        mCategoryList = mCategoryDAO.getUserCategories(mUserID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_addedit_expense, container, false);

        mItemsList = v.findViewById(R.id.expense_recycler_view);
        mItemsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Default transactions that are loaded in the absence of any user entered transactions.
        if (mTransactionList.size() == 0)
        {
            Transaction newTransaction0 = mTransactionDAO.createTransaction(mUserID,"07/07/2019",
                    "Test Expense","Birthday", 1, 100.00);
            Transaction newTransaction1 = mTransactionDAO.createTransaction(mUserID,"06/08/2019",
                    "Vons Grocery Store","Grocery", -1, 198.46);
            Transaction newTransaction2 = mTransactionDAO.createTransaction(mUserID,"04/07/2019",
                    "Gift from Auntie Debbie Just need to make this longer","Birthday", 1, 250.00);
            Transaction newTransaction3 = mTransactionDAO.createTransaction(mUserID,"03/08/2019",
                    "Albertsons","Grocery", -1, 198.46);
            Transaction newTransaction4 = mTransactionDAO.createTransaction(mUserID,"06/07/2019",
                    "Panama Joe's","Bar", -1, 34.56);
            Transaction newTransaction5 = mTransactionDAO.createTransaction(mUserID,"08/08/2019",
                    "Uber","Ride Share", -1, 7.86);

            mTransactionList.add(newTransaction0);
            mTransactionList.add(newTransaction1);
            mTransactionList.add(newTransaction2);
            mTransactionList.add(newTransaction3);
            mTransactionList.add(newTransaction4);
            mTransactionList.add(newTransaction5);
        }

        // Set up recycler view.
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, mTransactionList,
                mCategoryList,this);
        mItemsList.setAdapter(adapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View view, int position) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
