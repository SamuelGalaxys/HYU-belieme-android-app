package hanyang.ac.kr.belieme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.DataType.UserInfo;
import hanyang.ac.kr.belieme.DataType.UserInfoRequest;

public class LoginActivity extends AppCompatActivity {

    private WebView webView;
    private EditText id;
    private EditText password;
    private Button button;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        webView = findViewById(R.id.login_webView);
        id = findViewById(R.id.login_editText_id);
        password = findViewById(R.id.login_editText_password);
        button = findViewById(R.id.login_btn_login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript("$('#uid').val('" + id.getText().toString() + "');\n" +
                        "$('#upw').val('" + password.getText().toString() + "');\n" +
                        "$('#login_btn').click();", null);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("startedUrl", url);
                if(url.indexOf(Constants.redirectUrl + "#access_token=") == 0) {
                    accessToken = url.substring(Constants.redirectUrl.length() + "#access_token=".length(), url.indexOf('&'));
                    Log.d("accessToken", accessToken);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("finishedUrl", url);
                if(url.equals(Constants.loginUrl)) {
                    id.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
                else {
                    id.setVisibility(View.INVISIBLE);
                    password.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                }
                if(url.equals(Constants.redirectUrl + "index.page")) {
                    NetworkTask networkTask = new NetworkTask();
                    networkTask.execute(accessToken);
                }
                if(url.indexOf(Constants._5ErrorRedirectUrl) != -1) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 5회 이상 잘못 입력하였습니다. 포탈에서 본인 인증을 통해 비밀번호를 변경하여야 로그인 가능합니다", Toast.LENGTH_LONG).show();
                    webView.loadUrl(Constants.getAuthorizeUrl);
                }
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(Constants.getAuthorizeUrl);



    }

    private class NetworkTask extends AsyncTask<String, Void, UserInfo> {

        @Override
        protected UserInfo doInBackground(String... strings) {
            try {
                return UserInfoRequest.getUserInfo(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserInfo userInfo) {
            super.onPostExecute(userInfo);
            if(userInfo != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Globals.userInfo = userInfo;
                startActivity(intent);
                finish();
            }
        }
    }
}
