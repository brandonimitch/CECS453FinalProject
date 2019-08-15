package com.example.cecs453finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cecs453finalproject.R;
import com.example.cecs453finalproject.classes.User;
import com.example.cecs453finalproject.database.UsersDAO;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Signup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SignupActivity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Get all signup boxes
    private RelativeLayout usernameBox;
    private RelativeLayout passwordBox;
    private RelativeLayout retypePasswordBox;
    private RelativeLayout emailBox;
    private ArrayList<RelativeLayout> fillBoxes;

    // Database helpers
    private UsersDAO mUserDAO;
    private List<User> userList;

    public Signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signup.
     */
    // TODO: Rename and change types and number of parameters
    public static Signup newInstance(String param1, String param2) {
        Signup fragment = new Signup();
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
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        Button signup = v.findViewById(R.id.signupBtnSignup);

        // Get text fill-in fields and add them to list for parsing
        usernameBox = (RelativeLayout) v.findViewById(R.id.usernameBox);
        passwordBox = (RelativeLayout) v.findViewById(R.id.passwordBox);
        retypePasswordBox = (RelativeLayout) v.findViewById(R.id.passwordRetypeBox);
        emailBox = (RelativeLayout) v.findViewById(R.id.emailBox);
        fillBoxes = new ArrayList<RelativeLayout>();
        fillBoxes.add(usernameBox);
        fillBoxes.add(passwordBox);
        fillBoxes.add(retypePasswordBox);
        fillBoxes.add(emailBox);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateInputs()) {

                    String username = ((EditText) getView().findViewById(R.id.usernameEditTextSignup)).getText().toString();
                    String password = ((EditText) getView().findViewById(R.id.passwordEditTextSignup)).getText().toString();
                    String email = ((EditText) getView().findViewById(R.id.emailEditTextSignup)).getText().toString();

                    User user = mUserDAO.createUser(username, password, email,0.0);

                    // TODO: Delete after checking username is in database
                    User userCheck = mUserDAO.getUserByID(user.getId());

                    Log.e(TAG, "User created with. ID#" + userCheck.getId() +
                            "\nUsername: " +  userCheck.getUsername() +
                            "\nPassword: " + userCheck.getPassword() +
                            "\nEmail: " + userCheck.getEmail());
                    // TODO: TO HERE

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new Login()).commit();
                }
                else
                {

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

    private boolean validateInputs()
    {
        boolean allGood = true;

        for(RelativeLayout box : fillBoxes)
        {
            EditText textBox = (EditText) box.getChildAt(0);
            String inputText = textBox.getText().toString();
            ImageView errorImage = (ImageView) box.getChildAt(1);

            // Check for no inputs in each box
            if (inputText.length() == 0) {
                errorImage.setVisibility(View.VISIBLE);
                allGood = false;
            }
            else
            {
                errorImage.setVisibility(View.INVISIBLE);
            }
        }

        // Check for username viability
        String username = ((EditText) getView().findViewById(R.id.usernameEditTextSignup))
                .getText().toString();
        // Check if Username is over 6 Characters
        if (username.length() < 6)
        {
            allGood = false;
            Toast.makeText(getActivity(), "Username is too short.", Toast.LENGTH_SHORT).show();
            usernameBox.getChildAt(1).setVisibility(View.VISIBLE);
        }

        // Check if Username Exists
        for(User user : userList)
        {
            if (user.getUsername().equals(username)) {
                Toast.makeText(getActivity(), "Username taken", Toast.LENGTH_SHORT).show();
                usernameBox.getChildAt(1).setVisibility(View.VISIBLE);
                allGood = false;
            }
        }
        // Check for password viability
        String password = ((EditText) getView().findViewById(R.id.passwordEditTextSignup))
                .getText().toString();
        String retypePass = ((EditText) getView().findViewById(R.id.reEnterPassordEditText))
                .getText().toString();

        // Check if password is over 4 characters
        if (password.length() < 6)
        {
            allGood = false;
            Toast.makeText(getActivity(), "Password is too short.", Toast.LENGTH_SHORT).show();
            passwordBox.getChildAt(1).setVisibility(View.VISIBLE);
        }
        // Check if passwords match
        if (!password.equals(retypePass))
        {
            allGood = false;
            Toast.makeText(getActivity(),"Passwords do not match.", Toast.LENGTH_SHORT).show();
            passwordBox.getChildAt(1).setVisibility(View.VISIBLE);
            retypePasswordBox.getChildAt(1).setVisibility(View.VISIBLE);
        }

        //Check for email viability
        if (!Patterns.EMAIL_ADDRESS.matcher(((EditText)emailBox.getChildAt(0))
                .getText().toString()).matches())
        {
            allGood = false;
            Toast.makeText(getActivity(), "Invalid email address.", Toast.LENGTH_SHORT).show();
            emailBox.getChildAt(1).setVisibility(View.VISIBLE);
        }

        // Reset all error images if correct
        if (allGood)
        {
            for(RelativeLayout box : fillBoxes)
            {
                ImageView errorImage = (ImageView) box.getChildAt(1);
                errorImage.setVisibility(View.INVISIBLE);
            }
        }

        return allGood;
    }
}
