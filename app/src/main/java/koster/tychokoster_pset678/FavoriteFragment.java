package koster.tychokoster_pset678;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

// This fragment is the fragment that shows the favorite page of the user.

public class FavoriteFragment extends Fragment {
    ArrayList<Art> artlist = new ArrayList<>();
    DatabaseReference artRef;
    FavoriteAdapter adapter;

    ListView list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_layout, container, false);
        list = (ListView) view.findViewById(R.id.favorite_list);
        setUpDB();
        retrieveArt();
        return view;
    }

    // Connect to the database and set up the references that are needed.
    public void setUpDB() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users");
        userRef = userRef.child(auth.getCurrentUser().getEmail().replaceAll("[./#$\\[\\]]", ","));
        artRef = userRef.child("Art");
    }

    // Retrieves all the Art objects from the database favorite list.
    private void retrieveArt() {
        artlist.clear();
        artRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot artsnapshot : dataSnapshot.getChildren()){
                    artlist.add(artsnapshot.getValue(Art.class));
                }
                addArtToList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Adds all the retrieved art to the listview by the use of the favorite adapter.
    public void addArtToList() {
        adapter = new FavoriteAdapter(artlist, getContext());
        list.setAdapter(adapter);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
    }

    // Removes an item from the favorite list and from the firebase database.
    public void removeItem(String id) {
        final DatabaseReference singleArtRef = artRef.child(id);
        singleArtRef.removeValue();
        Toast.makeText(getContext(), "Art removed from favorites", Toast.LENGTH_SHORT).show();
        retrieveArt();
    }



}
