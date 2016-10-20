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

class ArtAdapter extends BaseAdapter {
    private ArrayList<String> artlist;
    private ArrayList<String> artposters;
    private Context context;

    ArtAdapter(ArrayList<String> artlist, ArrayList<String> artposters, Context context){
        this.artlist = artlist;
        this.context = context;
        this.artposters = artposters;
    }

    @Override
    public int getCount() {
        return artlist.size();
    }

    @Override
    public Object getItem(int position) {
        return artlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.search_item, null);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(artlist.get(position));
        ImageView image = (ImageView) view.findViewById(R.id.image);
        String url = artposters.get(position);
        if(url.equals("")){
            image.setImageResource(R.drawable.noimage);
        }
        else {
            Picasso.with(context).load(url).fit().into(image);
        }
        return view;
    }
}
