package com.example.cecs453finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.classes.User;
import com.example.cecs453finalproject.database.UsersDAO;

import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthlyIncome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthlyIncome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyIncome extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG =  "Monthly Income";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NumberFormat nf = NumberFormat.getCurrencyInstance();

    // Create variables for control objects.
    private EditText monthlyIncomeEdTxt;
    private TextView currentIncomeEdTxt;
    private Button monthlyIncomeBtn;
    private String monthlyIncomeEntered;
    private double userIncome;
    private UsersDAO mUserDAO;
    private long mUserId;
    private User mUser;

    private OnFragmentInteractionListener mListener;

    public MonthlyIncome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyIncome.
     */
    public static MonthlyIncome newInstance(String param1, String param2) {
        MonthlyIncome fragment = new MonthlyIncome();
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

        userIncome = ((MainActivity) getActivity()).getMonthlyIncome();
        mUserId = ((MainActivity) getActivity()).getLoggedInUserId();
        mUserDAO = new UsersDAO(getActivity());
        mUser = mUserDAO.getUserByID(mUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_monthly_income, container, false);

        monthlyIncomeEdTxt = v.findViewById(R.id.monthlyIncomeEditText);
        monthlyIncomeBtn = v.findViewById(R.id.submit_monthly_income);
        currentIncomeEdTxt = v.findViewById(R.id.current_income);

        currentIncomeEdTxt.setText(nf.format(userIncome));

        monthlyIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (monthlyIncomeEdTxt.getText().toString().length() > 0)
                {
                    monthlyIncomeEntered = monthlyIncomeEdTxt.getText().toString();
                    ((MainActivity)getActivity()).setMonthlyIncome(Double.parseDouble(monthlyIncomeEntered));
                    mUserDAO.updateUserIncome(mUserId, Double.parseDouble(monthlyIncomeEntered));
                    currentIncomeEdTxt.setText(nf.format(Double.parseDouble(monthlyIncomeEntered)));
                }
                else
                {
                    Toast.makeText(getContext(), "New Income field is blank.", Toast.LENGTH_SHORT).show();
                }


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
