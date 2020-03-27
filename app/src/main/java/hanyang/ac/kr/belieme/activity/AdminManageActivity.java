package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;

public class AdminManageActivity extends AppCompatActivity {
    private TextView title;
    private ImageView btn;
    private RecyclerView recyclerView;

    private InfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        findViewById(R.id.info_button_bottomButton).setVisibility(View.GONE);

        title = findViewById(R.id.info_textView_title);
        title.setText("관리자 명단");

        recyclerView = findViewById(R.id.info_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new InfoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        adapter.update(Globals.adminInfo);

        btn = findViewById(R.id.info_button_roundButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO server로 admin등록하기
            }
        });
    }
}
