package ca.danielw.rankr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateLeagueActivity;
import ca.danielw.rankr.activities.SignUpActivity;
import ca.danielw.rankr.utils.Constants;


public class UsernameFragment extends Fragment{
    private static final String TAG = "UsernameFragment";

    private Button nextBtn;
    private EditText etUsername;
    private String username;

    private String INTENT = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.username_fragment, container, false);

        Bundle args = getArguments();
        if(args != null){
            INTENT = (String) args.get(Constants.SIGN_IN_INTENT);
        }

        nextBtn = (Button) view.findViewById(R.id.btnNext);
        etUsername = (EditText) view.findViewById(R.id.etUsername);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

                if(INTENT != null){
                    SignUpActivity.mUsername = username;
                    Bundle bundle = new Bundle();
                    Fragment fragment = new EmailFragment();

                    bundle.putString(Constants.SIGN_IN_INTENT, Constants.SIGNUP_FRAGMENT);
                    fragment.setArguments(bundle);

                    transaction.replace(R.id.root_frame, fragment);

                } else {
                    CreateLeagueActivity.mUsername = username;
                    transaction.replace(R.id.root_frame, new PasswordFragment());

                }

                transaction.addToBackStack("Username");
                transaction.commit();
            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextBtn.setEnabled(true);

                username = etUsername.getText().toString();
            }
        });

        return view;
    }
}
