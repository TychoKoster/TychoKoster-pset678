package koster.tychokoster_pset678;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

// This is the fragment that shows all the information about a piece of art on one page.
// It is also possible to add the art piece to the users favorite list.

public class ArtInfoFragment extends Fragment {

    TextView title;
    TextView year;
    ImageView image;
    TextView artist;
    TextView description;
    JSONObject art_object;
    Art art;
    String unique_id;
    Button add;
    DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artinfo_layout, container, false);
        title = (TextView) view.findViewById(R.id.title);
        year = (TextView) view.findViewById(R.id.year);
        image = (ImageView) view.findViewById(R.id.image);
        artist = (TextView) view.findViewById(R.id.artist);
        description = (TextView) view.findViewById(R.id.description);
        add = (Button) view.findViewById(R.id.addbutton);
        setUpDB();
        try {
            createLayout();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // When clicked on the add to favorites button the addtofavo method is called.
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavo();
            }
        });
        return view;
    }

    // Adds the piece of art to the favorite list of the user.
    private void addToFavo() {
        DatabaseReference artRef = userRef.child("Art");
        try {
            // Creates Art object to store in the favorite list.
            art = new Art(art_object.get("title").toString(), MainActivity.myBundle.getString("url"), unique_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Use unique id of the art to store in the database.
        final DatabaseReference singleArtRef = artRef.child(unique_id);
        singleArtRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the item is not yet in the database, it will add the item.
                // Otherwise it will send a message that it is already stored.
                if(dataSnapshot.getValue() == null){
                    singleArtRef.setValue(art);
                    Toast.makeText(getContext(), "Art added to favorites", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Art already in favorites", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Creates the entire layout of the art information page.
    public void createLayout() throws ExecutionException, InterruptedException, JSONException {
        String id = MainActivity.myBundle.getString("id");
        String url = MainActivity.myBundle.getString("url");
        RetrieveSingleArt retrieve = new RetrieveSingleArt();
        String retrieved_art = retrieve.execute(id).get();
        JSONObject total_object = new JSONObject(retrieved_art);
        art_object = total_object.getJSONObject("artObject");
        title.setText(art_object.get("title").toString());
        String year_text = "Year: " + art_object.getJSONObject("dating").get("year").toString();
        year.setText(year_text);
        // If the url is empty show the no image drawable.
        if (url != null && url.equals("")) {
            image.setImageResource(R.drawable.noimage);
        } else {
            // Picasso used to place the image retrieved from the url in the imageview.
            Picasso.with(getActivity()).load(url).fit().into(image);
        }
        String artis_text = "Artist: " + art_object.getJSONArray("principalMakers").getJSONObject(0).get("name").toString();
        artist.setText(artis_text);
        description.setText(art_object.get("description").toString());
        unique_id = art_object.get("objectNumber").toString();
    }

    // Connects the database and sets up the references needed.
    private void setUpDB() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        userRef = userRef.child(auth.getCurrentUser().getEmail().replaceAll("[./#$\\[\\]]", ","));
    }

}
