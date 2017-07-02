package ca.danielw.rankr.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateLeagueActivity;
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.activities.SignInActivity;
import ca.danielw.rankr.utils.Constants;

public class SignInPasswordFragment extends Fragment{
    private static final String TAG = "SignInPasswordFragment";

    private Button nextBtn;
    private EditText etPassword;
    private String password;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.sign_in_password_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();

        nextBtn = (Button) view.findViewById(R.id.btnNext);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextBtn.setEnabled(true);
                password = etPassword.getText().toString();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity.mPassword = password;

                //Start the main leaderboard activity
                signInUser();
            }
        });


        return view;
    }

    private void signInUser(){
        mAuth.signInWithEmailAndPassword(SignInActivity.mEmail, SignInActivity.mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(Constants.LEAGUE_NAME, CreateLeagueActivity.mLeagueName);
                            editor.commit();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
