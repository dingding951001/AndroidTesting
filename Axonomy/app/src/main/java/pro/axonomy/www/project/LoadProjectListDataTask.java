package pro.axonomy.www.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import pro.axonomy.www.LoadImageFromURLTask;
import pro.axonomy.www.PostHttpsRequestTask;
import pro.axonomy.www.R;
import pro.axonomy.www.WebImageHandler;
import pro.axonomy.www.WebViewActivity;
import pro.axonomy.www.login.LogInTask;

public class LoadProjectListDataTask extends AsyncTask<String, Void, Void> {

    public static final String LATEST = "1";
    public static final String POPULAR = "0";
    public static final String RATING = "3";

    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "page_size";
    private static final String SORT = "sort";


    private static final String PROJECT_URL = "https://wx.aceport.com/public/project/items";
    private static final String MESSAGE = "message";
    private static final String SUCCESS = "OK";
    private static final String DATA = "data";
    private static final String ITEMS = "items";
    private static final String PROJECT_ID = "project_id";
    private static final String PROJECT_LOGO = "logo";
    private static final String PROJECT_DESCRIPTION = "desc";
    private static final String PROJECT_TITLE = "title";
    private static final String PROJECT_DETAIL_URL_PREFIX = "https://www.axonomy.pro/#/project/details?project_id=";

    private JSONObject projectListParams;

    private View view;
    private ProjectFragment projectFragment;
    private ProgressBar mProgressBar;

