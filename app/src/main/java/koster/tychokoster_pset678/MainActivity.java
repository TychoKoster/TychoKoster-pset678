package koster.tychokoster_pset678;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// This is the main activity which is used for the navigation drawer and handling all the fragment
// changes that are done.

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Initializes all fragments.
    SearchFragment searchfragment = new SearchFragment();
    FavoriteFragment favoritefragment = new FavoriteFragment();
    ProfileFragment profilefragment = new ProfileFragment();
    HomeFragment homefragment = new HomeFragment();
    SearchProfileFragment searchprofilefragment = new SearchProfileFragment();
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authlistener;
    private String file = "nickname.data";
    String nickname;
    int currentstate;

    public static Bundle myBundle = new Bundle();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Initializes the navigation drawer.
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        // Checks if the user is logged in or not.
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // If the user is logged in, it will go to the home fragment.
                if(user != null){
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    if(savedInstanceState == null ) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, homefragment).addToBackStack(null).commit();
                    }
                }
                // Otherwise it will go to the log-in fragment.
                else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    LogInFragment loginfragment = new LogInFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, loginfragment).addToBackStack(null).commit();
                }
            }
        };
        // This is used to select the correct selected item on the navigation drawer when the user pressed the back button.
        this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Fragment current = getCurrentFragment();
                        if (current instanceof SearchFragment) {
                            navigationView.setCheckedItem(R.id.nav_search);
                            currentstate = 1;
                        } else if (current instanceof FavoriteFragment){
                            navigationView.setCheckedItem(R.id.nav_favorites);
                            currentstate = 2;
                        }
                        else if (current instanceof ArtInfoFragment) {
                            navigationView.setCheckedItem(R.id.nav_search);
                            currentstate = 3;
                        }
                        else if (current instanceof ProfileFragment) {
                            navigationView.setCheckedItem(R.id.nav_profle);
                            currentstate = 4;
                        }
                        else if (current instanceof SearchProfileFragment) {
                            navigationView.setCheckedItem(R.id.nav_search_profile);
                            currentstate = 5;
                        }
                        else {
                            navigationView.setCheckedItem(R.id.nav_home);
                        }
                    }
                });
    }

    // Add auth listener on start
    public void onStart()
    {
        super.onStart();
        auth.addAuthStateListener(authlistener);
    }

    // Removes auth listener on stop.
    public void onStop(){
        super.onStop();
        if (authlistener != null)
        {
            auth.removeAuthStateListener(authlistener);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    // Gets the current fragment.
    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.content_main);
    }

    // When the user pressed the back button it will pop the first fragment in the backstack.
    // Or it will close the navigation drawer when it is open.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 10)
        {
            getSupportFragmentManager().popBackStack();
        }
        else {
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

    // This will go the fragment that is selected in the navigation drawer. And adds the fragment
    // to the backstack.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            fragmentTransaction.replace(R.id.content_main, searchfragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_favorites) {
            fragmentTransaction.replace(R.id.content_main, favoritefragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profle) {
            fragmentTransaction.replace(R.id.content_main, profilefragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_home) {
            fragmentTransaction.replace(R.id.content_main, homefragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_search_profile) {
            fragmentTransaction.replace(R.id.content_main, searchprofilefragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // The on click for the search button, in search art.
    public void searchArt(View v) throws InterruptedException, ExecutionException, JSONException {
        searchfragment.search();
    }

    // Called when the remove icon in the favorite list is pressed.
    public void removelist(String id, int position) {
        favoritefragment.removeItem(id, position);
    }

    // Reads the username from the data file on the phone.
    public void readUsername(Context context) {
        try {
            FileInputStream input = context.openFileInput(file);
            ObjectInputStream object_in = new ObjectInputStream(input);
            nickname = object_in.readObject().toString();
            input.close();
            object_in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Writes the username to a data file on the phone.
    public void writeUsername(Context context, String username) {
        try {
            nickname = username;
            FileOutputStream output = context.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream object_out = new ObjectOutputStream(output);
            object_out.writeObject(nickname);
            output.close();
            object_out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
