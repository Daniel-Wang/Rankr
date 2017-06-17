package ca.danielw.rankr.fragments;

import android.content.Intent;
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
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.UserModel;
import ca.danielw.rankr.utils.Constants;


public class VerifyEmailFragment extends Fragment {
    private static final String TAG = "VerifyEmailFragment";

    private Button nextBtn;
    private String leagueKey;
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

//                FirebaseUser user = mAuth.getCurrentUser();
//
//                //Create the user in the database
//                Log.e(TAG, String.valueOf(user.isEmailVerified()));
//
//                if(user != null && user.isEmailVerified()) {
//                    UserModel userModel = new UserModel(CreateLeagueActivity.mUsername, CreateLeagueActivity.mEmail, leagueKey);
//                    LeagueModel leagueModel = new LeagueModel(CreateLeagueActivity.mLeagueName,
//                            Collections.singletonList(userId));
//
//                    Map<String, Object> userValues = userModel.toMap();
//
//                    Map<String, Object> childUpdates = new HashMap<>();
//                    childUpdates.put(Constants.NODE_USERS + "/" + userId, userValues);
//                    childUpdates.put(Constants.NODE_LEAGUES + "/" + leagueKey, leagueModel);
//
//                    mDatabase.updateChildren(childUpdates);
//
////                    mDatabase.child(Constants.NODE_LEAGUES).child(leagueKey)
////                                    .child(Constants.NODE_NAME).setValue(CreateLeagueActivity.mLeagueName);
////                    //Add this new user to the league
////                            mDatabase.child(Constants.NODE_LEAGUES).child(leagueKey)
////                                    .child(Constants.NODE_MEMBERS).child(userId).setValue(true);
////
////                    //Create user
////                            mDatabase.child(Constants.NODE_USERS)
////                                    .child(userId).setValue(userModel);
//
////                    Start the leaderboard activity
//                  Intent intent = new Intent(getActivity(), MainActivity.class);
//                  startActivity(intent);
//                }
            }
        });

        createUser();

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
//                            Log.e(TAG, String.valueOf(user.isEmailVerified()));

                            if(user != null && user.isEmailVerified()) {
                                UserModel userModel = new UserModel(CreateLeagueActivity.mUsername, CreateLeagueActivity.mEmail, leagueKey);
                                LeagueModel leagueModel = new LeagueModel(CreateLeagueActivity.mLeagueName,
                                        Collections.singletonList(userId));

                                Map<String, Object> userValues = userModel.toMap();
                                Map<String, Object> leagueValues = leagueModel.toMap();

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(Constants.NODE_USERS + "/" + userId, userValues);
                                childUpdates.put(Constants.NODE_LEAGUES + "/" + leagueKey, leagueValues);
//                                childUpdates.put(Constants.NODE_RANKINGS + "/" + leagueKey, "hello");
                                mDatabase.child(Constants.NODE_RANKINGS).child(leagueKey).setValue(null);

                                mDatabase.updateChildren(childUpdates);

//                    mDatabase.child(Constants.NODE_LEAGUES).child(leagueKey)
//                                    .child(Constants.NODE_NAME).setValue(CreateLeagueActivity.mLeagueName);
//                    //Add this new user to the league
//                            mDatabase.child(Constants.NODE_LEAGUES).child(leagueKey)
//                                    .child(Constants.NODE_MEMBERS).child(userId).setValue(true);
//
//                    //Create user
//                            mDatabase.child(Constants.NODE_USERS)
//                                    .child(userId).setValue(userModel);

//                    Start the leaderboard activity
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
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

                            leagueKey = mDatabase.child(Constants.NODE_LEAGUES)
                                    .push().getKey();

                            sendVerifyEmail(user);
                            nextBtn.setEnabled(true);
                            userId = user.getUid();

                            //Create league
//                            CreateLeagueActivity.dbRef.child(Constants.NODE_LEAGUES).child(leagueKey)
//                                    .child(Constants.NODE_NAME).setValue(CreateLeagueActivity.mLeagueName);
                            //Add this new user to the league
//                            CreateLeagueActivity.dbRef.child(Constants.NODE_LEAGUES).child(leagueKey)
//                                    .child(Constants.NODE_MEMBERS).child(userId).setValue(true);

                            //Create user
//                            CreateLeagueActivity.dbRef.child(Constants.NODE_USERS)
//                                    .child(userId).setValue(userModel);
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
                        }
                    }
                });
    }
}
