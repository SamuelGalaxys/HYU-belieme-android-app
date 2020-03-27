package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.dataType.UserInfo;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.manager.PreferenceManager;

public class StartActivity extends AppCompatActivity {
    private Context context;
    private Button loginButton;
//    private ConstraintLayout buttonPartLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        context = getApplicationContext();

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

    public class GetVersionTask extends AsyncTask<Void, Void, Double> {

        @Override
        protected Double doInBackground(Void... voids) {
            try {
                return getVersionRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            if(aDouble == null) {
                new AlertDialog.Builder(StartActivity.this)
                        .setMessage("네트워크 상태가 불안정 합니다. 확인 후 다시 실행시켜 주십시오.")
                        .create()
                        .show();

            } else {
                if(!aDouble.equals(Constants.currentVersion)) {
                    new AlertDialog.Builder(StartActivity.this)
                            .setMessage("현재 앱의 버전은 " + Constants.currentVersion + "입니다. 새로운 버전 " + aDouble + "을 다운 받은 뒤 다시 실행시켜 주십시오.")
                            .create()
                            .show();
                } else {
                    if(!PreferenceManager.getString(context, "gaeinNo").equals("")) {
                        Globals.userInfo = new UserInfo(
                                PreferenceManager.getString(context, "gaeinNo"),
                                PreferenceManager.getString(context, "userNm"),
                                PreferenceManager.getString(context, "sosokId"),
                                PreferenceManager.getString(context, "userGb"),
                                PreferenceManager.getString(context, "daehakNm"),
                                PreferenceManager.getString(context, "sosokNm"),
                                PreferenceManager.getString(context, "userGbNm")
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
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
