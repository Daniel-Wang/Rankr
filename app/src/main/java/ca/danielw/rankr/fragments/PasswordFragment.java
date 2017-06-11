package ca.danielw.rankr.fragments;

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
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateLeagueActivity;
import ca.danielw.rankr.models.UserModel;
import ca.danielw.rankr.utils.Constants;

public class PasswordFragment extends Fragment {
    private static final String TAG = "PasswordFragment";

    private Button nextBtn;
    private EditText etPassword;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.password_fragment, container, false);

        nextBtn = (Button) view.findViewById(R.id.btnNext);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLeagueActivity.mPassword = password;

                //Start the leaderboard activity
                createUser();
            }
        });

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

        return view;
    }

    private void createUser(){
        CreateLeagueActivity.mAuth.createUserWithEmailAndPassword(CreateLeagueActivity.mEmail, CreateLeagueActivity.mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            String leagueKey = CreateLeagueActivity.dbRef.child(Constants.NODE_LEAGUES)
                                    .push().getKey();
                            FirebaseUser user = CreateLeagueActivity.mAuth.getCurrentUser();
                            UserModel userModel = new UserModel(CreateLeagueActivity.mUsername, CreateLeagueActivity.mEmail ,leagueKey);

                            String userId = user.getUid();

                            //Create league
                            CreateLeagueActivity.dbRef.child(Constants.NODE_LEAGUES).child(leagueKey)
                                    .child(Constants.NODE_NAME).setValue(CreateLeagueActivity.mLeagueName);
                            //Add this new user to the league
                            CreateLeagueActivity.dbRef.child(Constants.NODE_LEAGUES).child(leagueKey)
                                    .child(Constants.NODE_MEMBERS).child(userId).setValue(true);

                            //Create user
                            CreateLeagueActivity.dbRef.child(Constants.NODE_USERS)
                                    .child(userId).setValue(userModel);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure" + " " + CreateLeagueActivity.mEmail + " " + CreateLeagueActivity.mPassword, task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
