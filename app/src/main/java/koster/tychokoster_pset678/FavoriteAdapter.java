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

// This adapter is used for the favorite list.

class FavoriteAdapter extends BaseAdapter {
    private ArrayList<Art> artlist;
    private Context context;

    // Creates the favorite adapter with the use of the context and all the Art objects.
    FavoriteAdapter(ArrayList<Art> artlist, Context context) {
        this.artlist = artlist;
        this.context = context;
    }

    // Gets the size of the favorite list.
    @Override
    public int getCount() {
        return artlist.size();
    }

    public void update(ArrayList<Art> new_artlist) {
        artlist.clear();
        this.artlist = new_artlist;
    }

    // Gets the item at a certain position in the favorite list.
    @Override
    public Object getItem(int position) {
        return artlist.get(position);
    }

    // Gets the item id at a certain position.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Initializes the view for the favorite list adapter.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.favorite_item, null);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(artlist.get(position).getTitle());
        ImageView image = (ImageView) view.findViewById(R.id.image);
        String url = artlist.get(position).getUrl();
        ImageView delete = (ImageView) view.findViewById(R.id.delete);
        final String id = artlist.get(position).getId();
        // When pressed on the delete icon, the item will be removed from the favorite list.
        // This is done with the use of a method in the main activity.
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).removelist(id, position);

            }
        });
        // Sets no image drawable in the imageview when url is empty.
        if(url.equals("")){
            image.setImageResource(R.drawable.noimage);
        }
        else {
            // Picasso used to set the imageview to the retrieved image from the url.
            Picasso.with(context).load(url).fit().into(image);
        }
        return view;
    }
}
