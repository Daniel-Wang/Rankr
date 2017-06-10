package ca.danielw.rankr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateLeagueActivity;
import ca.danielw.rankr.adapters.SlidePagerAdapter;
import ca.danielw.rankr.fragments.EmailFragment;

public class RootFragment extends Fragment{

    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root, container, false);
        String sourceLocation = getArguments().getString(SlidePagerAdapter.PUT_EXTRA_SOURCE_LOCATION);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		if(sourceLocation.equals(CreateLeagueActivity.SOURCE_LOCATION)) {
            transaction.replace(R.id.root_frame, new EmailFragment());
        } else {
            transaction.replace(R.id.root_frame, new SignInLeagueNameFragment());
        }

        transaction.commit();

        return view;
    }
}
