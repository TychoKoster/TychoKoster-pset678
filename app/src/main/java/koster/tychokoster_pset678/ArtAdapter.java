package koster.tychokoster_pset678;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

// This adapter is used for the setting the search fragment.

class ArtAdapter extends BaseAdapter {
    private ArrayList<String> artlist;
    private ArrayList<String> artposters;
    private Context context;

    ArtAdapter(ArrayList<String> artlist, ArrayList<String> artposters, Context context){
        this.artlist = artlist;
        this.context = context;
        this.artposters = artposters;
    }

    // Gets the size of the artlist retrieved.
    @Override
    public int getCount() {
        return artlist.size();
    }

    // Gets the art at a certain position.
    @Override
    public Object getItem(int position) {
        return artlist.get(position);
    }

    // Gets the position.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Sets adapter view for the search listview.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.search_item, null);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        // Textview with the title of the art.
        title.setText(artlist.get(position));
        ImageView image = (ImageView) view.findViewById(R.id.image);
        // Imageview with the image url of the art.
        String url = artposters.get(position);
        // If there is no url, then a drawable of a no image will be set to the imageview.
        if(url.equals("")){
            image.setImageResource(R.drawable.noimage);
        }
        else {
            // Picasso used for setting the image to the retrieved image from the url.
            Picasso.with(context).load(url).fit().into(image);
        }
        return view;
    }
}
