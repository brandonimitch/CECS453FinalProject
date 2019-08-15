package com.example.cecs453finalproject.fragments;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Expenses.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Expenses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Expenses extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{
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

    public Expenses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Expenses.
     */
    // TODO: Rename and change types and number of parameters
    public static Expenses newInstance(long param1, String param2) {
        Expenses fragment = new Expenses();
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

        // TODO: DELETE AFTER TESTING
        mTransactionDAO.deleteAllUserTransactions(mUserID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_expenses, container, false);

        if (savedInstanceState != null) {

            // Obtain username and customer ID from main activity.
            String username = getArguments().getString("username");
            Long customerId = getArguments().getLong("customerID");
        }


        mItemsList = (RecyclerView) v.findViewById(R.id.expense_recycler_view);
        mItemsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, mTransactionList,
                mCategoryList,this);

        mItemsList.setAdapter(adapter);

        //TODO: DELETE AFTER TESTING IS COMPLETE
        Transaction newTransaction0 = mTransactionDAO.createTransaction(mUserID,"7/7/2019",
                "Test Expense","Category", -1, 100.00);
        Transaction newTransaction1 = mTransactionDAO.createTransaction(mUserID,"7/8/2019",
                "Vons Grocery Store","Grocery", -1, 198.46);

        mTransactionList.add(newTransaction0);
        mTransactionList.add(newTransaction1);

        mTransactionDAO.deleteAllUserTransactions(mUserID);
        // TODO: TO HERE

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
