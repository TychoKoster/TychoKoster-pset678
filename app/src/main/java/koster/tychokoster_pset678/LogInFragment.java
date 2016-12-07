package koster.tychokoster_pset678;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Fragment that shows the Log in page

public class LogInFragment extends Fragment {
    MainActivity activity;
    View view;
    String username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        Button login = (Button) view.findViewById(R.id.loginbutton);
        // When pressed on the long in button it checks if all fields are filled in.
        // If so it proceeds to the logIn method.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) view.findViewById(R.id.email_login);
                EditText password = (EditText) view.findViewById(R.id.password_login);
                String password_text = password.getText().toString();
                String email_text = email.getText().toString();
                if(email_text.trim().length() == 0 || password_text.trim().length() == 0) {
                    Toast.makeText(getContext(), "One of the fields is not filled in correctly", Toast.LENGTH_SHORT).show();
                }
                else {
                    logIn(email_text, password_text);
                }
            }
        });
        TextView register = (TextView) view.findViewById(R.id.register_textview);
        // When clicked on the register here textview, it goes to the register fragment.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerfragment = new RegisterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, registerfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    // Tries to log in the user, if it failed it will show a message, if not it will log in.
    private void logIn(String email_text, String password_text) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Closes keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().
                getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final DatabaseReference usersRef = database.getReference("Users");
        auth.signInWithEmailAndPassword(email_text, password_text).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getContext(),"Failed to log in",Toast.LENGTH_SHORT).show();
                }
                else{
                    // After log in, write the username to a data file on the phone so it can be retrieved, easily.
                    DatabaseReference userRef = usersRef.child(auth.getCurrentUser().getEmail().replaceAll("[./#$\\[\\]]", ","));
                    userRef.child("nickname").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            username = dataSnapshot.getValue(String.class);
                            ((MainActivity)getActivity()).writeUsername(getContext(), username);
                            Toast.makeText(getContext(),"Logged in", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    // Get the main activity used in this fragment.
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            activity = (MainActivity) getActivity();
        }
        catch(ClassCastException e)
        {
            e.printStackTrace();
        }
    }
}
