package pro.axonomy.www;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import pro.axonomy.www.me.MeFragment;
import pro.axonomy.www.project.ProjectFragment;
import pro.axonomy.www.vote.VoteFragment;

public class BottomNavigationActivity extends AppCompatActivity {

    private static String fragmentTag;
    private String FRAGMENT_TAG_KEY = "fragment-tag";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_vote:
                    fragmentTag = "navigation_vote";
                    VoteFragment voteFragment = new VoteFragment();
                    openFragment(voteFragment);
                    return true;
                case R.id.navigation_project:
                    fragmentTag = "navigation_project";
                    ProjectFragment projectFragment = new ProjectFragment();
                    openFragment(projectFragment);
                    return true;
                case R.id.navigation_me:
                    fragmentTag = "navigation_me";
                    MeFragment meFragment = new MeFragment();
                    openFragment(meFragment);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bottom_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null || savedInstanceState.getString(FRAGMENT_TAG_KEY) == null) {
            View view = navigation.findViewById(R.id.navigation_vote);
            view.performClick();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_TAG_KEY, fragmentTag);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
