package ca.danielw.rankr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;

public class CreateGameActivity extends AppCompatActivity {

    private Button nextBtn;
    private EditText etNameGame;
    private String mGameName;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        final Activity activity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.purpleDark));
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nextBtn = (Button) findViewById(R.id.btnCreateGame);
        etNameGame = (EditText) findViewById(R.id.etNameGame);

        etNameGame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextBtn.setEnabled(true);

                mGameName = etNameGame.getText().toString();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initiate Creation of game in the db
                //Return to the main activity
                final FirebaseUser user = mAuth.getCurrentUser();

                mDatabase.child(Constants.NODE_USERS).child(user.getUid())
                        .child(Constants.NODE_LEAGUE).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String leagueKey = (String) dataSnapshot.getValue();

                        mDatabase.child(Constants.NODE_LEAGUES).child(leagueKey)
                                .child(Constants.NODE_MEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Map<String, Object> gamesUpdates = new HashMap<>();
                                gamesUpdates.put(Constants.NODE_LEAGUES + "/" + leagueKey + "/" + Constants.NODE_GAMES + "/" + mGameName, true);

                                mDatabase.updateChildren(gamesUpdates);

                                // Add all existing players to the new game with default rankings
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String userId = postSnapshot.getKey();

                                    RankingModel rankingModel = new RankingModel(userId,
                                            Constants.BASE_RATING, (String) postSnapshot.getValue(), 0, Constants.KFACTOR);

                                    Map<String, Object> rankingValues = rankingModel.toMap();
                                    Map<String, Object> childUpdates = new HashMap<>();

                                    childUpdates.put(Constants.NODE_RANKINGS + "/" + leagueKey
                                    + "/" + mGameName + "/" + userId, rankingValues);
                                    childUpdates.put(Constants.NODE_USERS + "/" + userId + "/"
                                    + Constants.NODE_GAMES + "/" + mGameName, Constants.BASE_RATING);

                                    mDatabase.updateChildren(childUpdates);
                                }

                                Intent returnIntent = new Intent();
                                setResult(Constants.RESULT_OK,returnIntent);
                                activity.finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
