package com.soomter;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import net.hockeyapp.android.CrashManager;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{


    private static final String MAIN_PLACEHOLDER = "main";
    private static final String NOTIFICATIONS_PLACEHOLDER = "notifications";
    private APIInterface apiInterface;
    private SharedPref prefObj;
    public ExtendedBottomNavigationView bottomNavigationView;
    public BottomNavigationViewBehavior bottomNavigationViewBehavior;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SSLCertificateHandler.nuke();

        this.savedInstanceState = savedInstanceState;

        if (getSharedPreferences("language", MODE_PRIVATE).getString("languageToLoad", "en_US").equals("ar")) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        prefObj = new SharedPref(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = (ExtendedBottomNavigationView) findViewById(R.id.the_bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        bottomNavigationViewBehavior = new BottomNavigationViewBehavior();
        layoutParams2.setBehavior(bottomNavigationViewBehavior);
        bottomNavigationViewBehavior.slideUp(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //bottomNavigationView.setSelectedItemId(R.id.action_item1);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        /*BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notifications_badge, bottomNavigationMenuView, false);

        itemView.addView(badge);*/

        bottomNavigationView.setSelectedItemId(R.id.action_item1);

        /*if (sharedPref.getProductsCatg() == null) {
            getProductsCategories();
        }*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            /*MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("main_frag");
            if(mainFragment != null)
                mainFragment.onDestroyView();
       */
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

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        checkForCrashes();
    }

    private void loadMainFrag(){
        if (savedInstanceState == null) {
            //Handle the initial fragment transaction
            if (prefObj.getUser() == null)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, CountriesFragment.newInstance(null, null)).commit();
            else
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, MainFragment.newInstance(null, null)).commit();
        }
       /* Fragment fragment = getSupportFragmentManager().findFragmentByTag(MAIN_PLACEHOLDER);
        if (fragment == null) {
            fragment = new MainFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, fragment, MAIN_PLACEHOLDER)
                .commit();*/
    }

    private void loadNotificationsFrag(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(NOTIFICATIONS_PLACEHOLDER);
        if (fragment == null) {
            fragment = new NotificationsFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, fragment, NOTIFICATIONS_PLACEHOLDER)
                .commit();
    }

    /*private void loadNotificationsFrag(){
        getFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.frag_container,
                        CompanyProfileFragment.newInstance(arrayList.get(position).Id ,
                                arrayList.get(position).CompanyName)).hide(this).addToBackStack(null).commit();
    }
*/
    private void checkForCrashes() {
        CrashManager.register(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_item3:
                loadNotificationsFrag();
                break;
            case R.id.action_item1:
                loadMainFrag();
                break;
                case R.id.action_item2:

                    break;
        }
        return true;
    }
}