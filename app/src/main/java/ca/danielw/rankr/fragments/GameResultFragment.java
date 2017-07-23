package ca.danielw.rankr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.Random;

import ca.danielw.rankr.R;
import ca.danielw.rankr.utils.Constants;

public class GameResultFragment extends Fragment {
    private static String ORIGNAL_ELO = "ORIGNAL_ELO";
    private static String NEW_ELO = "NEW_ELO";

    private LinearLayout mBackground;
    private TextView mQuote;
    private Context mContext;

    public static Fragment newInstance(int origElo, int newElo, boolean isWin) {
        Fragment fragment = new GameResultFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.GAME, isWin);
        bundle.putInt(ORIGNAL_ELO, origElo);
        bundle.putInt(NEW_ELO,  newElo);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.game_result_fragment, container, false);
        Bundle bundle = getArguments();
        boolean isWin = bundle.getBoolean(Constants.GAME);
        int origElo = bundle.getInt(ORIGNAL_ELO);
        int newElo = bundle.getInt(NEW_ELO);

        mContext = getContext();

        mBackground = (LinearLayout) view.findViewById(R.id.llBackground);
        mQuote = (TextView) view.findViewById(R.id.tvQuote);
        final TickerView tickerView = (TickerView) view.findViewById(R.id.tkElo);

        tickerView.setCharacterList(TickerUtils.getDefaultNumberList());
        tickerView.setText(String.valueOf(origElo));

        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                Activity activity = getActivity();

                activity.setResult(Constants.RESULT_OK, returnIntent);
                activity.finish();
            }
        });

        Random rand = new Random();

        int quoteSelection = rand.nextInt(Constants.NUM_QUOTES + 1);

        if(isWin) {
            mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
            mQuote.setText(getResources()
                    .getStringArray(R.array.victory_quotes)[quoteSelection]);
            mQuote.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
        } else {
            mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            mQuote.setText(getResources()
                    .getStringArray(R.array.defeat_quotes)[quoteSelection]);
            mQuote.setTextColor(ContextCompat.getColor(mContext, R.color.red_dark));
        }

        tickerView.setText(String.valueOf(newElo));

        return view;
    }

}