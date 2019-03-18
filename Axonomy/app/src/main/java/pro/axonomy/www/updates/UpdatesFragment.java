package pro.axonomy.www.updates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pro.axonomy.www.GetHttpUrlRequestTask;
import pro.axonomy.www.LoadImageFromURLTask;
import pro.axonomy.www.R;
import pro.axonomy.www.WebImageHandler;
import pro.axonomy.www.me.user.UserActivity;
import pro.axonomy.www.utils.observableScrollView.ObservableScrollView;
import pro.axonomy.www.utils.observableScrollView.ScrollViewListener;

/**
 * Created by xingyuanding on 3/2/19.
 */

public class UpdatesFragment extends Fragment implements ScrollViewListener {

    private int updateCount;
    private int page;
    private ObservableScrollView scrollView;
    private static final int pageSize = 10;
    private static final String TREND_URL = "https://wx.aceport.com/api/v1/trends?page=%d&page_size=%d&sticked=%d";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_updates, container, false);
        scrollView = view.findViewById(R.id.update_scroll_view);
        scrollView.setScrollViewListener(this);

        page = 0;

        final String stickedUrl = String.format(TREND_URL, page, pageSize, 1);
        final String nonStickedUrl = String.format(TREND_URL, page, pageSize, 0);

        final GetHttpUrlRequestTask stickedDataAsyncTask = new GetHttpUrlRequestTask(getContext(), view) {
            @Override
            public void onPostExecute(final String result) {
                try {
                    final JSONObject stickedData = new JSONObject(result);
                    populateStickedView(view, stickedData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        final GetHttpUrlRequestTask nonstickedDataAsyncTask = new GetHttpUrlRequestTask(getContext(), view) {
            @Override
            protected void onPreExecute() {
                this.progressBar = view.getRootView().findViewById(R.id.progress_bar);
                this.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(final String result) {
                this.progressBar.setVisibility(View.GONE);
                try {
                    final JSONObject nonstickedData = new JSONObject(result);
                    populateNonStickedView(view, nonstickedData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        stickedDataAsyncTask.execute(stickedUrl);
        nonstickedDataAsyncTask.execute(nonStickedUrl);

        return view;
    }

    private void populateStickedView(View view, JSONObject stickedTrendData) throws JSONException {
        final TableLayout table = view.findViewById(R.id.sticked_update_table);
        JSONArray itemList = stickedTrendData.getJSONObject("data").getJSONArray("items");
        for (int i = 0; i < itemList.length(); i++) {
            JSONObject item = itemList.getJSONObject(i);
            TableRow tableRow = new TableRow(getActivity());
            View row_view = view.inflate(getActivity(), R.layout.tablerow_sticked_update, tableRow);
            TextView stickedUpdateText = row_view.findViewById(R.id.sticked_update_text);
            stickedUpdateText.setText(item.getString("title"));
            table.addView(row_view);
        }
    }

    private void populateNonStickedView(View view, JSONObject nonStickedTrendData) throws JSONException {
        updateCount = nonStickedTrendData.getJSONObject("data").getInt("total_count");

        final LinearLayout updatesLayout = view.findViewById(R.id.nonsticked_update_layout);
        JSONArray itemList = nonStickedTrendData.getJSONObject("data").getJSONArray("items");
        for (int i = 0; i < itemList.length(); i++) {
            JSONObject item = itemList.getJSONObject(i);

            LinearLayout updateLayout = new LinearLayout(getActivity());
            View updateView = view.inflate(getActivity(), R.layout.layout_update, updateLayout);

            TextView updateAuthor = updateView.findViewById(R.id.update_author);
            JSONObject author = item.getJSONObject("author");
            int isOfficial = author.getInt("is_official");
            String updateAuthorImgUrl;
            if (isOfficial == 0) {
                updateAuthor.setText(author.getString("name"));
                updateAuthorImgUrl = author.getString("avatar");
            } else {
                JSONObject project = item.getJSONObject("project");
                updateAuthor.setText(project.getString("name"));
                updateAuthorImgUrl = project.getString("logo");
            }
            ImageView updateAuthorImg = updateView.findViewById(R.id.update_author_img);
            WebImageHandler.UNFINISHED_ASYNC_TASKS.put(updateAuthorImgUrl, new LoadImageFromURLTask(updateAuthorImg, updateAuthorImgUrl).execute(updateAuthorImgUrl));

            TextView updateTime = updateView.findViewById(R.id.update_time);
            updateTime.setText(item.getString("create_at_str"));

            LinearLayout updateSubjectlayout = updateView.findViewById(R.id.update_subject);
            JSONObject updateSubject = item.getJSONObject("subject");
            if (updateSubject.length() == 0) {
                updateSubjectlayout.setVisibility(View.GONE);
            } else {
                TextView updateSubjectTitle = updateView.findViewById(R.id.update_subject_title);
                updateSubjectTitle.setText(updateSubject.getString("title"));
            }

            RatingBar ratingBar = updateView.findViewById(R.id.update_ratingBar);
            int rating = item.getInt("rating");
            if (rating == -1) {
                ratingBar.setVisibility(View.GONE);
            } else {
                ratingBar.setRating((float) (rating / 2.0));
            }

            TextView updateContent = updateView.findViewById(R.id.update_content);
            updateContent.setText(item.getString("content"));

            TextView updateLikedUsers = updateView.findViewById(R.id.update_liked_users);
            String users = "";
            JSONObject likes = item.getJSONObject("likes");
            JSONArray usersArray = likes.getJSONArray("items");
            List<Integer> userNameLength = new ArrayList();
            for (int j = 0; j < usersArray.length(); j++) {
                JSONObject user = usersArray.getJSONObject(j);
                if (j != 0) {
                    users += ", ";
                }
                String userName = user.getString("user_name");
                users += userName;
                userNameLength.add(userName.length());
            }
            int likesCount = likes.getInt("total_count");
            if (likesCount > usersArray.length()) {
                users += " and " + likesCount + " others liked";
            }
            SpannableString likesSpannableString = new SpannableString(users);
            int start = 0;
            for (Integer length: userNameLength) {
                likesSpannableString.setSpan(new MyClickableSpan(), start, start+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                start += length + 2;
            }
            updateLikedUsers.setText(likesSpannableString);
            updateLikedUsers.setMovementMethod(LinkMovementMethod.getInstance());

            TableLayout updateCommentTable = updateView.findViewById(R.id.update_comment_table);
            JSONObject comments = item.getJSONObject("comments");
            JSONArray userComments = comments.getJSONArray("items");
            for (int j = 0; j < userComments.length(); j++) {
                JSONObject userComment = userComments.getJSONObject(j);
                TableRow updateCommentRow = new TableRow(getActivity());
                View updateCommentView = view.inflate(getActivity(), R.layout.tablerow_update_comment, updateCommentRow);
                TextView updateCommentContent = updateCommentView.findViewById(R.id.update_comment_content);
                String userName = userComment.getJSONObject("author").getString("name");
                String commentContent = userComment.getString("content");
                String userCommentString = userName + ": " + commentContent;
                SpannableString commentSpannableString = new SpannableString(userCommentString);
                commentSpannableString.setSpan(new MyClickableSpan(), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                updateCommentContent.setText(commentSpannableString);
                updateCommentContent.setMovementMethod(LinkMovementMethod.getInstance());
                updateCommentTable.addView(updateCommentView);
            }
            int commentsCount = comments.getInt("total_count");
            if (commentsCount > userComments.length()) {
                TableRow updateCommentRow = new TableRow(getActivity());
                View updateCommentView = view.inflate(getActivity(), R.layout.tablerow_update_comment_more, updateCommentRow);
                updateCommentTable.addView(updateCommentView);
            }

            updatesLayout.addView(updateView);
        }
    }

    @Override
    public void onScrollEnded(final ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.d("onScrollEnded", "onScrollEnded");
        int numItems = (page + 1) * pageSize;
        if (numItems < updateCount) {
            final String nonStickedUrl = String.format(TREND_URL, page + 1, pageSize, 0);

            final GetHttpUrlRequestTask loadMoreAsyncTask = new GetHttpUrlRequestTask(getContext(), scrollView) {
                @Override
                protected void onPreExecute() {
                    this.progressBar = scrollView.getRootView().findViewById(R.id.progress_bar_bottom);
                    this.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPostExecute(final String result) {
                    this.progressBar.setVisibility(View.GONE);
                    try {
                        final JSONObject nonstickedData = new JSONObject(result);
                        populateNonStickedView(scrollView, nonstickedData);
                        page += 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            loadMoreAsyncTask.execute(nonStickedUrl);
        }
    }

    class MyClickableSpan extends ClickableSpan {
        public void onClick(View textView) {
            Log.i("onClick","MyClickableSpan on click");
            Intent userIntent = new Intent(getContext(), UserActivity.class);
            startActivity(userIntent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.colorLinkText));
            ds.setUnderlineText(false);
        }
    }
}
