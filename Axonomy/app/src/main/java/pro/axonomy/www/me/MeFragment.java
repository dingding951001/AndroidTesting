package pro.axonomy.www.me;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import pro.axonomy.www.GetHttpUrlRequestTask;
import pro.axonomy.www.LoadImageFromURLTask;
import pro.axonomy.www.R;
import pro.axonomy.www.WebImageHandler;

/**
 * Created by xingyuanding on 1/12/19.
 */

public class MeFragment extends Fragment {

    private static final String IS_FACE_ID_URL = "https://wx.aceport.com/api/v1/user/isfaceId";
    private static final String DBA_MENU_URL = "https://wx.aceport.com/public/user/tasks";


    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_me, container, false);

        //TODO: change the View Parameter for the constructor
        AsyncTask<String, String, String> faceIdTask = new GetHttpUrlRequestTask(getContext(), this.getView()) {
            @Override
            public void onPostExecute(String result) {
                //TODO: need to render the LinearLayout for top pop-up
                Log.i("MeFragment:FaceIdData", result);
            }
        };

        AsyncTask<String, String, String> menuTask = new GetHttpUrlRequestTask(getContext(), view, inflater) {
            @Override
            protected void onPreExecute() {
                this.progressBar = (ProgressBar) view.getRootView().findViewById(R.id.progress_bar);
                this.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(String result) {
                Log.i("MeFragment:MenuData", "Received the data for DBA User Menu: " + result);
                this.progressBar.setVisibility(View.GONE);
                try {
                    final JSONObject menu = new JSONObject(result);
                    final JSONArray menuData = menu.getJSONArray("data");

                    final TableLayout table = this.parentView.findViewById(R.id.user_task_table);
                    for (int i = 0; i < menuData.length(); i += 3) {
                        if (i != 0) {
                            TableRow separatorRow = (TableRow) this.inflater.inflate(R.layout.tablerow_separator, null, false);
                            table.addView(separatorRow);
                        }
                        TableRow currentRow = (TableRow) this.inflater.inflate(R.layout.tablerow_user_tasks, null, false);
                        for (int j = 0; j < 3 && i + j < menuData.length(); j++) {
                            final String imageUrl = ((JSONObject) menuData.get(i + j)).getString("icon");
                            final String title = ((JSONObject) menuData.get(i + j)).getString("title");
                            TextView menuText;
                            ImageView menuImg;
                            View separator;
                            switch (j) {
                                case 0:
                                    menuText = currentRow.findViewById(R.id.user_task_txt_0);
                                    menuText.setText(title);
                                    menuImg = currentRow.findViewById(R.id.user_task_img_0);
                                    WebImageHandler.UNFINISHED_ASYNC_TASKS.put(imageUrl, new LoadImageFromURLTask(menuImg, imageUrl).execute(imageUrl));
                                    separator = currentRow.findViewById(R.id.separator_0);
                                    separator.setBackgroundColor(Color.parseColor("#EFEFEF"));
                                    break;
                                case 1:
                                    menuText = currentRow.findViewById(R.id.user_task_txt_1);
                                    menuText.setText(title);
                                    menuImg = currentRow.findViewById(R.id.user_task_img_1);
                                    WebImageHandler.UNFINISHED_ASYNC_TASKS.put(imageUrl, new LoadImageFromURLTask(menuImg, imageUrl).execute(imageUrl));
                                    separator = currentRow.findViewById(R.id.separator_1);
                                    separator.setBackgroundColor(Color.parseColor("#EFEFEF"));
                                    break;
                                default:
                                    menuText = currentRow.findViewById(R.id.user_task_txt_2);
                                    menuText.setText(title);
                                    menuImg = currentRow.findViewById(R.id.user_task_img_2);
                                    WebImageHandler.UNFINISHED_ASYNC_TASKS.put(imageUrl, new LoadImageFromURLTask(menuImg, imageUrl).execute(imageUrl));
                            }
                        }
                        table.addView(currentRow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };


        faceIdTask.execute(IS_FACE_ID_URL);
        menuTask.execute(DBA_MENU_URL);

        return view;
    }
}
