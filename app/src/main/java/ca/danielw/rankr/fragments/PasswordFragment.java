package ca.danielw.rankr.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateLeagueActivity;
import ca.danielw.rankr.activities.InviteActivity;
import ca.danielw.rankr.activities.SignUpActivity;
import ca.danielw.rankr.models.UserModel;
import ca.danielw.rankr.utils.Constants;

public class PasswordFragment extends Fragment {
    private static final String TAG = "PasswordFragment";

    private Button nextBtn;
    private EditText etPassword;
    private String password;
    private String userId;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String INTENT = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.password_fragment, container, false);

        Bundle args = getArguments();
        if (args != null) {
            INTENT = (String) args.get(Constants.SIGN_IN_INTENT);
        }

        mAuth = FirebaseAuth.getInstance();

        nextBtn = (Button) view.findViewById(R.id.btnNext);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLeagueActivity.mPassword = password;

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

                if (INTENT != null) {
                    SignUpActivity.mPassword = password;
                    Log.e("Congrats", "We made it!" + SignUpActivity.mUsername + " " + SignUpActivity.mLeagueName + " "
                            + SignUpActivity.mEmail + " " + SignUpActivity.mPassword);

                    createUser();

                } else {
                    transaction.replace(R.id.root_frame, new VerifyEmailFragment());
                }
                transaction.addToBackStack("Password");
                transaction.commit();
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

    private void createUser() {
        mAuth.createUserWithEmailAndPassword(SignUpActivity.mEmail, SignUpActivity.mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signUpUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            mDatabase = FirebaseDatabase.getInstance().getReference();

                            if (user != null) {
                                addPreferences();

                                userId = user.getUid();
                                UserModel userModel = new UserModel(SignUpActivity.mUsername, SignUpActivity.mEmail,
                                        SignUpActivity.mLeagueName);
//                                LeagueModel leagueModel = new LeagueModel(Collections.singletonList(userId));

                                Map<String, Object> userValues = userModel.toMap();
//                                Map<String, Object> leagueValues = leagueModel.toMap();

                                Log.e(TAG, userId + " " + SignUpActivity.mLeagueName);

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(Constants.NODE_USERS + "/" + userId, userValues);
                                childUpdates.put(Constants.NODE_LEAGUES + "/" + SignUpActivity.mLeagueName + "/"
                                        + "/" + Constants.NODE_MEMBERS + "/" + userId, userModel.getUsername());

                                // Add default ranking to all league
//                                childUpdates.put(Constants.NODE_RANKINGS + "/" + SignUpActivity.mLeagueName
//                                        + "/" + name + "/" + userId, rankingValues);

                                mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Log.e(TAG, "onComplete: success");
                                        } else {
                                            Log.e(TAG, "onComplete: fail", databaseError.toException());
                                        }
                                    }
                                });

                                Log.e("hello", "asd");

                                // Start the leaderboard activity
                                Intent intent = new Intent(getActivity(), InviteActivity.class);
                                intent.putExtra(Constants.LEAGUE_NAME, SignUpActivity.mLeagueName);
                                intent.putExtra(Constants.EMAIL, SignUpActivity.mEmail);

                                startActivity(intent);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure" + " " + SignUpActivity.mEmail + " " + SignUpActivity.mPassword, task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addPreferences(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.LEAGUE_NAME, SignUpActivity.mLeagueName);
        Log.e("Verify", SignUpActivity.mLeagueName);
        editor.commit();
    }
}
