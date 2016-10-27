package koster.tychokoster_pset678;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Fragment that shows the profile of the user.

public class ProfileFragment extends Fragment {
    String nickname;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        final TextView username = (TextView) view.findViewById(R.id.username);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference userRef = usersRef.child(auth.getCurrentUser().getEmail().replaceAll("[./#$\\[\\]]", ","));
        // Shows the username of the user.
        userRef.child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nickname = dataSnapshot.getValue(String.class);
                username.setText(nickname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
