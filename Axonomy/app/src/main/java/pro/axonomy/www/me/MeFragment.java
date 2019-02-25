package pro.axonomy.www.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_me, container, false);
        try {
            String isFaceIdData = new GetHttpUrlRequestTask(getContext()).execute(IS_FACE_ID_URL).get();
            Log.i("isFaceIdData", isFaceIdData);

            final JSONObject response = new JSONObject(new GetHttpUrlRequestTask(getContext()).execute(DBA_MENU_URL).get());
            Log.i("UserFragmentView", "Received the data for DBA User Menu: " + response);

            String jsonString = "{\"code\":200,\"message\":\"success\",\"data\":[{\"icon\":\"https://img.aceport.com/28216316377933626.png\",\"title\":\"邀请好友\",\"path\":\"#/invite\",\"android_path\":null,\"ios_path\":\"jump://DBMineInviteViewController\",\"wx_path\":\"/pages/invite/main\",\"paras\":\"\",\"desc\":\"\"},{\"icon\":\"https://img.aceport.com/6736554685113474.png\",\"title\":\"推荐项目\",\"path\":\"https://mp.weixin.qq.com/s/GtzrC0GiQJ-5GWmR9Z0xnQ\",\"android_path\":null,\"ios_path\":\"jump://DBMainWebViewController?urlString=https://mp.weixin.qq.com/s/GtzrC0GiQJ-5GWmR9Z0xnQ&title=Axonomy\",\"wx_path\":null,\"paras\":\"\",\"desc\":\"推荐或录入项目，可获得DBA代币奖励\"},{\"icon\":\"https://img.aceport.com/28216316377933626.png\",\"title\":\"邀请好友\",\"path\":\"#/invite\",\"android_path\":null,\"ios_path\":\"jump://DBMineInviteViewController\",\"wx_path\":\"/pages/invite/main\",\"paras\":\"\",\"desc\":\"\"},{\"icon\":\"https://img.aceport.com/6736554685113474.png\",\"title\":\"推荐项目\",\"path\":\"https://mp.weixin.qq.com/s/GtzrC0GiQJ-5GWmR9Z0xnQ\",\"android_path\":null,\"ios_path\":\"jump://DBMainWebViewController?urlString=https://mp.weixin.qq.com/s/GtzrC0GiQJ-5GWmR9Z0xnQ&title=Axonomy\",\"wx_path\":null,\"paras\":\"\",\"desc\":\"推荐或录入项目，可获得DBA代币奖励\"}]}";
            JSONObject test = new JSONObject(jsonString);

            populateUserTask(test, view, inflater);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("UserFragmentView", "Error when getting the DBA Menu data: " + e.getMessage());
            e.printStackTrace();
        }

        return view;
    }

    private void populateUserTask(final JSONObject menuResponse, final View view, LayoutInflater inflater) throws JSONException {
        final JSONArray menuData = menuResponse.getJSONArray("data");
        final TableLayout table = view.findViewById(R.id.user_task_table);
        for (int i=0; i<menuData.length(); i+=3) {
            if (i != 0) {
                TableRow separatorRow = (TableRow) inflater.inflate(R.layout.tablerow_separator, null, false);
                table.addView(separatorRow);
            }
            TableRow currentRow = (TableRow) inflater.inflate(R.layout.tablerow_user_tasks, null, false);
            for (int j=0; j<3 && i+j<menuData.length(); j++) {
                final String imageUrl = ((JSONObject) menuData.get(i+j)).getString("icon");
                final String title = ((JSONObject) menuData.get(i+j)).getString("title");
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
    }

}
