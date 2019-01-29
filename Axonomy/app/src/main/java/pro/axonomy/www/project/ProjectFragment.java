package pro.axonomy.www.project;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pro.axonomy.www.R;
import pro.axonomy.www.WebImageHandler;

public class ProjectFragment extends Fragment {

    private static View projectFragmentView = null;
    private Context _context = getContext();
    private ProjectFragment _fragment = this;

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
                new LoadProjectListDataTask(v, _context, _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.LATEST);
            }
        });

        TextView popularProjects = (TextView) projectFragmentView.findViewById(R.id.popularProjects);
        popularProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadProjectListDataTask(v, _context, _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.POPULAR);
            }
        });

        TextView ratingProjects = (TextView) projectFragmentView.findViewById(R.id.ratingProjects);
        ratingProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadProjectListDataTask(v, _context, _fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.RATING);
            }

        });

        new LoadProjectListDataTask(latestProjects, this.getContext(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LoadProjectListDataTask.LATEST);

        return projectFragmentView;
    }
}
