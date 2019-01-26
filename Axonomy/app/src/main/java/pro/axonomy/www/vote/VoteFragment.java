package pro.axonomy.www.vote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */
public class VoteFragment extends Fragment implements BaseSliderView.OnSliderClickListener {

    private SliderLayout mSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vote, container, false);

        generateBanner(view);
        generateVotingRoundsView(view);

        return view;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // TODO if needed
    }

    private void generateBanner(View view) {
        mSlider = view.findViewById(R.id.slider);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://img.aceport.com/4102152238648369.png");
        imageUrls.add("https://img.aceport.com/4720170772930343.png");

        for(String imageUrl : imageUrls){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .image(imageUrl)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            mSlider.addSlider(textSliderView);
        }
    }

    @SuppressLint("ResourceType")
    private void generateVotingRoundsView(View view) {
        try {
            String votingRoundsData = new GetVoteTask().execute().get();
            JSONObject votingRoundsJson = new JSONObject(votingRoundsData);
            JSONObject votingData = (JSONObject) votingRoundsJson.get("data");
            JSONArray votingItems = (JSONArray) votingData.get("items");

            LinearLayout voteContainer = view.findViewById(R.id.vote_container);

            for (int i = 1; i < votingItems.length(); i++)
            {
                JSONObject votingItem = votingItems.getJSONObject(i);
                int sequence = votingItem.getInt("seq");
                JSONObject timelineData = (JSONObject) votingItem.get("timeline_card");
                JSONObject revenueData = (JSONObject) votingItem.get("revenue_card");
                JSONObject voteData = (JSONObject) votingItem.get("vote_card");

                LinearLayout historyRoundLayout = new LinearLayout(getActivity());
                View historyRoundView = view.inflate(getActivity(), R.layout.layout_history_rounds, historyRoundLayout);
                TextView round = historyRoundView.findViewById(R.id.round);
                round.setText("ROUND " + sequence);
                LinearLayout timelineCardLayout = new LinearLayout(getActivity());
                View timelineCardView = view.inflate(getActivity(), R.layout.timeline_card, timelineCardLayout);
                setTimelineCard(timelineCardView, timelineData);
                LinearLayout voteCardLayout = new LinearLayout(getActivity());
                View voteCardView = view.inflate(getActivity(), R.layout.vote_card, voteCardLayout);
                setVoteCard(voteCardView, voteData);
                LinearLayout revenueCardLayout = new LinearLayout(getActivity());
                View revenueCardView = view.inflate(getActivity(), R.layout.revenue_card, revenueCardLayout);
                setRevenueCard(revenueCardView, revenueData);

                LinearLayout historyTimelineCard = historyRoundLayout.findViewById(R.id.timeline_card);
                historyTimelineCard.addView(timelineCardLayout);
                LinearLayout historyVoteCard = historyRoundLayout.findViewById(R.id.vote_card);
                historyVoteCard.addView(voteCardLayout);
                LinearLayout historyRevenueCard = historyRoundLayout.findViewById(R.id.revenue_card);
                historyRevenueCard.addView(revenueCardLayout);

                voteContainer.addView(historyRoundLayout);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTimelineCard(View timelineCardView, JSONObject timelineData) throws JSONException {
        JSONArray stages = timelineData.getJSONArray("stages");
        JSONObject beginStage = (JSONObject) stages.get(0);
        String beginTime = beginStage.getString("begin_time_str");
        JSONObject endStage = (JSONObject) stages.get(1);
        String endTime = endStage.getString("begin_time_str");
        JSONObject rewardStage = (JSONObject) stages.get(2);
        String rewardTime = rewardStage.getString("begin_time_str");

        TextView voteBeginsTime = timelineCardView.findViewById(R.id.vote_begins_time);
        voteBeginsTime.setText(beginTime);
        TextView voteEndsTime = timelineCardView.findViewById(R.id.vote_ends_time);
        voteEndsTime.setText(endTime);
        TextView voteRewardTime = timelineCardView.findViewById(R.id.reward_time);
        voteRewardTime.setText(rewardTime);
    }

    private void setVoteCard(View voteCardView, JSONObject voteData) throws JSONException {
        String title = voteData.getString("title");
        int count = voteData.getInt("votes");
        TextView voteTitle = voteCardView.findViewById(R.id.vote_title);
        voteTitle.setText(title);
        TextView voteCount = voteCardView.findViewById(R.id.vote_count);
        voteCount.setText(count + " Votes");

        JSONArray winners = voteData.getJSONArray("winners");
        TableLayout winnerTableLayout = (TableLayout) voteCardView.findViewById(R.id.table_layout_winner_projects);
        for (int i = 0; i < winners.length(); i++) {
            JSONObject winner = (JSONObject) winners.get(i);
            TableRow tableRow = new TableRow(getActivity());
            View tableRowView = voteCardView.inflate(getActivity(), R.layout.tablerow_winner_project, tableRow);
            TextView winnerTitle = tableRowView.findViewById(R.id.winner_title);
            title = winner.getString("title");
            winnerTitle.setText(title);
            winnerTableLayout.addView(tableRowView);
        }

        JSONArray projects = voteData.getJSONArray("projects");
        TableLayout voteTableLayout = (TableLayout) voteCardView.findViewById(R.id.table_layout_vote_projects);
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = (JSONObject) projects.get(i);
            TableRow tableRow = new TableRow(getActivity());
            View tableRowView = voteCardView.inflate(getActivity(), R.layout.tablerow_voted_project, tableRow);
            TextView index = tableRowView.findViewById(R.id.vote_index);
            index.setText(String.valueOf(i+1));
            TextView projectTitle = tableRowView.findViewById(R.id.vote_project_title);
            title = project.getString("title");
            projectTitle.setText(title);
            TextView projectCount = tableRowView.findViewById(R.id.vote_project_count);
            double votes = project.getDouble("votes");
            projectCount.setText(String.valueOf(votes));
            voteTableLayout.addView(tableRowView);
        }
    }

    private void setRevenueCard(View revenueCardView, JSONObject revenueData) throws JSONException {
        String title = revenueData.getString("title");
        String tips = revenueData.getString("tips");

        TextView revenueTitle = revenueCardView.findViewById(R.id.revenue_title);
        revenueTitle.setText(title);
        TextView revenueTips = revenueCardView.findViewById(R.id.revenue_tip);
        revenueTips.setText(tips);
    }
}
