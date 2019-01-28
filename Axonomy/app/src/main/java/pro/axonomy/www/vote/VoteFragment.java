package pro.axonomy.www.vote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;
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

import java.util.concurrent.ExecutionException;

import pro.axonomy.www.GetHttpUrlRequestTask;
import pro.axonomy.www.LoadImageFromURLTask;
import pro.axonomy.www.R;
import pro.axonomy.www.WebImageHandler;

/**
 * Created by xingyuanding on 1/12/19.
 */
public class VoteFragment extends Fragment implements BaseSliderView.OnSliderClickListener {

    private SliderLayout mSlider;
    private static final String BANNER_URL = "https://wx.aceport.com/public/project/banner/voteslice";
    private static final String VOTE_URL_LOGOUT = "https://wx.aceport.com/api/v1/integration/voting/rounds";
    private static final String DATA = "data";

    private static View view = null;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        WebImageHandler.clearUnfinishedAsyncTaskList();

        view = inflater.inflate(R.layout.fragment_vote, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            generateBanner(view);
            generateVotingRoundsView(view);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // TODO if needed
    }

    private void generateBanner(View view) throws ExecutionException, InterruptedException, JSONException {
        mSlider = view.findViewById(R.id.slider);
        JSONObject response = new JSONObject(new GetHttpUrlRequestTask().execute(BANNER_URL).get());
        JSONArray bannerData = (JSONArray) response.get(DATA);
        Log.i("GenerateBanner", "Received response with data: " + bannerData.toString());

        for(int i=0; i< bannerData.length(); i++) {
            JSONObject banner = bannerData.getJSONObject(i);
            final String imageUrl = banner.get("image_url").toString();
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
            String votingRoundsData = new GetHttpUrlRequestTask().execute(VOTE_URL_LOGOUT).get();
            JSONObject votingRoundsJson = new JSONObject(votingRoundsData);
            JSONObject votingData = (JSONObject) votingRoundsJson.get(DATA);
            JSONArray votingItems = (JSONArray) votingData.get("items");

            JSONObject currentRound = (JSONObject) votingItems.get(0);
            setCurrentRound(view, currentRound);

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

    private void setCurrentRound(View view, JSONObject data) throws JSONException {
        int sequence = data.getInt("seq");
        TextView round = view.findViewById(R.id.current_round);
        round.setText("ROUND " + sequence);

        setCurrentTimeline(view, data);
        setCurrentActivity(view, data);
        setCurrentRevenue(view, data);
        setCurrentBalance(view, data);
    }

    private void setCurrentTimeline(View view, JSONObject data) throws JSONException {
        JSONObject timelineData = data.getJSONObject("timeline_card");
        String timelineType = timelineData.getString("type");

        LinearLayout timelineCard = view.findViewById(R.id.current_timeline_card);
        if (timelineType.equals("comming_soon")) {
            LinearLayout startingSoonLayout = new LinearLayout(getActivity());
            View startingSoonView = view.inflate(getActivity(), R.layout.timeline_starting_soon, startingSoonLayout);
            TextView startingSoonText = startingSoonView.findViewById(R.id.starting_soon_text);
            startingSoonText.setText(timelineData.getString("text"));
            timelineCard.addView(startingSoonView);
        }
    }

    private void setCurrentActivity(View view, JSONObject data) throws JSONException {
        TableLayout activityTable = (TableLayout) view.findViewById(R.id.table_current_activity);
        JSONObject activityData = data.getJSONObject("activity_card");
        TextView activityTitle = view.findViewById(R.id.current_activity_title);
        activityTitle.setText(activityData.getString("title"));
        TextView activityLabel = view.findViewById(R.id.current_activity_label);
        JSONArray labels = activityData.getJSONArray("labels");
        for (int i = 0; i < labels.length(); i++) {
            activityLabel.setText(labels.getJSONObject(i).getString("text"));
        }
        JSONArray activityItems = activityData.getJSONArray("items");
        for (int i = 0; i < activityItems.length(); i++) {
            JSONObject activityItem = (JSONObject) activityItems.get(i);
            TableRow tableRow = new TableRow(getActivity());
            View tableRowView = view.inflate(getActivity(), R.layout.tablerow_current_activity, tableRow);
            TextView activityText = tableRowView.findViewById(R.id.activity_text);
            activityText.setText(activityItem.getString("text"));
            activityTable.addView(tableRowView);
        }
    }

    private void setCurrentRevenue(View view, JSONObject data) throws JSONException {
        JSONObject revenueData = data.getJSONObject("revenue_card");
        TextView revenueTitle = view.findViewById(R.id.current_revenue_title);
        revenueTitle.setText(revenueData.getString("title"));
        TextView revenueTip = view.findViewById(R.id.current_revenue_tip);
        revenueTip.setText(revenueData.getString("tips"));
        TextView revenueItemLeft = view.findViewById(R.id.current_revenue_item_left);
        revenueItemLeft.setText(revenueData.getJSONArray("items").getJSONObject(0).getString("content"));
        TextView revenueItemRight = view.findViewById(R.id.current_revenue_item_right);
        revenueItemRight.setText(revenueData.getJSONArray("items").getJSONObject(1).getString("content"));
        TextView revenueCost = view.findViewById(R.id.current_revenue_cost);
        revenueCost.setText(revenueData.getString("cost"));
    }

    private void setCurrentBalance(View view, JSONObject data) throws JSONException {
        JSONObject balanceData = data.getJSONObject("balance_card");
        TextView balanceTitle = view.findViewById(R.id.current_balance_title);
        balanceTitle.setText(balanceData.getString("title"));
        TextView balanceAmount = view.findViewById(R.id.current_balance_amount);
        balanceAmount.setText(balanceData.getString("balance_str"));
        Button balanceButton = view.findViewById(R.id.current_balance_button);
        balanceButton.setText(balanceData.getString("button_text"));
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

            ImageView winnerImg = tableRowView.findViewById(R.id.winner_img);
            final String logoUrl = winner.getString("logo");
            WebImageHandler.UNFINISHED_ASYNC_TASKS.put(logoUrl, new LoadImageFromURLTask(winnerImg, logoUrl).execute(logoUrl));

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

            ImageView projectImg = tableRowView.findViewById(R.id.vote_image);
            final String logoUrl = project.getString("logo");
            WebImageHandler.UNFINISHED_ASYNC_TASKS.put(logoUrl, new LoadImageFromURLTask(projectImg, logoUrl).execute(logoUrl));

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
