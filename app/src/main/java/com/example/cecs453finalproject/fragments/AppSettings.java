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
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cecs453finalproject.R;


/**
 * AppSettings class provides functionality for the fragment_app_settings.xml layout.
 * Users have the option to modify the expense tracker, this page acts as a jump off point
 * for DailyExpense, MonthlyIncome, AddEditExpense, AddEditCategory.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppSettings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppSettings extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    // Required empty public constructor
    public AppSettings() {}


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppSettings.
     */
    public static AppSettings newInstance(String param1, String param2) {
        AppSettings fragment = new AppSettings();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_app_settings, container, false);


        if (savedInstanceState != null) {

            // Obtain username and customer ID from main activity.
            String username = getArguments().getString("username");
            Long customerId = getArguments().getLong("customerID");
        }



        // Set daily expense button to open daily expense fragment.
        Button dailyExpense = v.findViewById(R.id.dailyExpenseBtn);
        dailyExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new DailyExpense()).commit();
            }
        });


        // Set expense item button to open expense item fragment.
        Button expenseItem = v.findViewById(R.id.expenseItemBtn);
        expenseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new AddEditCategory()).commit();
            }
        });

        // Set monthly income button to open monthly income fragment.
        Button monthlyIncome = v.findViewById(R.id.monthlyIncomeBtn);
        monthlyIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new MonthlyIncome()).commit();
            }
        });

        // Set delete transaction button to open delete fragment.
        Button deleteButton = v.findViewById(R.id.deleteExpenseBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new DeleteTransaction()).commit();
            }
        });

        return v;
    }


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
