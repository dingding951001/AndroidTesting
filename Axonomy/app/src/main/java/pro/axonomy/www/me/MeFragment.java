package pro.axonomy.www.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutionException;

import pro.axonomy.www.GetHttpUrlRequestTask;
import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */

public class MeFragment extends Fragment {

    private static final String IS_FACE_ID_URL = "https://wx.aceport.com/api/v1/user/isfaceId";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            String isFaceIdData = new GetHttpUrlRequestTask(getContext()).execute(IS_FACE_ID_URL).get();
            Log.i("isFaceIdData", isFaceIdData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_me, container, false);
    }

}
