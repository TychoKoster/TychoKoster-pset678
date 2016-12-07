package koster.tychokoster_pset678;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

// Fragment that shows the register page.

public class RegisterFragment extends Fragment {
    View v;
    FirebaseDatabase db;
    DatabaseReference ref;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.register_layout, container, false);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Users");
        Button register = (Button) v.findViewById(R.id.registerbutton);
        // When pressed on the register button it proceeds to the registerUser method.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        return v;
    }

    // Registers the user when the user does not yet exists.
    private void registerUser() {
        EditText nickname = (EditText) v.findViewById(R.id.nickname_register);
        EditText email_register = (EditText) v.findViewById(R.id.email_register);
        EditText password_register = (EditText) v.findViewById(R.id.password_register);
        EditText password2 = (EditText) v.findViewById(R.id.password2_register);
        final String nicknametext = nickname.getText().toString();
        final String emailtext = email_register.getText().toString();
        final String passwordtext = password_register.getText().toString();
        final String password2text = password2.getText().toString();
        // Check if all fields are filled in
        if(nicknametext.trim().equals("") || emailtext.trim().equals("") || passwordtext.trim().equals("") || password2text.trim().equals("")){
            Toast.makeText(getContext(), "One field was not filled in correctly", Toast.LENGTH_SHORT).show();
        }
        // Checks if both password fields are the same.
        else if(!(passwordtext.equals(password2text))) {
            Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else {
            final DatabaseReference userRef = ref.child(nicknametext);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Creates user if not yet exists
                    if(dataSnapshot.getValue() == null){
                        createUser(emailtext, passwordtext, nicknametext);
                    }
                    else {
                        Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    // Creates user with email and password in the firebase.
    private void createUser(final String emailtext, final String passwordtext, final String nicknametext) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailtext.trim(), passwordtext.trim()).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!(task.isSuccessful())){
                    Toast.makeText(getContext(), "Register failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    final DatabaseReference userRef = ref.child(emailtext.replaceAll("[./#$\\[\\]]", ","));
                    User user = new User(emailtext, nicknametext);
                    Toast.makeText(getContext(),"Register succesfull", Toast.LENGTH_SHORT).show();
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().
                            getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    userRef.setValue(user);
                }
            }
        });
    }

    // Gets the main activity of the current fragment.
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            activity = (MainActivity) this.getActivity();
        }
        catch(ClassCastException e)
        {
            e.printStackTrace();
        }
    }


}
