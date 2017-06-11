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

public class SignInEmailFragment extends Fragment{
    private static final String TAG = "SignInEmailFragment";

    private Button nextBtn;
    private EditText etEmail;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.sign_in_email_fragment, container, false);

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
                SignInActivity.mEmail = email;

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.root_frame, new SignInPasswordFragment());
                transaction.addToBackStack("Sign In Email");
                transaction.commit();
            }
        });

        return view;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
