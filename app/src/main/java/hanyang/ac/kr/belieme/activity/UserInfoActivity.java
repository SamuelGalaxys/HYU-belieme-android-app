package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;
import hanyang.ac.kr.belieme.manager.PreferenceManager;

public class UserInfoActivity extends AppCompatActivity {
    TextView title;
    Button logoutBtn;
    RecyclerView recyclerView;

    InfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        title = findViewById(R.id.info_textView_title);
        title.setText("개인정보");

        logoutBtn = findViewById(R.id.info_btn);
        logoutBtn.setText("로그아웃");
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setString(getApplicationContext(), "gaeinNo", "");
                PreferenceManager.setString(getApplicationContext(), "userNm", "");
                PreferenceManager.setString(getApplicationContext(), "sosokId", "");
                PreferenceManager.setString(getApplicationContext(), "userGb", "");
                PreferenceManager.setString(getApplicationContext(), "daehakNm", "");
                PreferenceManager.setString(getApplicationContext(), "sosokNm","");
                PreferenceManager.setString(getApplicationContext(), "userGbNm", "");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("logout", true);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.info_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new InfoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("이름", Globals.userInfo.getName()));
        list.add(new Pair<>("학번", Globals.userInfo.getStudentId()));
        list.add(new Pair<>("단과대학", Globals.userInfo.getDaehakName()));
        list.add(new Pair<>("학과", Globals.userInfo.getMajorName()));
        list.add(new Pair<>("재학상태", Globals.userInfo.getStatus()));

        adapter.update(list);
    }
}
