package koster.tychokoster_pset678;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

// Fragment that shows the profile of the user.

public class ProfileFragment extends Fragment {
    String username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        final TextView user = (TextView) view.findViewById(R.id.username);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        ((MainActivity)getActivity()).readUsername(getContext());
        username = ((MainActivity)getActivity()).nickname;
        user.setText(username);
        // Textview to sign out the user.
        TextView signout = (TextView) view.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });
        return view;
    }
}
