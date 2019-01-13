package pro.axonomy.www.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */

public class VoteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vote, container, false);
    }

}
