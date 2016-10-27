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

// Adapter used to show the favorite list of the searched user.

class UserAdapter extends BaseAdapter {
    private ArrayList<Art> artlist;
    private Context context;

    // Creates adapter.
    UserAdapter(ArrayList<Art> artlist, Context context) {
        this.artlist = artlist;
        this.context = context;
    }

    // Gets the size of the favorite list.
    @Override
    public int getCount() {
        return artlist.size();
    }

    // Gets the item at a certain position in the favorite list.
    @Override
    public Object getItem(int position) {
        return artlist.get(position);
    }

    // Gets the item id of a item at a certain position in the favorite list.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Gets the view of the adapter to use for the list view.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.search_item, null);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(artlist.get(position).getTitle());
        ImageView image = (ImageView) view.findViewById(R.id.image);
        String url = artlist.get(position).getUrl();
        // If no url imageview is filled in with drawable no image.
        if(url.equals("")){
            image.setImageResource(R.drawable.noimage);
        }
        else {
            // Picasso used to fill in the image view with the retrieved image from the url.
            Picasso.with(context).load(url).fit().into(image);
        }
        return view;
    }
}
