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


public class EmailFragment extends Fragment {

    private static final String TAG = "EmailFragment";

    private Button nextBtn;
    private EditText etEmail;
    private String email;

    private String INTENT = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.email_fragment, container, false);

        Bundle args = getArguments();
        if(args != null){
            INTENT = (String) args.get(Constants.SIGN_IN_INTENT);
        }

        nextBtn = (Button) view.findViewById(R.id.btnNext);
        etEmail = (EditText) view.findViewById(R.id.etEmail);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = etEmail.getText().toString();
                if(isValidEmail(email)){
                    nextBtn.setEnabled(true);
                } else {
                    nextBtn.setEnabled(false);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

                if(INTENT != null){
                    SignUpActivity.mEmail = email;
                    Bundle bundle = new Bundle();
                    Fragment fragment = new PasswordFragment();

                    bundle.putString(Constants.SIGN_IN_INTENT, Constants.SIGNUP_FRAGMENT);
                    fragment.setArguments(bundle);

                    transaction.replace(R.id.root_frame, fragment);
                } else {
                    CreateLeagueActivity.mEmail = email;
                    transaction.replace(R.id.root_frame, new LeagueNameFragment());
                }

                transaction.addToBackStack("Email");
                transaction.commit();
            }
        });

        return view;
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
