package com.example.cecs453finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.adapters.TransactionSpinnerAdapter;
import com.example.cecs453finalproject.classes.Category;
import com.example.cecs453finalproject.classes.Transaction;
import com.example.cecs453finalproject.database.CategoryDAO;
import com.example.cecs453finalproject.database.TransactionDAO;
import com.example.cecs453finalproject.database.UsersDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


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
    ArrayList<String> mTransactionListStrings;
    private OnFragmentInteractionListener mListener;

    // Control objects.
    private EditText mDateText;
    private EditText mDescText;
    private EditText mCategoryText;
    private EditText mAmountText;
    private Switch mIncomeSwitch;
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

        spinner = v.findViewById(R.id.dailyExpenseSpinner);
        mDateText = v.findViewById(R.id.daily_expense_date);
        mDescText = v.findViewById(R.id.daily_expense_desc);
        mCategoryText = v.findViewById(R.id.daily_expense_category);
        mIncomeSwitch = v.findViewById(R.id.daily_expense_type);
        mAmountText = v.findViewById(R.id.daily_expense_amount);
        dailyExpenseBtn =  v.findViewById(R.id.daliyExpenseBtn);

        mTransactionListStrings = new ArrayList<>();
        for (Transaction trans : mTransactionList)
        {
            mTransactionListStrings.add(trans.getDate() + " - " +
                    trans.getDescr() + "   " + trans.getAmount()*trans.getType());
        }

        TransactionSpinnerAdapter adapter = new TransactionSpinnerAdapter(spinner.getContext(),
                R.layout.spinner_drop_item, mTransactionListStrings);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(spinner.getChildCount());

        mIncomeSwitch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mIncomeSwitch.isChecked())
                {
                    mIncomeSwitch.setText("Income");
                }
                else
                {
                    mIncomeSwitch.setText("Expense");
                }
            }
        });

        // Button functionality.
        dailyExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verifyInputs())
                {

                    String date;
                    String desc;
                    String cat;
                    double amt;
                    int type;

                    if (spinner.getSelectedItem().equals("*Add New*") || spinner.getSelectedItemPosition() == 0)
                    {
                        spinner.setSelection(spinner.getChildCount()-1);
                        date = mDateText.getText().toString();
                        date = date.replace('-','/');
                        desc = mDescText.getText().toString();
                        cat = mCategoryText.getText().toString();
                        amt = Double.parseDouble(mAmountText.getText().toString());
                        if (mIncomeSwitch.isChecked())
                        {
                            type = 1;
                        }
                        else
                        {
                            type = -1;
                        }
                        mTransactionDAO.createTransaction(mUserID, date, desc, cat, type, amt);
                        Toast.makeText(getContext(), "Transaction Added.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (mDateText.getText().toString().length() == 0)
                        {
                            date = mDateText.getHint().toString();
                        }
                        else
                        {
                            date = mDateText.getText().toString();
                        }

                        date = date.replace('-','/');

                        if (mDescText.getText().toString().length() == 0)
                        {
                            desc = mDescText.getHint().toString();
                        }
                        else
                        {
                            desc = mDescText.getText().toString();
                        }

                        if (mCategoryText.getText().toString().length() == 0)
                        {
                            cat = mCategoryText.getHint().toString();
                        }
                        else
                        {
                            cat = mCategoryText.getText().toString();
                        }

                        if (mIncomeSwitch.isChecked())
                        {
                            type = 1;
                        }
                        else
                        {
                           type = -1;
                        }

                        if (mAmountText.getText().toString().length() == 0)
                        {
                            amt = Double.parseDouble(mAmountText.getHint().toString());
                        }
                        else
                        {
                            amt = Double.parseDouble(mAmountText.getText().toString());
                        }

                        long transId = mTransactionList.get(spinner.getSelectedItemPosition()-1).getId();
                        mTransactionDAO.updateTransaction(transId, date, desc, cat, type, amt);
                        Toast.makeText(getContext(), "Transaction Updated.", Toast.LENGTH_SHORT).show();
                    }

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new AddEditExpense()).commit();
                }

            }
        });

        return v;
    }

    private boolean verifyInputs() {
        boolean allGood = true;

        String regexDatePattern = "^((0|1)\\d{1})-((0|1|2)\\d{1})-((19|20)\\d{2})";
        Pattern compliedDatePattern = Pattern.compile(regexDatePattern, Pattern.CASE_INSENSITIVE);

        // Only Check if Add New
        if (spinner.getSelectedItemPosition() == 0)
        {
            // Check Date Input
            if (!compliedDatePattern.matcher(mDateText.getText().toString()).lookingAt())
            {
                allGood = false;
                Toast.makeText(getContext(), "Date needs to be in MM-DD-YYYY format",
                        Toast.LENGTH_SHORT).show();
            }

            // Check Description input
            if (mDescText.getText().toString().length() == 0)
            {
                allGood = false;
                Toast.makeText(getContext(), "Please enter description", Toast.LENGTH_SHORT).show();
            }

            // Check Category input
            if (mCategoryText.getText().toString().length() == 0)
            {
                allGood = false;
                Toast.makeText(getContext(), "Please enter category", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ArrayList<String> catStrings = new ArrayList<>();
                String tempCat = mCategoryText.getText().toString();
                for (Category cat : mCategoryList)
                {
                    catStrings.add(cat.getName());
                }
                if (!catStrings.contains(tempCat))
                {
                    mCategoryDAO.createCategory(mUserID, tempCat);
                }
            }

            // Check Amount input
            if (mAmountText.getText().toString().length() == 0)
            {
                allGood = false;
                Toast.makeText(getContext(), "Amount is empty.", Toast.LENGTH_SHORT).show();
            }
        }

        return allGood;
    }

    private void updateTransactionDropDown()
    {
        mTransactionList = mTransactionDAO.getUserTransactions(mUserID);
        mTransactionListStrings = new ArrayList<>();
        for (Transaction trans : mTransactionList)
        {
            mTransactionListStrings.add(trans.getDate() + " - " +
                    trans.getDescr() + "   " + trans.getAmount()*trans.getType());
        }

        TransactionSpinnerAdapter adapter = new TransactionSpinnerAdapter(spinner.getContext(),
                R.layout.spinner_drop_item, mTransactionListStrings);
        spinner.setAdapter(adapter);
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


    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG, "Position: " + position + ", Size: " + mTransactionList.size());

        if (position == 0)
        {
            mDateText.setHint("Enter Date");
            mDescText.setHint("Enter Description");
            mCategoryText.setHint("Enter Category");
            mAmountText.setHint("Enter Amount");
        }
        else
        {
            Transaction transaction = mTransactionList.get(position - 1);

            mDateText.setHint(transaction.getDate());
            mDescText.setHint(transaction.getDescr());
            mCategoryText.setHint(transaction.getCategory());
            mAmountText.setHint(Double.toString(transaction.getAmount()));

            if (transaction.getType() > 0)
            {
                mIncomeSwitch.setText("Income");
                mIncomeSwitch.setChecked(true);
            }
            else
            {
                mIncomeSwitch.setText("Expense");
                mIncomeSwitch.setChecked(false);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

