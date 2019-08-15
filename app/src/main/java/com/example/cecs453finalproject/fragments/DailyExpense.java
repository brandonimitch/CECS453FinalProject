package com.example.cecs453finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
public class DailyExpense extends Fragment implements AdapterView.OnItemSelectedListener {


//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String USER_ID = "param1";
//    private static final String USERNAME = "param2";
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // Fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DailyExpense";

    // Member userId and username variables.
    private Long mUserID;
    private String mUsername;
    private String mParam1;
    private String mParam2;


//    private RecyclerView mItemsList;
    private TransactionDAO mTransactionDAO;
    private UsersDAO mUserDAO;
    private CategoryDAO mCategoryDAO;
    private List<Category> mCategoryList;
    private List<Transaction> mTransactionList;
    private OnFragmentInteractionListener mListener;

    // Control objects.
    private EditText categoryName;
    private EditText expenseAmount;
    private Button dailyExpenseBtn;
    private Spinner spinner;
    // Variables to capture user input from Edit Text objects.
    private String categoryNameEntered;
    private double expenseAmountEntered;





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
    public static DailyExpense newInstance(String param1, String param2) {
        DailyExpense fragment = new DailyExpense();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

        dailyExpenseBtn =  v.findViewById(R.id.daliyExpenseBtn);
        categoryName =  v.findViewById(R.id.dailyExpenseEditText1);
        expenseAmount =  v.findViewById(R.id.dailyExpenseEditText2);
        spinner = v.findViewById(R.id.dailyExpenseSpinner);

        ArrayList<String> categoryStrings = new ArrayList<>();
        for (Category category : mCategoryList)
        {
            categoryStrings.add(category.getName());
        }

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(spinner.getContext(),
                R.layout.spinner_drop_item, categoryStrings);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Button functionality.
        dailyExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> catStrings = new ArrayList<>();
                for (Category category : mCategoryList) {

                    catStrings.add(category.getName());
                }

                categoryNameEntered = categoryName.getText().toString();
                expenseAmountEntered = Double.parseDouble(expenseAmount.getText().toString());

                if (catStrings.contains(categoryNameEntered)) {


                }


//                String oldCat = spinner.getSelectedItem().toString();

//                if (catStrings.contains(oldCat)) {
//                    mCategoryDAO.updateCategory(mUserID, oldCat, categoryNameEntered);
//                    Toast.makeText(view.getContext(),
//                            oldCat + " has been replaced by " + categoryNameEntered,
//                            Toast.LENGTH_SHORT)
//                            .show();
//                    Log.e(TAG, "Replace");
//                    catStrings.remove(oldCat);
//                    catStrings.add(newCat);
//                } else {
//                    Log.e(TAG, "New");
//                    mCategoryDAO.createCategory(mUserID, newCat);
//                    Toast.makeText(view.getContext(),
//                            newCat + " has been added",
//                            Toast.LENGTH_SHORT)
//                            .show();
//                    catStrings.add(newCat);
//                }

                // Reset Text field
                newCategoryTextView.setText("");

                //Update Spinner list
                CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(categorySpinner.getContext(),
                        R.layout.spinner_drop_item, catStrings);
                categorySpinner.setAdapter(adapter);

                //Update mCategoryList
                mCategoryList = mCategoryDAO.getUserCategories(mUserID);
            }
        }

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

