package koster.tychokoster_pset678;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Yvonn on 20-10-2016.
 */

public class FavoriteFragment extends Fragment {

    ListView list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_layout, container, false);
        list = (ListView) view.findViewById(R.id.favorite_list);
        return view;
    }



}
