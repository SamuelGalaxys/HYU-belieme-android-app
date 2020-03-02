package hanyang.ac.kr.belieme.dataType;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.manager.PreferenceManager;

public class UserInfoRequest {
    Context context;

    public UserInfoRequest(Context context) {
        this.context = context;
    }

    public UserInfo getUserInfo(String accessToken) throws IOException, JSONException {
        String output = "";
        String line;

        URL login_url = new URL(Constants.getUserInfoUrl);
        HttpURLConnection connection = (HttpURLConnection) login_url.openConnection();

        connection.setRequestProperty("client_id", Constants.clientId);
        connection.setRequestProperty("access_token", accessToken);
        connection.setRequestProperty("swap_key", String.valueOf(System.currentTimeMillis() / 1000L));

        Log.d("swap_key", String.valueOf(System.currentTimeMillis() / 1000L));

        connection.connect();

        InputStream is = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        while (true) {
            line = in.readLine();
            if (line == null) break;
            output += line;
        }

        Log.d("jsonOutput", output);

        JSONObject outputJason = new JSONObject(output);
        outputJason = outputJason.getJSONObject("response");
        outputJason = outputJason.getJSONObject("item");
        UserInfo result = new UserInfo(
                outputJason.getString("gaeinNo"),
                outputJason.getString("userNm"),
                outputJason.getString("sosokId"),
                outputJason.getString("userGb"),
                outputJason.getString("daehakNm"),
                outputJason.getString("sosokNm"),
                outputJason.getString("userGbNm")
        );

        PreferenceManager.setString(context, "gaeinNo", outputJason.getString("gaeinNo"));
        PreferenceManager.setString(context, "userNm", outputJason.getString("userNm"));
        PreferenceManager.setString(context, "sosokId", outputJason.getString("sosokId"));
        PreferenceManager.setString(context, "userGb", outputJason.getString("userGb"));
        PreferenceManager.setString(context, "daehakNm", outputJason.getString("daehakNm"));
        PreferenceManager.setString(context, "sosokNm", outputJason.getString("sosokNm"));
        PreferenceManager.setString(context, "userGbNm", outputJason.getString("userGbNm"));


        Log.d("니 이름", outputJason.getString("userNm"));

        in.close();
        is.close();
        connection.disconnect();

        return result;
    }
}
