package com.example.cecs453finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.adapters.CategorySpinnerAdapter;
import com.example.cecs453finalproject.adapters.MyRecyclerViewAdapter;
import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Transaction;
import com.example.cecs453finalproject.database.CategoryDAO;
import com.example.cecs453finalproject.database.TransactionDAO;
import com.example.cecs453finalproject.database.UsersDAO;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyExpense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyExpense extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "param1";
    private static final String USERNAME = "param2";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Long mUserID;
    private String mUsername;


    // Create variables for control objects.
    private EditText categoryName;
    private EditText expenseAmount;
    private Button dailyExpenseBtn;
    private Spinner spinner;
    private String categoryNameEntered;
    private double expenseAmountEntered;

    private RecyclerView mItemsList;
    private List<Transaction> mTransactionList;
    private TransactionDAO mTransactionDAO;
    private UsersDAO mUserDAO;
    private CategoryDAO mCategoryDAO;
    private List<Category> mCategoryList;
    private DailyExpense.OnFragmentInteractionListener mListener;



    public DailyExpense() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyExpense.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyExpense newInstance(Long param1, String param2) {
        DailyExpense fragment = new DailyExpense();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mUserID = getArguments().getLong(USER_ID);
//            mUsername = getArguments().getString(USERNAME);
//        }

        mUserID = ((MainActivity) getActivity()).getLoggedInUserId();
        mUsername = ((MainActivity) getActivity()).getLoggedInUsername();


        mTransactionDAO = new TransactionDAO(getActivity());
        mUserDAO = new UsersDAO(getActivity());
        mCategoryDAO = new CategoryDAO(getActivity());
        mTransactionList = mTransactionDAO.getUserTransactions(mUserID);
        mCategoryList = mCategoryDAO.getUserCategories(mUserID);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily_expense, container, false);
        View expenseView = inflater.inflate(R.layout.fragment_expenses, container, false);

        // Define control objects.
        dailyExpenseBtn = v.findViewById(R.id.daliyExpenseBtn);
        categoryName = v.findViewById(R.id.dailyExpenseEditText1);
        expenseAmount = v.findViewById(R.id.dailyExpenseEditText2);
        spinner = v.findViewById(R.id.dailyExpenseSpinner);


        mItemsList = expenseView.findViewById(R.id.expense_recycler_view);
        mItemsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, mTransactionList,
                mCategoryList,this);

        mItemsList.setAdapter(adapter);

        dailyExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity().getApplicationContext();

                if (expenseAmount != null && categoryName != null) {

                    if (categoryName.getText().toString().length() != 0) {

                        categoryNameEntered = categoryName.getText().toString();

                    } else if (categoryName.getText().toString().length() == 0) {

                        Toast.makeText(context, "You must enter a category!", Toast.LENGTH_SHORT).show();

                    } else if (expenseAmount.getText().toString().length() != 0) {

                        expenseAmountEntered = Double.parseDouble(expenseAmount.getText().toString());

                    } else if (expenseAmount.getText().toString().length() == 0) {

                        Toast.makeText(context, "You must enter an amount in dollars!", Toast.LENGTH_SHORT).show();

                    } else {

                        // Database Transaction: add values to database.
                        Transaction newTransaction0 = mTransactionDAO.createTransaction(mUserID,
                                "7/10/2019", "New / Edit Expense", categoryNameEntered, -1, expenseAmountEntered);
                        Toast.makeText(context, "Database updated " + categoryNameEntered + " with $" + expenseAmountEntered, Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(context, "You must enter both category name and expense amount!", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
