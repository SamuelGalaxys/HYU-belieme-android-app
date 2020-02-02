package hanyang.ac.kr.belieme.DataType;

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

public class UserInfoRequest {
    public static UserInfo getUserInfo(String accessToken) throws IOException, JSONException {
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
        JSONObject outputJason = new JSONObject(output);
        outputJason = outputJason.getJSONObject("response");
        outputJason = outputJason.getJSONObject("item");
        UserInfo result = new UserInfo(
                outputJason.getString("gaeinNo"),
                outputJason.getString("userNm"),
                outputJason.getString("sosokId"),
                outputJason.getString("userGb")
        );

        Log.d("니 이름", outputJason.getString("userNm"));

        in.close();
        is.close();
        connection.disconnect();

        return result;
    }
}
