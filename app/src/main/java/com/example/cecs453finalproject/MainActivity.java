package com.example.cecs453finalproject;

//import android.app.FragmentManager;

import android.graphics.Point;
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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cecs453finalproject.database.DBHelper;
import com.example.cecs453finalproject.fragments.AppSettings;
import com.example.cecs453finalproject.fragments.DailyExpense;
import com.example.cecs453finalproject.fragments.ExpenseItem;
import com.example.cecs453finalproject.fragments.Expenses;
import com.example.cecs453finalproject.fragments.Login;
import com.example.cecs453finalproject.fragments.MonthlyIncome;
import com.example.cecs453finalproject.fragments.Reports;
import com.example.cecs453finalproject.fragments.Signup;
import com.example.cecs453finalproject.interfaces.DrawerLocker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Login.OnFragmentInteractionListener,
        Signup.OnFragmentInteractionListener, AppSettings.OnFragmentInteractionListener,
        Expenses.OnFragmentInteractionListener, Reports.OnFragmentInteractionListener,
        ExpenseItem.OnFragmentInteractionListener, DailyExpense.OnFragmentInteractionListener,
        MonthlyIncome.OnFragmentInteractionListener, DrawerLocker {

    DBHelper dbHelper;
    private String loggedInUsername;
    private long loggedInUserId;

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

        if(savedInstanceState == null) {
            fragment = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainContentFrameContainer, fragment).commit();

        }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            loggedInUserId = 0;
            loggedInUsername = null;
            fragment = new Login();

        } // TODO: Do we need this in the navigation bar????
//        else if (id == R.id.nav_signup) {
//            fragment = new Signup();
//
//        }
        //TODO: Pass Username and ID to each Fragment
        else if (id == R.id.nav_settings) {
            fragment = new AppSettings();
        } else if (id == R.id.nav_expenses) {
            fragment = new Expenses();
        } else if (id == R.id.nav_reports) {
            fragment = new Reports();
        }

        if(fragment != null) {
            fragmentManager.popBackStack();
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

    @Override
    public void setDrawerLocked(boolean enabled) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (enabled) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public int getScreenWidth()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}


