package koster.tychokoster_pset678;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Fragment that shows the profile search page.

public class SearchProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_profile, container, false);
        final EditText search = (EditText) view.findViewById(R.id.search_profile_text);
        Button searchbutton = (Button) view.findViewById(R.id.search_profile_button);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference usersRef = db.getReference("Users");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // When pressed on the search button it will search the database for the user.
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchprofile = search.getText().toString();
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean found = false;
                        // Searches the whole User database.
                        for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){
                            // if the username is in the database, then it will instantly show the user profile page of the
                            // searched profile by calling the searchedprofile fragment.
                            if(usersnapshot.child("nickname").getValue(String.class).equals(searchprofile)){
                                found = true;
                                final String email = usersnapshot.child("email").getValue(String.class).replaceAll("[./#$\\[\\]]", ",");
                                MainActivity.myBundle.putString("email_user", email);
                                MainActivity.myBundle.putString("nickname_user", usersnapshot.child("nickname").getValue(String.class));
                                SearchedProfileFragment searchedprofilefragment = new SearchedProfileFragment();
                                fragmentTransaction.replace(R.id.content_main, searchedprofilefragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                        // if no user found it will show a message.
                        if(!found){
                            Toast.makeText(getContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }
}
