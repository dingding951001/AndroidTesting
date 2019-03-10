package pro.axonomy.www.project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pro.axonomy.www.R;
import pro.axonomy.www.WebImageHandler;

public class ProjectFragment extends Fragment {

    private static View projectFragmentView = null;
    private ProjectFragment _fragment = this;

    private static boolean JUMPED_OUT_FRAGMENT = false;
    public static ProjectType CURRENT_ACTIVE_TAB = ProjectType.LATEST;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        WebImageHandler.clearUnfinishedAsyncTaskList();

        if (projectFragmentView != null) {
            return projectFragmentView;
        }

        projectFragmentView = inflater.inflate(R.layout.fragment_project,container,false);

        TextView latestProjects = (TextView) projectFragmentView.findViewById(R.id.latestProjects);
        latestProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadProjectListDataTask(v, getContext(), _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.LATEST);
            }
        });

        TextView popularProjects = (TextView) projectFragmentView.findViewById(R.id.popularProjects);
        popularProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadProjectListDataTask(v, getContext(), _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.POPULAR);
            }
        });

        TextView ratingProjects = (TextView) projectFragmentView.findViewById(R.id.ratingProjects);
        ratingProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadProjectListDataTask(v, getContext(), _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.RATING);
            }

        });

        new LoadProjectListDataTask(latestProjects, this.getContext(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.LATEST);

        return projectFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (JUMPED_OUT_FRAGMENT) {
            Log.i("ProjectFragmentResume", "Resume the AsyncTasks for " + CURRENT_ACTIVE_TAB.name() + " view.");
            switch(CURRENT_ACTIVE_TAB) {
                case LATEST:
                    TextView latestProjects = (TextView) projectFragmentView.findViewById(R.id.latestProjects);
                    new LoadProjectListDataTask(latestProjects, this.getContext(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.LATEST);
                case POPULAR:
                    TextView popularProjects = (TextView) projectFragmentView.findViewById(R.id.popularProjects);
                    new LoadProjectListDataTask(popularProjects, getContext(), _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.POPULAR);
                case RATING:
                    TextView ratingProjects = (TextView) projectFragmentView.findViewById(R.id.ratingProjects);
                    new LoadProjectListDataTask(ratingProjects, getContext(), _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.RATING);
            }

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        JUMPED_OUT_FRAGMENT = true;
        WebImageHandler.clearUnfinishedAsyncTaskList();
    }
}
