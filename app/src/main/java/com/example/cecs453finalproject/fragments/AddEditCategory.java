package com.example.cecs453finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.adapters.CategorySpinnerAdapter;
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
 * {@link AddEditCategory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEditCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditCategory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Long mUserID;
    private String mUsername;

    private OnFragmentInteractionListener mListener;
    private TransactionDAO mTransactionDAO;
    private UsersDAO mUserDAO;
    private CategoryDAO mCategoryDAO;
    private List<Transaction> mTransactionList;
    private List<Category> mCategoryList;

    public AddEditCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditCategory newInstance(String param1, String param2) {
        AddEditCategory fragment = new AddEditCategory();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);

        Button submitButton = (Button) view.findViewById(R.id.categoryChangeBtn);
        TextView originalCategoryTextView = (TextView) view.findViewById(R.id.changeCategoryEditText);
        TextView newCategoryTextView = (TextView) view.findViewById(R.id.newCategoryEditText);
        Spinner categorySpinner = (Spinner) view.findViewById(R.id.expenseCatSpinner);

        ArrayList<String> categoryStrings = new ArrayList<>();
        for (Category category : mCategoryList)
        {
            categoryStrings.add(category.getName());
        }
        categoryStrings.add("Add New");

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getContext(), categorySpinner.getId(),
                categoryStrings);
        categorySpinner.setAdapter(adapter);


        return view;
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