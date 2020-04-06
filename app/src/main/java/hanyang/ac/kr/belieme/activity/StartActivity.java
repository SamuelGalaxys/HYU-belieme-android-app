package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.dataType.AdminInfo;
import hanyang.ac.kr.belieme.dataType.AdminInfoRequest;
import hanyang.ac.kr.belieme.dataType.UserInfo;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.manager.PreferenceManager;

public class StartActivity extends AppCompatActivity {
    private Context context;
    private Button loginButton;
//    private ConstraintLayout buttonPartLayout;
    private ProgressBar progressBar;
    private boolean onlyResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        context = getApplicationContext();
        onlyResume = false;

        loginButton = findViewById(R.id.start_message_box);
//        buttonPartLayout = findViewById(R.id.start_layout_buttonPart);
        progressBar = findViewById(R.id.start_progressBar);

        if(getIntent().getBooleanExtra("exit", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        GetVersionTask getVersionTask = new GetVersionTask();
        getVersionTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onlyResume == false) {
            onlyResume = true;
        } else {
            GetVersionTask getVersionTask = new GetVersionTask();
            getVersionTask.execute();
        }
    }

    public class GetVersionTask extends AsyncTask<Void, Void, Pair<Double, ArrayList<AdminInfo>>> {

        @Override
        protected Pair<Double, ArrayList<AdminInfo>> doInBackground(Void... voids) {
            try {
                return new Pair(getVersionRequest(), AdminInfoRequest.getList());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InternalServerException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Pair<Double, ArrayList<AdminInfo>> result) {
            if(result == null) {
                new AlertDialog.Builder(StartActivity.this)
                        .setMessage("네트워크 상태가 불안정 합니다. 확인 후 다시 실행시켜 주십시오.")
                        .create()
                        .show();

            } else {
                if(result.first == null || result.second == null) {
                    new AlertDialog.Builder(StartActivity.this)
                            .setMessage("네트워크 상태가 불안정 합니다. 확인 후 다시 실행시켜 주십시오.")
                            .create()
                            .show();
                }
                else if(!result.first.equals(Constants.currentVersion)) {
                    new AlertDialog.Builder(StartActivity.this)
                            .setMessage("현재 앱의 버전은 " + Constants.currentVersion + "입니다. 새로운 버전 " + result.first + "을 다운 받은 뒤 다시 실행시켜 주십시오.")
                            .create()
                            .show();
                } else {
                    Globals.adminInfo = result.second;
                    if(!PreferenceManager.getString(context, "gaeinNo").equals("")) {
                        Globals.userInfo = new UserInfo(
                                PreferenceManager.getString(context, "gaeinNo"),
                                PreferenceManager.getString(context, "userNm"),
                                PreferenceManager.getString(context, "sosokId"),
                                PreferenceManager.getString(context, "userGb"),
                                PreferenceManager.getString(context, "daehakNm"),
                                PreferenceManager.getString(context, "sosokNm"),
                                PreferenceManager.getString(context, "userGbNm"),
                                Globals.getPermission(Integer.parseInt(PreferenceManager.getString(context, "gaeinNo")))
                        );
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        loginButton.setVisibility(View.VISIBLE);
//                        buttonPartLayout.setVisibility(View.VISIBLE);
                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressBar.setVisibility(View.VISIBLE);
                                loginButton.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }
    }

    public double getVersionRequest() throws IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "updateVer");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output += (line + "\n");
            }
            reader.close();
            connection.disconnect();
        }
        return Double.parseDouble(output);
    }
}
