package pro.axonomy.www.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */

public class ProjectFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project,container,false);
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.table_layout_project);
        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(getActivity());
            View tableRowView = view.inflate(getActivity(), R.layout.tablerow_project, tableRow);
            tableLayout.addView(tableRowView);
        }
        return view;
    }

}
