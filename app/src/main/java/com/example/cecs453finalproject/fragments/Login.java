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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cecs453finalproject.MainActivity;
import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.classes.SaveSharedPreference;
import com.example.cecs453finalproject.classes.User;
import com.example.cecs453finalproject.database.UsersDAO;

import java.util.List;


/**
 * Login class provides functionality for the fragment_login.xml layout
 * Allows users to login if they have an account, and has a link to the signup
 * page if they do not yet have an account.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LoginActivity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private UsersDAO mUserDAO;
    private List<User> userList;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        mUserDAO = new UsersDAO(getActivity());
        userList = mUserDAO.getAllUsers();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Make drawer invisible until login is successful
        ((MainActivity) getActivity()).setDrawerLocked(true);

        //Print all users for testing information
        for (User user : userList)
        {
            Log.e(TAG,"ID: " + user.getId() +
                    "\nUsername: " + user.getUsername() +
                    "\nPassword: " + user.getPassword() +
                    "\nEmail: " + user.getEmail()+
                    "\nIncome: " + user.getIncome() +"\n");
        }


        Button signup = v.findViewById(R.id.signupBtnLogin);
        Button login = v.findViewById(R.id.loginBtnLogin);
        final CheckBox saveUsername = (CheckBox) v.findViewById(R.id.saveUsername);

        // Control fragment when SignUp Button is clicked
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new Signup()).commit();
            }
        });

        // Control Fragment when Login Button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = ((EditText) getView().findViewById(R.id.userNameEditTextLogin))
                        .getText().toString();
                User checkUser = mUserDAO.getUserByUsername(username);

                if (verifyCredentials(checkUser))
                {
                    // Allow User to access navigation drawer
                    ((MainActivity) getActivity()).setDrawerLocked(false);
                    ((MainActivity) getActivity()).setLoggedInUsername(checkUser.getUsername());
                    ((MainActivity) getActivity()).setLoggedInUserId(checkUser.getId());
                    ((MainActivity) getActivity()).setMonthlyIncome(checkUser.getIncome());

                    if (saveUsername.isChecked())
                    {
                        SaveSharedPreference.setUserName(getContext(), checkUser.getUsername());
                    }


                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new AddEditExpense()).commit();
                }
            }
        });

        return v;
    }

    // Verifies that password entered equals the password in the account, if a username is provided.
    private boolean verifyCredentials(User checkUser) {

        String password = ((EditText) getView().findViewById(R.id.passwordEditTextLogin))
                .getText().toString();

        if (checkUser != null)
        {
            if (checkUser.getPassword().equals(password))
            {
                return true;
            }
        }

        Toast.makeText(getActivity(), "Username/Password does not exist", Toast.LENGTH_SHORT).show();
        return false;
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
