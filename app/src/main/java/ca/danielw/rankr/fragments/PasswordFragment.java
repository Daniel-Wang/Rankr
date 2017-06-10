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
}
