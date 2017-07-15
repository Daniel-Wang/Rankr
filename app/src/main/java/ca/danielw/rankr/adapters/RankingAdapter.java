package ca.danielw.rankr.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.danielw.rankr.R;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView mRank;
        private TextView mUsername;
        private TextView mElo;
        private TextView mRankDiff;
        private View mNoRankChange;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            mRankDiff = (TextView) itemView.findViewById(R.id.tvRankDiff);
            mNoRankChange = itemView.findViewById(R.id.horizontal_divider);
            mRank = (TextView) itemView.findViewById(R.id.tvRank);
            mUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            mElo = (TextView) itemView.findViewById(R.id.tvElo);

        }
    }

    private LeagueModel mLeagueModel = new LeagueModel();
    private Context mContext;

    public RankingAdapter(Context context, LeagueModel leagueModel){
        mLeagueModel = leagueModel;
        mContext = context;
    }

    private Context getContent(){
        return mContext;
    }

    @Override
    public RankingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rankingView = inflater.inflate(R.layout.item_ranking, parent, false);

        return new ViewHolder(rankingView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RankingModel ranking = mLeagueModel.getmRankings().get(position);

        // Set item views based on your views and data model
        TextView tvrank = holder.mRank;
        TextView tvRankDiff = holder.mRankDiff;

        View noRankChange = holder.mNoRankChange;

        int rank = ranking.getRank();
        int rankDiff = rank - ranking.getPrevRank();
        String rankDiffString = "";

        if(rankDiff > 0) {
            rankDiffString = rankDiffString.concat("+" + String.valueOf(rankDiff));
            tvRankDiff.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            tvRankDiff.setVisibility(View.VISIBLE);
        } else if (rankDiff < 0) {
            rankDiffString = rankDiffString.concat(String.valueOf(rankDiff));
            tvRankDiff.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            tvRankDiff.setVisibility(View.VISIBLE);
        } else {
            noRankChange.setVisibility(View.VISIBLE);
        }

        tvrank.setText(String.valueOf(rank));
        tvRankDiff.setText(rankDiffString);

        TextView username = holder.mUsername;
        username.setText(ranking.getUsername());

        TextView elo = holder.mElo;
        elo.setText(String.valueOf(ranking.getElo()));
    }

    @Override
    public int getItemCount() {
        return mLeagueModel.getmRankings().size();
    }

}
