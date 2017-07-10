package ca.danielw.rankr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ca.danielw.rankr.R;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import rx.Observable;
import rx.subjects.PublishSubject;

public class UsernameAdapter extends RecyclerView.Adapter<UsernameAdapter.ViewHolder>{

    private final PublishSubject<RankingModel> onClickSubject = PublishSubject.create();

    private LeagueModel mLeagueModel;
    private Context mContext;
    private ArrayList<RankingModel> mUsersList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public TextView mElo;

        public ViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.tvName);
            mElo = (TextView) itemView.findViewById(R.id.tvElo);
        }
    }

    public UsernameAdapter(Context context, LeagueModel leagueModel){
        mLeagueModel = leagueModel;
        mContext = context;
        mUsersList = leagueModel.getmRankings();
    }

    @Override
    public UsernameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View usernameView = inflater.inflate(R.layout.item_username_list, parent, false);

        // Return a new holder instance
        return new ViewHolder(usernameView);
    }

    @Override
    public void onBindViewHolder(UsernameAdapter.ViewHolder holder, int position) {
        final RankingModel rankingModel = mUsersList.get(position);

        String name = rankingModel.getUsername();

        TextView mName = holder.mName;
        mName.setText(name);

        TextView mElo = holder.mElo;
        mElo.setText(String.valueOf(rankingModel.getElo()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(rankingModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public Observable<RankingModel> getPositionClicks(){
        return onClickSubject.asObservable();
    }
}
