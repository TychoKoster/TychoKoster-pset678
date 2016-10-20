package koster.tychokoster_pset678;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ArtInfoFragment extends Fragment {

    TextView title;
    TextView year;
    ImageView image;
    TextView artist;
    TextView description;
    Button button;
    JSONObject art_object;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artinfo_layout, container, false);
        title = (TextView) view.findViewById(R.id.title);
        year = (TextView) view.findViewById(R.id.year);
        image = (ImageView) view.findViewById(R.id.image);
        artist = (TextView) view.findViewById(R.id.artist);
        description = (TextView) view.findViewById(R.id.description);
        try {
            createLayout();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void createLayout() throws ExecutionException, InterruptedException, JSONException {
        String id = MainActivity.myBundle.getString("id");
        String url = MainActivity.myBundle.getString("url");
        RetrieveSingleArt retrieve = new RetrieveSingleArt();
        String retrieved_art = retrieve.execute(id).get();
        JSONObject total_object = new JSONObject(retrieved_art);
        art_object = total_object.getJSONObject("artObject");
        title.setText(art_object.get("title").toString());
        String year_text = "Year: " + art_object.getJSONObject("dating").get("year").toString();
        year.setText(year_text);
        if (url != null && url.equals("")) {
            image.setImageResource(R.drawable.noimage);
        } else {
            Picasso.with(getActivity()).load(url).fit().into(image);
        }
        String artis_text = "Artist: " + art_object.getJSONArray("principalMakers").getJSONObject(0).get("name").toString();
        artist.setText(artis_text);
        description.setText(art_object.get("description").toString());
    }

    public void addToFavorites() throws JSONException {
        String title = art_object.get("title").toString();
        String url = MainActivity.myBundle.getString("url");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference titleRef = database.getReference("title");
        titleRef.setValue(title);
        DatabaseReference urlRef = database.getReference("url");
        urlRef.setValue(url);
    }
}
