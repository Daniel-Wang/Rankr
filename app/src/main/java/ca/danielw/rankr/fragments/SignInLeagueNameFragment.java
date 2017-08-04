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
import ca.danielw.rankr.activities.SignInActivity;
import ca.danielw.rankr.activities.SignUpActivity;
import ca.danielw.rankr.utils.Constants;

public class SignInLeagueNameFragment extends Fragment {
    private String INTENT = null;

    private Button nextBtn;
    private EditText etLeagueName;
    private String leagueName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.sign_in_league_name_fragment, container, false);

        Bundle args = getArguments();
        if(args != null){
            INTENT = (String) args.get(Constants.SIGN_IN_INTENT);
        }
        nextBtn = (Button) view.findViewById(R.id.btnNext);
        etLeagueName = (EditText) view.findViewById(R.id.etLeagueName);

        if(SignUpActivity.mLeagueName != null && !SignUpActivity.mLeagueName.isEmpty()) {
            leagueName = SignUpActivity.mLeagueName;
            etLeagueName.setText(SignUpActivity.mLeagueName);
            nextBtn.setEnabled(true);
        }

        etLeagueName.requestFocus();

        etLeagueName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                leagueName = etLeagueName.getText().toString();

                if (leagueName.isEmpty()) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

                if(INTENT.equals(Constants.SIGNIN_FRAGMENT)){
                    SignInActivity.mLeagueName = leagueName;
                    Bundle bundle = new Bundle();
                    Fragment fragment = new SignInEmailFragment();

                    bundle.putString(Constants.SIGN_IN_INTENT, Constants.SIGNIN_FRAGMENT);
                    fragment.setArguments(bundle);

                    transaction.replace(R.id.root_frame, fragment);
                } else {
                    SignUpActivity.mLeagueName = leagueName;
                    Bundle bundle = new Bundle();
                    Fragment fragment = new UsernameFragment();

                    bundle.putString(Constants.SIGN_IN_INTENT, Constants.SIGNUP_FRAGMENT);
                    fragment.setArguments(bundle);

                    transaction.replace(R.id.root_frame, fragment);
                }

                transaction.addToBackStack("Sign In League Name");
                transaction.commit();
            }
        });

        return view;
    }
}
