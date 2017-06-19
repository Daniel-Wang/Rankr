package ca.danielw.rankr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ca.danielw.rankr.R;
import ca.danielw.rankr.models.RankingModel;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvRank;
        public TextView tvUsername;
        public TextView tvElo;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvRank = (TextView) itemView.findViewById(R.id.tvRank);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvElo = (TextView) itemView.findViewById(R.id.tvElo);

        }
    }

    private List<RankingModel> mRankings;
    private Context mContext;

    public RankingAdapter(Context context, List<RankingModel> rankingModel){
        mRankings = rankingModel;
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
        RankingModel ranking = mRankings.get(position);

        // Set item views based on your views and data model
        TextView rank = holder.tvRank;
        rank.setText(ranking.getRank());

        TextView username = holder.tvUsername;
        username.setText(ranking.getUsername());

        TextView elo = holder.tvElo;
        elo.setText(ranking.getElo());
    }

    @Override
    public int getItemCount() {
        return mRankings.size();
    }

}
