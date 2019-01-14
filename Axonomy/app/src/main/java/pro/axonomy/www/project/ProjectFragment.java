package pro.axonomy.www.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */

public class ProjectFragment extends Fragment {

    private static View projectFragmentView = null;

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
                loadLatestProject(v);
            }
        });

        TextView popularProjects = (TextView) projectFragmentView.findViewById(R.id.popularProjects);
        popularProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPopularProject(v);
            }
        });

        TextView ratingProjects = (TextView) projectFragmentView.findViewById(R.id.ratingProjects);
        ratingProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRatingProject(v);
            }
        });

        loadLatestProject(latestProjects);
        return projectFragmentView;
    }

    @Override
    public void onCreate(Bundle bundle) {
        setRetainInstance(true);
        super.onCreate(bundle);
    }

    private void loadLatestProject(View view) {
        changeProjectTextViewDisplay(view);
        View rootView = view.getRootView();
        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout_project);
        tableLayout.removeAllViews();
        // TODO loadLatestProject
        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(getActivity());
            View tableRowView = view.inflate(getActivity(), R.layout.tablerow_project, tableRow);
            tableLayout.addView(tableRowView);
        }
    }

    private void loadPopularProject(View view) {
        changeProjectTextViewDisplay(view);
        View rootView = view.getRootView();
        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout_project);
        tableLayout.removeAllViews();
        // TODO loadPopularProject
    }

    private void loadRatingProject(View view) {
        changeProjectTextViewDisplay(view);
        View rootView = view.getRootView();
        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout_project);
        tableLayout.removeAllViews();
        // TODO loadRatingProject
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
}
