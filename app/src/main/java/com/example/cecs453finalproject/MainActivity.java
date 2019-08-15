package com.example.cecs453finalproject;

//import android.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cecs453finalproject.classes.SaveSharedPreference;
import com.example.cecs453finalproject.classes.User;
import com.example.cecs453finalproject.database.DBHelper;
import com.example.cecs453finalproject.database.UsersDAO;
import com.example.cecs453finalproject.fragments.AddEditCategory;
import com.example.cecs453finalproject.fragments.AddEditExpense;
import com.example.cecs453finalproject.fragments.AppSettings;
import com.example.cecs453finalproject.fragments.ByMonthChart;
import com.example.cecs453finalproject.fragments.DailyExpense;
import com.example.cecs453finalproject.fragments.Login;
import com.example.cecs453finalproject.fragments.MonthlyIncome;
import com.example.cecs453finalproject.fragments.Reports;
import com.example.cecs453finalproject.fragments.Signup;
import com.example.cecs453finalproject.interfaces.DrawerLocker;


/*
*   Class MainActivity is the activity that hosts all the fragments in the project. Implements
*   onFragmentInteractionListener for each fragment.
* */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Login.OnFragmentInteractionListener,
        Signup.OnFragmentInteractionListener, AppSettings.OnFragmentInteractionListener,
        AddEditExpense.OnFragmentInteractionListener, Reports.OnFragmentInteractionListener,
        AddEditCategory.OnFragmentInteractionListener, DailyExpense.OnFragmentInteractionListener,
        MonthlyIncome.OnFragmentInteractionListener, DrawerLocker, ByMonthChart.OnFragmentInteractionListener {

    DBHelper dbHelper;
    private String loggedInUsername;
    private long loggedInUserId;
    private boolean showOptionsMenu = false;
    private double monthlyIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dbHelper = new DBHelper(this);

        Fragment fragment;

        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            fragment = new Login();
        }
        else
        {
            fragment = new AddEditExpense();
            String username = SaveSharedPreference.getUserName(this);
            UsersDAO mUserDAO = new UsersDAO(this);

            User checkUser = mUserDAO.getUserByUsername(username);

            setDrawerLocked(false);
            setLoggedInUsername(username);
            setLoggedInUserId(checkUser.getId());
            setMonthlyIncome(checkUser.getIncome());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return showOptionsMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, new AppSettings()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        long customerId = getLoggedInUserId();
        String username = getLoggedInUsername();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            loggedInUserId = 0;
            loggedInUsername = null;
            fragment = new Login();

        }
        // Set target layout to correct fragment, set the arguments and pass the data.
        else if (id == R.id.nav_settings) {

            fragment = new AppSettings();

        } else if (id == R.id.nav_expenses) {

            fragment = new AddEditExpense();

        } else if (id == R.id.nav_reports) {

            fragment = new Reports();
        }

        if(fragment != null) {

            fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    public long getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(long loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    @Override
    public void setDrawerLocked(boolean enabled) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (enabled) {
            invalidateOptionsMenu();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            showOptionsMenu = false;
        } else {
            invalidateOptionsMenu();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            showOptionsMenu = true;
        }
    }


}


