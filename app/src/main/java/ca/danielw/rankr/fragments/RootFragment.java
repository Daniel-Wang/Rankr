package ca.danielw.rankr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.danielw.rankr.R;
import ca.danielw.rankr.utils.Constants;

public class RootFragment extends Fragment{

    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root, container, false);
        String sourceLocation = getArguments().getString(Constants.SOURCE_LOCATION);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		switch(sourceLocation){
            case Constants.CREATE_LEAGUE_FRAGMENT:
                transaction.replace(R.id.root_frame, new EmailFragment());
                break;
            case Constants.SIGNIN_FRAGMENT:
                Bundle bundle1 = new Bundle();
                Fragment fragment1 = new SignInLeagueNameFragment();

                bundle1.putString(Constants.SIGN_IN_INTENT, Constants.SIGNIN_FRAGMENT);
                fragment1.setArguments(bundle1);

                transaction.replace(R.id.root_frame, fragment1);
                break;
            case Constants.HOME_FRAGMENT:

                transaction.replace(R.id.root_frame, new RankingFragment(), Constants.FRAGMENT_RANKING);
                break;
            case Constants.SIGNUP_FRAGMENT:
                Bundle bundle2 = new Bundle();
                Fragment fragment2 = new SignInLeagueNameFragment();

                bundle2.putString(Constants.SIGN_IN_INTENT, Constants.SIGNUP_FRAGMENT);
                fragment2.setArguments(bundle2);

                transaction.replace(R.id.root_frame, fragment2);
                break;

            case Constants.PROFILE_FRAGMENT:
                transaction.replace(R.id.root_frame, new ProfileFragment());
                break;

            default:
                break;
        }

        transaction.commit();

        return view;
    }
}
