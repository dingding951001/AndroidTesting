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

public class ButtomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_vote:
                    VoteFragment voteFragment = new VoteFragment();
                    openFragment(voteFragment);
                    return true;
                case R.id.navigation_project:
                    ProjectFragment projectFragment = new ProjectFragment();
                    openFragment(projectFragment);
                    return true;
                case R.id.navigation_me:
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
        setContentView(R.layout.layout_buttom_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View view = navigation.findViewById(R.id.navigation_vote);
        view.performClick();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
