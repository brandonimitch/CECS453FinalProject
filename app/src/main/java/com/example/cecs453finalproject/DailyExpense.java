package com.example.cecs453finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyExpense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyExpense extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // Create variables for control objects.
    private EditText categoryName;
    private EditText expenseAmount;
    private Button dailyExpenseBtn;
    private Spinner spinner;
    private String categoryNameEntered;
    private double expenseAmountEntered;


    private OnFragmentInteractionListener mListener;

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
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily_expense, container, false);

        // Define control objects.
        categoryName = v.findViewById(R.id.dailyExpenseEditText1);
        expenseAmount = v.findViewById(R.id.dailyExpenseEditText2);
        dailyExpenseBtn = v.findViewById(R.id.dailyExpenseBtn);


        /*
        *   Adapter class to convert data for recycler view and spinner.
        * */
        class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {


            @NonNull
            @Override
            public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull WordViewHolder wordViewHolder, int i) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }

            /*
            *   View Holder class for converting to recycler view or spinner.
            * */
            class WordViewHolder extends RecyclerView.ViewHolder {

                public WordViewHolder(@NonNull View itemView) {
                    super(itemView);
                }
            }
        }



        spinner = v.findViewById(R.id.dailyExpenseSpinner);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        WordListAdapter<CharSequence> adapter = WordListAdapter.createFromResource(this,
//                R.array.planets_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);


        dailyExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryNameEntered = categoryName.getText().toString();
                expenseAmountEntered = Double.parseDouble(expenseAmount.getText().toString());

                ///// TODO: ADD FUNCTIONALITY TO EDIT DAILY EXPENSE AMOUNT IN DATABASE ///////

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
}
