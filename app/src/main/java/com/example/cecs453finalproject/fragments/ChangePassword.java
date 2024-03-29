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
import android.widget.Toast;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.classes.User;
import com.example.cecs453finalproject.database.UsersDAO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePassword.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Create variables for control objects.
    private EditText mCurrentPassword;
    private EditText mNewPassword;
    private EditText mReEnterPassword;
    private Button mSubmitButton;
    private UsersDAO mUserDAO;
    private long mUserId;
    private User mUser;

    private OnFragmentInteractionListener mListener;

    public ChangePassword() {
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
        mUserId = ((MainActivity) getActivity()).getLoggedInUserId();
        mUserDAO = new UsersDAO(getActivity());
        mUser = mUserDAO.getUserByID(mUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        mCurrentPassword = (EditText) view.findViewById(R.id.current_password);
        mNewPassword = (EditText) view.findViewById(R.id.set_new_password);
        mReEnterPassword = (EditText) view.findViewById(R.id.set_retype_password);
        mSubmitButton = (Button) view.findViewById(R.id.change_pass_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verifyInputs())
                {
                    String newPassword = mNewPassword.getText().toString();
                    mUserDAO.updateUserPassword(mUserId, newPassword);
                    Toast.makeText(getContext(), "Password Updated!", Toast.LENGTH_SHORT).show();

                    mCurrentPassword.setText("");
                    mNewPassword.setText("");
                    mReEnterPassword.setText("");
                }

            }
        });

        return view;
    }

    private boolean verifyInputs()
    {
        boolean allGood = true;

        // Check for empty boxes
        if (mCurrentPassword.getText().toString().length() == 0)
        {
            allGood = false;
            Toast.makeText(getContext(), "Current Password is Empty.", Toast.LENGTH_SHORT).show();
        }
        if (mNewPassword.getText().toString().length() == 0)
        {
            allGood = false;
            Toast.makeText(getContext(), "New Password is Empty.", Toast.LENGTH_SHORT).show();
        }
        if (mReEnterPassword.getText().toString().length() == 0)
        {
            allGood = false;
            Toast.makeText(getContext(), "Re-Entered Password is Empty.", Toast.LENGTH_SHORT).show();
        }

        // Check if current password is valid
        if (!mCurrentPassword.getText().toString().equals(mUser.getPassword()))
        {
            allGood = false;
            Toast.makeText(getContext(), "Current Password is Incorrect", Toast.LENGTH_SHORT).show();
        }

        // Check if Passwords Match
        if (!mNewPassword.getText().toString().equals(mReEnterPassword.getText().toString()))
        {
            allGood = false;
            Toast.makeText(getContext(), "New Password and Re-Enter Password do not match"
                    , Toast.LENGTH_SHORT).show();
        }

        return allGood;
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
