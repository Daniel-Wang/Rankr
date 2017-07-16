package ca.danielw.rankr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateLeagueActivity;
import ca.danielw.rankr.activities.InviteActivity;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.UserModel;
import ca.danielw.rankr.utils.Constants;


public class VerifyEmailFragment extends Fragment {
    private static final String TAG = "VerifyEmailFragment";

    private Button nextBtn;
    private String userId;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.verify_email_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nextBtn = (Button) view.findViewById(R.id.btnNext);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Refresh to see if the user has been validated

                mAuth.signOut();
                signIn(CreateLeagueActivity.mEmail, CreateLeagueActivity.mPassword);
            }
        });

        if (!CreateLeagueActivity.isVerified) {
            createUser();
        } else {
            nextBtn.setEnabled(true);
        }
        return view;
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            //Create the user in the database

                            if(user != null && user.isEmailVerified()) {
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(Constants.LEAGUE_NAME, CreateLeagueActivity.mLeagueName);
                                editor.apply();

                                UserModel userModel = new UserModel(CreateLeagueActivity.mUsername, CreateLeagueActivity.mEmail,
                                        CreateLeagueActivity.mLeagueName);

                                Map<String, Object> userValues = userModel.toMap();

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(Constants.NODE_USERS + "/" + userId, userValues);
                                childUpdates.put(Constants.NODE_LEAGUES + "/" + CreateLeagueActivity.mLeagueName + "/"
                                        + "/" + Constants.NODE_MEMBERS + "/" + userId, userModel.getUsername());

                                mDatabase.updateChildren(childUpdates).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Update failed", e.getMessage());
                                        Toast.makeText(getActivity(), "That league name is already used.",
                                                Toast.LENGTH_LONG).show();
                                        CreateLeagueActivity.isVerified = true;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Start the invite activity
                                        Activity activity = getActivity();
                                        Intent intent = new Intent(activity, InviteActivity.class);
                                        intent.putExtra(Constants.LEAGUE_NAME, CreateLeagueActivity.mLeagueName);
                                        intent.putExtra(Constants.EMAIL, CreateLeagueActivity.mEmail);

                                        startActivity(intent);
                                        activity.finish();
                                    }
                                });
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                        }
                    }
                });
    }

    private void createUser(){
        mAuth.createUserWithEmailAndPassword(CreateLeagueActivity.mEmail, CreateLeagueActivity.mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            sendVerifyEmail(user);
                            nextBtn.setEnabled(true);
                            userId = user.getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure" + " " + CreateLeagueActivity.mEmail + " " + CreateLeagueActivity.mPassword, task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerifyEmail(FirebaseUser user){
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getActivity(), "Email has been sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}