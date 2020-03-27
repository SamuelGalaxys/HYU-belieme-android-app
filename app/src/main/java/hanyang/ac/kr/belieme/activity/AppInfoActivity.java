package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;

public class AppInfoActivity extends AppCompatActivity {
    private TextView title;
    private RecyclerView recyclerView;

    private InfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        findViewById(R.id.info_button_bottomButton).setVisibility(View.GONE);
        findViewById(R.id.info_button_roundButton).setVisibility(View.GONE);

        title = findViewById(R.id.info_textView_title);
        title.setText("어플 정보");

        recyclerView = findViewById(R.id.info_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new InfoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Pair<String, String>> infoList = new ArrayList<>();

        infoList.add(new Pair<String, String>("version", String.valueOf(Constants.currentVersion)));

        adapter.update(infoList);

    }
}
