package pro.axonomy.www.project;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import pro.axonomy.www.LoadImageFromURLTask;
import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */

public class ProjectFragment extends Fragment {

    private static View projectFragmentView = null;

    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "page_size";
    private static final String SORT = "sort";

    private static final String DATA = "data";
    private static final String ITEMS = "items";
    private static final String PROJECT_ID = "project_id";
    private static final String PROJECT_LOGO = "logo";
    private static final String PROJECT_DESCRIPTION = "desc";
    private static final String PROJECT_TITLE = "title";

    private JSONObject projectListParams;
    private HashMap<String, AsyncTask> unfinishedAsyncTasks = new HashMap<>();
    private HashMap<String, Bitmap> projectImage = new HashMap<>();

    public void addCacheForProjectImage(String url, Bitmap bitmap) {
        projectImage.put(url, bitmap);
    }

    public Bitmap retrieveBitmapForURL(String url) {
        return projectImage.get(url);
    }

    public void removeFromUnfinishedAsyncTaskList(String key) {
        unfinishedAsyncTasks.remove(key);
    }

    protected void clearUnfinishedAsyncTaskList() {
        Log.i("ClearAsyncTask", "Clearing Unfinished AsyncTasks...");
        while (!unfinishedAsyncTasks.isEmpty()) {
            Iterator it = unfinishedAsyncTasks.entrySet().iterator();
            Map.Entry pair = (Map.Entry)it.next();
            AsyncTask task = (AsyncTask) pair.getValue();
            task.cancel(true);
            unfinishedAsyncTasks.remove(pair.getKey());
        }
        Log.i("ClearAsyncTask", "Clearance finished.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        if (projectFragmentView != null) {
            return projectFragmentView;
        }

        projectFragmentView = inflater.inflate(R.layout.fragment_project,container,false);

        TextView latestProjects = (TextView) projectFragmentView.findViewById(R.id.latestProjects);

        latestProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadLatestProject(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView popularProjects = (TextView) projectFragmentView.findViewById(R.id.popularProjects);
        popularProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadPopularProject(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView ratingProjects = (TextView) projectFragmentView.findViewById(R.id.ratingProjects);
        ratingProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadRatingProject(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            loadLatestProject(latestProjects);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return projectFragmentView;
    }

    @Override
    public void onCreate(Bundle bundle) {
        setRetainInstance(true);
        super.onCreate(bundle);
    }

    private void loadLatestProject(View view) throws JSONException, ExecutionException, InterruptedException {
        clearUnfinishedAsyncTaskList();

        final JSONObject latestRequest = new JSONObject() {{
            put(PAGE, 1);
            put(PAGE_SIZE, 9999);
            put(SORT, 1);
        }};

        final String result = new GetProjectListTask().execute(latestRequest.toString()).get();
        Log.i("ProjectFragment", "Project List data: " + result);
        projectListParams = new JSONObject(result);
        createProjectListView(view);
    }

    private void loadPopularProject(View view) throws JSONException, ExecutionException, InterruptedException {
        clearUnfinishedAsyncTaskList();

        final JSONObject popularRequest = new JSONObject() {{
            put(PAGE, 1);
            put(PAGE_SIZE, 9999);
            put(SORT, 0);
        }};

        final String result = new GetProjectListTask().execute(popularRequest.toString()).get();
        Log.i("ProjectFragment", "Project List data: " + result);
        projectListParams = new JSONObject(result);
        createProjectListView(view);
    }


    private void loadRatingProject(View view) throws JSONException, ExecutionException, InterruptedException {
        clearUnfinishedAsyncTaskList();

        final JSONObject popularRequest = new JSONObject() {{
            put(PAGE, 1);
            put(PAGE_SIZE, 9999);
            put(SORT, 3);
        }};

        final String result = new GetProjectListTask().execute(popularRequest.toString()).get();
        Log.i("ProjectFragment", "Project List data: " + result);
        projectListParams = new JSONObject(result);
        createProjectListView(view);
    }

    @SuppressLint("ResourceAsColor")
    private void changeProjectTextViewDisplay(View view) {
        View rootView = view.getRootView();
        TextView latestProjects = (TextView) rootView.findViewById(R.id.latestProjects);
        latestProjects.setBackgroundDrawable(null);
        latestProjects.setTextColor(getResources().getColor(R.color.colorNavProjectUnclicked));
        TextView popularProjects = (TextView) rootView.findViewById(R.id.popularProjects);
        popularProjects.setBackgroundDrawable(null);
        popularProjects.setTextColor(getResources().getColor(R.color.colorNavProjectUnclicked));
        TextView ratingProjects = (TextView) rootView.findViewById(R.id.ratingProjects);
        ratingProjects.setBackgroundDrawable(null);
        ratingProjects.setTextColor(getResources().getColor(R.color.colorNavProjectUnclicked));
        view.setBackgroundResource(R.drawable.light_purple_rounded_layout);
        ((TextView) view).setTextColor(getResources().getColor(R.color.colorNavClicked));
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

            TableRow tableRow = new TableRow(getActivity());

            View tableRowView = view.inflate(getActivity(), R.layout.tablerow_project, tableRow);
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

            unfinishedAsyncTasks.put(projectLogoUrl, new LoadImageFromURLTask(rowImage, projectLogoUrl,this).execute(projectLogoUrl));
            tableLayout.addView(tableRowView);

        }
    }

    public static void createWebViewWithProjectId(View view) {
        final String projectId = view.getTag().toString();
        Log.i("CreateWebViewForProject" , projectId);
    }
}
