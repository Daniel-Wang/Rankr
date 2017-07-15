package ca.danielw.rankr.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.utils.Constants;

public class ProfileFragment extends Fragment {

    private TextView mUsername;
    private TextView mLeagueName;
    private TextView mEmail;
    private TextView mNoGames;

    private LinearLayout mGamesLayout;

    private DatabaseReference mDatabase;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mEmail = (TextView) view.findViewById(R.id.tvEmail);
        mGamesLayout = (LinearLayout) view.findViewById(R.id.llGames);
        mUsername = (TextView) view.findViewById(R.id.tvUsername);
        mLeagueName = (TextView) view.findViewById(R.id.tvLeagueName);
        mNoGames = (TextView) view.findViewById(R.id.tvNoGames);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(Constants.NODE_USERS).child(MainActivity.mCurrentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mUsername.setText(dataSnapshot.child(Constants.NODE_USERNAME).getValue().toString());
                        mLeagueName.setText(dataSnapshot.child(Constants.NODE_LEAGUE).getValue().toString());
                        mEmail.setText(dataSnapshot.child(Constants.NODE_EMAIL).getValue().toString());

                        // Create views for each game and elo pair
                        if (dataSnapshot.child(Constants.NODE_GAMES).exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.child(Constants.NODE_GAMES).getChildren()) {
                                Activity activity = getActivity();

                                if (activity != null) {
                                    TextView tvGameName = (TextView) activity.getLayoutInflater().inflate(R.layout.cardview_subheader_template, null);
                                    TextView tvGameElo = (TextView) activity.getLayoutInflater().inflate(R.layout.cardview_detail_template, null);

                                    tvGameName.setText(snapshot.getKey());
                                    tvGameElo.setText(String.valueOf(snapshot.getValue()));

                                    mGamesLayout.addView(tvGameName);
                                    mGamesLayout.addView(tvGameElo);
                                }
                            }
                        } else {
                            mNoGames.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }
}