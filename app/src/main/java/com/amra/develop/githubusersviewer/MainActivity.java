package com.amra.develop.githubusersviewer;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Anton Stukov
 * 12.02.2015
 */
public class MainActivity extends ActionBarActivity {
    Fragment fragment;

    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Fragment that show users from gitHub.
        fragment = new UsersFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle actionbar buttons
        switch(item.getItemId()) {
            // Clear cache
            case R.id.clear_cache: ((UsersFragment)fragment).clearCache(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}