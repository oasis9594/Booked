package com.example.dell.assignment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Assignment");
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        if(bundle==null)
        {
            navigationView.getMenu().getItem(0).setChecked(true);
            StoreFragment fragment=new StoreFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Log.i(MyConstants.TAG, "TransactionBegun");
            fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
            Log.i(MyConstants.TAG, "ReplaceCalled");
        }
        else {
            if(bundle.getString(MyConstants.MODE_KEY)==null)
                Log.w(MyConstants.TAG, "mode null in main activity");
            if(bundle.getString(MyConstants.MODE_KEY).equals(MyConstants.SEARCH_KEY))
            {
                navigationView.getMenu().getItem(1).setChecked(true);
                Bundle bundle1=new Bundle();
                bundle1.putString(MyConstants.MODE_KEY, MyConstants.SEARCH_KEY);
                SearchFragment searchFragment=new SearchFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                searchFragment.setArguments(bundle1);
                Log.i(MyConstants.TAG, "TransactionBegun");
                fragmentTransaction.replace(R.id.fragment_container, searchFragment).commit();
                Log.i(MyConstants.TAG, "ReplaceCalled");
            }
            else {
                navigationView.getMenu().getItem(2).setChecked(true);
                Bundle bundle1=new Bundle();
                bundle1.putString(MyConstants.MODE_KEY, MyConstants.REMOVE_KEY);
                SearchFragment searchFragment=new SearchFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                searchFragment.setArguments(bundle1);
                Log.i(MyConstants.TAG, "TransactionBegun");
                fragmentTransaction.replace(R.id.fragment_container, searchFragment).commit();
                Log.i(MyConstants.TAG, "ReplaceCalled");
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_store) {
            StoreFragment storeFragment=new StoreFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Log.i(MyConstants.TAG, "TransactionBegun");
            fragmentTransaction.replace(R.id.fragment_container, storeFragment).commit();
            Log.i(MyConstants.TAG, "ReplaceCalled");

        } else if (id == R.id.nav_search) {

            Bundle bundle=new Bundle();
            bundle.putString(MyConstants.MODE_KEY, MyConstants.SEARCH_KEY);
            SearchFragment searchFragment=new SearchFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            searchFragment.setArguments(bundle);
            Log.i(MyConstants.TAG, "TransactionBegun");
            fragmentTransaction.replace(R.id.fragment_container, searchFragment).commit();
            Log.i(MyConstants.TAG, "ReplaceCalled");

        } else if (id == R.id.nav_remove) {

            Bundle bundle=new Bundle();
            bundle.putString(MyConstants.MODE_KEY, MyConstants.REMOVE_KEY);
            SearchFragment searchFragment=new SearchFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            searchFragment.setArguments(bundle);
            Log.i(MyConstants.TAG, "TransactionBegun");
            fragmentTransaction.replace(R.id.fragment_container, searchFragment).commit();
            Log.i(MyConstants.TAG, "ReplaceCalled");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer!=null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