    public LoadProjectListDataTask(View view, Context context, ProjectFragment fragment) {
        Log.i("LoadProjectList", "in the constructor");
        this.view = view;
        this.projectFragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(String... flag) {
        Log.i("LoadProjectList", "in the function implementation");

        try {
            switch (flag[0]) {
                case POPULAR:
                    Log.i("LoadProjectList", "Load for popular list.");
                    loadPopularProject();
                    break;
                case LATEST:
                    Log.i("LoadProjectList", "Load for latest list.");
                    loadLatestProject();
                    break;
                case RATING:
                    Log.i("LoadProjectList", "Load for rating list.");
                    loadRatingProject();
                    break;
                default:
                    Log.i("LoadProjectList", "Load for default (latest) list.");
                    loadLatestProject();
                    break;
            }
            } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void param) {
        mProgressBar.setVisibility(View.GONE);
        projectFragment.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createProjectListView(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public JSONObject loadLatestProject() throws JSONException, ExecutionException, InterruptedException {
        WebImageHandler.clearUnfinishedAsyncTaskList();

        final JSONObject latestRequest = new JSONObject() {{
            put(PAGE, 1);
            put(PAGE_SIZE, 9999);
            put(SORT, 1);
        }};

        Log.i("LoadProjectList", "start retrieving data...");
        final String result = getProjectListDate(latestRequest.toString());
        Log.i("LoadProjectList", "Project List data: " + result);
        projectListParams = new JSONObject(result);

        return new JSONObject(result);
    }

    private JSONObject loadPopularProject() throws JSONException, ExecutionException, InterruptedException {
        WebImageHandler.clearUnfinishedAsyncTaskList();

        final JSONObject popularRequest = new JSONObject() {{
            put(PAGE, 1);
            put(PAGE_SIZE, 9999);
            put(SORT, 0);
        }};

        final String result = getProjectListDate(popularRequest.toString());
        Log.i("LoadProjectList", "Project List data: " + result);
        projectListParams = new JSONObject(result);

        return new JSONObject(result);
    }


    private JSONObject loadRatingProject() throws JSONException, ExecutionException, InterruptedException {
        WebImageHandler.clearUnfinishedAsyncTaskList();

        final JSONObject popularRequest = new JSONObject() {{
            put(PAGE, 1);
            put(PAGE_SIZE, 9999);
            put(SORT, 3);
        }};

        final String result = getProjectListDate(popularRequest.toString());
        Log.i("LoadProjectList", "Project List data: " + result);
        projectListParams = new JSONObject(result);

        return new JSONObject(result);
    }

    @SuppressLint("ResourceAsColor")
    private void changeProjectTextViewDisplay(View view) {
        View rootView = view.getRootView();
        TextView latestProjects = (TextView) rootView.findViewById(R.id.latestProjects);
        latestProjects.setBackgroundDrawable(null);
        latestProjects.setTextColor(projectFragment.getResources().getColor(R.color.colorNavProjectUnclicked));
        TextView popularProjects = (TextView) rootView.findViewById(R.id.popularProjects);
        popularProjects.setBackgroundDrawable(null);
        popularProjects.setTextColor(projectFragment.getResources().getColor(R.color.colorNavProjectUnclicked));
        TextView ratingProjects = (TextView) rootView.findViewById(R.id.ratingProjects);
        ratingProjects.setBackgroundDrawable(null);
        ratingProjects.setTextColor(projectFragment.getResources().getColor(R.color.colorNavProjectUnclicked));
        view.setBackgroundResource(R.drawable.light_purple_rounded_layout);
        ((TextView) view).setTextColor(projectFragment.getResources().getColor(R.color.colorNavClicked));
    }

    protected void createProjectListView(View view) throws JSONException {
        changeProjectTextViewDisplay(view);
        View rootView = view.getRootView();
        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout_project);
        tableLayout.removeAllViews();

        JSONArray projects = (JSONArray) ((JSONObject)projectListParams.get(DATA)).get(ITEMS);
        for (int i = 0; i < projects.length(); i++) {
            final String projectName = ((JSONObject) projects.get(i)).get(PROJECT_TITLE).toString();
            final String projectDescription = ((JSONObject) projects.get(i)).get(PROJECT_DESCRIPTION).toString();
            final String projectLogoUrl = ((JSONObject) projects.get(i)).get(PROJECT_LOGO).toString();
            final String projectId = ((JSONObject) projects.get(i)).get(PROJECT_ID).toString();

            TableRow tableRow = new TableRow(projectFragment.getContext());

            View tableRowView = view.inflate(projectFragment.getContext(), R.layout.tablerow_project, tableRow);
            tableRowView.setTag(projectId);
            tableRowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("CreateWebViewForProject", "in the onClick function");
                    createWebViewWithProjectId(view);
                }
            });

            TextView rowName = (TextView) tableRowView.findViewById(R.id.rowName);
            rowName.setText(projectName);
            Log.i("test", rowName.getParent().getClass().toString());
            TextView rowDescription = (TextView) tableRowView.findViewById(R.id.rowDescription);
            rowDescription.setText(projectDescription);
            ImageView rowImage = (ImageView) tableRowView.findViewById(R.id.rowImage);

            WebImageHandler.UNFINISHED_ASYNC_TASKS.put(projectLogoUrl, new LoadImageFromURLTask(rowImage, projectLogoUrl).execute(projectLogoUrl));
            tableLayout.addView(tableRowView);

        }
    }

    @SuppressLint("ResourceType")
    public void createWebViewWithProjectId(View view) {
        final String projectId = view.getTag().toString();
        Log.i("CreateWebViewForProject" , projectId);

        final String projectDetailURL = PROJECT_DETAIL_URL_PREFIX + projectId;
        Intent projectDetailIntent = new Intent(projectFragment.getContext(), WebViewActivity.class);
        projectDetailIntent.putExtra(WebViewActivity.URL_PARAM, projectDetailURL);
        projectFragment.startActivity(projectDetailIntent);
    }

    private String getProjectListDate(String request) {

        Log.i("LoadProjectList", "in the GetDataTask");
        SSLContext sc;
        HttpsURLConnection connection = null;

        // set up SSLContext and HttpsURLConnection
        try {
            URL url = new URL(PROJECT_URL);

            connection = (HttpsURLConnection) url.openConnection();
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());

            // set Timeout and method
            connection.setReadTimeout(LogInTask.LOGIN_TIMEOUT);
            connection.setConnectTimeout(LogInTask.LOGIN_TIMEOUT);
            connection.setRequestMethod(LogInTask.POST);
            connection.setDoInput(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // send the request body to Https
        PostHttpsRequestTask.sendPostRequestBodyToHttpsConnection(connection, request);

        // validate the username and password for login
        String result = null;
        JSONObject response = null;
        final int status;
        try {
            status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    result = LogInTask.extractResponse(connection);
                    Log.i("LoadProjectList", result);
            }

            if (result != null) {
                response = new JSONObject(result);
            }
            if (response != null && (response.get(MESSAGE).equals(SUCCESS))) {
                Log.i("LoadProjectList", "SUCCEED in retrieving project lists with request: " + request +
                        " and response: " + response.toString());
            } else if (!response.get(MESSAGE).equals(SUCCESS)) {
                Log.i("LoadProjectList", "FAILED in retrieving project lists with request: " + request +
                        " and response: " + response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}
