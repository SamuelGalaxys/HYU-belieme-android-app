package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.dataType.UserInfo;
import hanyang.ac.kr.belieme.dataType.UserInfoRequest;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private WebView webView;

    private ProgressBar progressBar; //쓸까 말까

    private ConstraintLayout loginPart;

    private EditText id;
    private EditText password;
    private Button loginButton;

    private ConstraintLayout privacyNoticePart;

    private CheckBox agreeCheckBox;
    private CheckBox disagreeCheckBox;
    private Button noticeLoginButton;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        webView = findViewById(R.id.login_webView);
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearMatches();
        webView.clearSslPreferences();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(LoginActivity.this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }else {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.d("onReceiveValue", value.toString());
                }
            });
            cookieManager.getInstance().flush();
        }

        progressBar = findViewById(R.id.login_progressBar);

        loginPart = findViewById(R.id.login_layout_login);
        id = findViewById(R.id.login_editText_id);
        password = findViewById(R.id.login_editText_password);
        loginButton = findViewById(R.id.login_button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript("$('#uid').val('" + id.getText().toString() + "');\n" +
                        "$('#upw').val('" + password.getText().toString() + "');\n" +
                        "$('#login_btn').click();", null);
            }
        });

        privacyNoticePart = findViewById(R.id.login_layout_privacyNotice);
        agreeCheckBox = findViewById(R.id.login_checkBox_agree);
        disagreeCheckBox = findViewById(R.id.login_checkBox_disagree);
        noticeLoginButton = findViewById(R.id.login_button_noticeLogin);

        agreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    disagreeCheckBox.setChecked(false);
                }
            }
        });

        disagreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    agreeCheckBox.setChecked(false);
                }
            }
        });

        noticeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript("$('input:checkbox[name=chkItem]').prop('checked', " + agreeCheckBox.isChecked() + ");\n" +
                        "$('#btnAgree').click();", null);
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
                    privacyNoticePart.setVisibility(View.GONE);
                    loginPart.setVisibility(View.VISIBLE);
                }
                else {
                    loginPart.setVisibility(View.GONE);
                }

                if(url.indexOf(Constants.privacyNotice) != -1) {
                    loginPart.setVisibility(View.GONE);
                    privacyNoticePart.setVisibility(View.VISIBLE);
                } else {
                    privacyNoticePart.setVisibility(View.GONE);
                }

                if(url.equals(Constants.redirectUrl + "index.page")) {
                    NetworkTask networkTask = new NetworkTask();
                    networkTask.execute(accessToken);
                }

                if(url.indexOf(Constants._5ErrorRedirectUrl) != -1) {
                    webView.loadUrl(Constants.getAuthorizeUrl);
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(Constants.getAuthorizeUrl);
    }

    private class NetworkTask extends AsyncTask<String, Void, UserInfo> {

        @Override
        protected UserInfo doInBackground(String... strings) {
            publishProgress();
            try {
                UserInfoRequest userInfoRequest = new UserInfoRequest(context);
                return userInfoRequest.getUserInfo(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            loginPart.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(UserInfo userInfo) {
            super.onPostExecute(userInfo);
            if(userInfo != null) {
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                Globals.userInfo = userInfo;
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("exit", true);
                startActivity(intent);
                finish();
            } else {
                loginPart.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "네트워크 문제로 인해 로그인에 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
