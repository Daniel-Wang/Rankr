package ca.danielw.rankr.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.IntroActivity;
import ca.danielw.rankr.activities.InviteActivity;
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.utils.Constants;

public class SettingsFragment extends Fragment{

    private TextView mSignOut;
    private TextView mInviteMembers;
    private TextView mDevWebsite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mSignOut = (TextView) view.findViewById(R.id.tvSignOut);
        mInviteMembers = (TextView) view.findViewById(R.id.tvInvite);
        mDevWebsite = (TextView) view.findViewById(R.id.tvDevWebsite);

        mInviteMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                intent.putExtra(Constants.LEAGUE_NAME, MainActivity.mLeagueName);
                intent.putExtra(Constants.EMAIL, MainActivity.mEmail);
                startActivity(intent);
            }
        });

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                //Show dialog
                Activity activity = getActivity();
                if(activity != null) {
                    addPreferences();
                    Intent intent = new Intent(activity, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    activity.finish();
                }
            }
        });

        mDevWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.danielw.ca/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
            }
        });

        return view;
    }

    private void addPreferences(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.LEAGUE_NAME, null);
        editor.apply();
    }
}
