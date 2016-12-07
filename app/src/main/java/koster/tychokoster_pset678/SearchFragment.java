package koster.tychokoster_pset678;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// Fragment that show the search art page.

public class SearchFragment extends Fragment {

    // Creates all variables used in the class.
    ArrayList<String> artlist = new ArrayList<>();
    ArrayList<String> urllist = new ArrayList<>();
    ArrayList<String> idlist = new ArrayList<>();
    RetrieveArt retrieve;
    String retrieved_art;
    JSONObject json_art;
    JSONArray art_array;
    ListView list;
    EditText search_text;
    ArtInfoFragment artinfofragment = new ArtInfoFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        search_text = (EditText) view.findViewById(R.id.search_art);
        list = (ListView) view.findViewById(R.id.searchlist);
        TextView empty = (TextView) view.findViewById(R.id.emptysearch);
        list.setEmptyView(empty);
        return view;
    }

    // Searches for the art with the use of the Rijksmuseum API.
    public void search() throws ExecutionException, InterruptedException, JSONException {
        idlist.clear();
        artlist.clear();
        urllist.clear();
        String text = search_text.getText().toString();
        // Closes keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().
                getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if(text.equals("")){
            Toast.makeText(getActivity(),"Search was empty", Toast.LENGTH_SHORT).show();
        }
        else {
            retrieve = new RetrieveArt();
            retrieved_art = retrieve.execute(text).get();
            json_art = new JSONObject(retrieved_art);
            art_array = json_art.getJSONArray("artObjects");
            // Stores all the retrieved art in arraylists with titles and urls.
            for(int i=0; i<art_array.length(); i++){
                JSONObject object = art_array.getJSONObject(i);
                String title = object.get("title").toString();
                String id = object.get("objectNumber").toString();
                if(object.get("webImage").equals(null)){
                    String url = "";
                    urllist.add(i, url);
                }
                else {
                    JSONObject image = object.getJSONObject("webImage");
                    String url = image.get("url").toString();
                    urllist.add(i, url);
                }
                idlist.add(i, id);
                artlist.add(i, title);
            }
            // Sets the listview with all the retrieved art from the search, with the use of the art adapter.
            ArtAdapter adapter = new ArtAdapter(artlist, urllist, getActivity());
            list.setAdapter(adapter);
            // When clicked on an item in the retrieved list it shows the information page of this piece of art.
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MainActivity.myBundle.putString("id", idlist.get(position));
                    MainActivity.myBundle.putString("url", urllist.get(position));
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, artinfofragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
