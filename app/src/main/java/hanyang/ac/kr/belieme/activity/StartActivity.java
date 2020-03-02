package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import hanyang.ac.kr.belieme.dataType.UserInfo;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.manager.PreferenceManager;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(getIntent().getBooleanExtra("exit", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if(!PreferenceManager.getString(getApplicationContext(), "gaeinNo").equals("")) {
            Globals.userInfo = new UserInfo(
                    PreferenceManager.getString(this, "gaeinNo"),
                    PreferenceManager.getString(this, "userNm"),
                    PreferenceManager.getString(this, "sosokId"),
                    PreferenceManager.getString(this, "userGb"),
                    PreferenceManager.getString(this, "daehakNm"),
                    PreferenceManager.getString(this, "sosokNm"),
                    PreferenceManager.getString(this, "userGbNm")
            );
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        ImageView loginButton = findViewById(R.id.login_message_box);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
