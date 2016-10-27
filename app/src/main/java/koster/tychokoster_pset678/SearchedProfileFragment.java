package koster.tychokoster_pset678;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

// This fragment shows the profile of the user that was searched.

public class SearchedProfileFragment extends Fragment {
    ArrayList<Art> artlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searched_profile, container, false);
        ListView list = (ListView) view.findViewById(R.id.searched_list);
        TextView nickname = (TextView) view.findViewById(R.id.username_searched);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // Retrieves the username and email of the searched user.
        String username = MainActivity.myBundle.getString("nickname_user");
        String email = MainActivity.myBundle.getString("email_user");
        nickname.setText(username);
        DatabaseReference userRef = db.getReference("Users").child(email);
        // Retrieves the favorite list of the searched user from the database.
        userRef.child("Art").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot artsnapshot : dataSnapshot.getChildren()) {
                    artlist.add(artsnapshot.getValue(Art.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Sets the listview to the created adapter.
        UserAdapter adapter = new UserAdapter(artlist, getContext());
        list.setAdapter(adapter);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Shows the information page of the art that was clicked in the favorite list.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtInfoFragment artinfofragment = new ArtInfoFragment();
                fragmentTransaction.replace(R.id.content_main, artinfofragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        adapter.notifyDataSetChanged();
        return view;
    }
}
