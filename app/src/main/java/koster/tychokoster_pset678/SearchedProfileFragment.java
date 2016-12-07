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
import java.util.List;

// This fragment shows the profile of the user that was searched.

public class SearchedProfileFragment extends Fragment {
    ArrayList<Art> artlist = new ArrayList<>();
    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searched_profile, container, false);
        list = (ListView) view.findViewById(R.id.searched_list);
        TextView nickname = (TextView) view.findViewById(R.id.username_searched);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // Retrieves the username and email of the searched user.
        String username = MainActivity.myBundle.getString("nickname_user");
        String email = MainActivity.myBundle.getString("email_user");
        nickname.setText(username);
        DatabaseReference userRef = db.getReference("Users").child(email);
        // Retrieves the favorite list of the searched user from the database.
        artlist.clear();
        userRef.child("Art").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot artsnapshot : dataSnapshot.getChildren()) {
                    artlist.add(artsnapshot.getValue(Art.class));
                }
                setAdapter(artlist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    // Sets the adapter for the favorite list of the searched user.
    private void setAdapter(final ArrayList<Art> artlist) {
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
                MainActivity.myBundle.putString("id", artlist.get(position).getId());
                MainActivity.myBundle.putString("url", artlist.get(position).getUrl());
                fragmentTransaction.replace(R.id.content_main, artinfofragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        adapter.notifyDataSetChanged();
    }
}
