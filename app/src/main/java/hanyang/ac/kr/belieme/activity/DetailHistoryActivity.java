package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;

public class DetailHistoryActivity extends AppCompatActivity {
    TextView title;
    Button btn;
    RecyclerView recyclerView;

    InfoAdapter adapter;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        title = findViewById(R.id.info_textView_title);
        recyclerView = findViewById(R.id.info_recyclerView);
        btn = findViewById(R.id.info_btn);

        btn.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new InfoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        title = findViewById(R.id.info_textView_title);
        title.setText("기록 세부사항");

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        GetItemTask getItemTask = new GetItemTask();
        getItemTask.execute(id);
    }

    private class GetItemTask extends AsyncTask<Integer, Void, ExceptionAdder<History>> {
        @Override
        protected ExceptionAdder<History> doInBackground(Integer... integers) {
            try {
                return new ExceptionAdder<>(HistoryRequest.getHistoryById(integers[0]));
            } catch (IOException e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            } catch (JSONException e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            } catch (InternalServerException e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<History> result) {
            if(result.getException() == null) {
                History history = result.getBody();
                ArrayList<Pair<String, String>> list = new ArrayList<>();
                SimpleDateFormat formatter = new SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분");
                switch (history.getStatus()) {
                    case REQUESTED:
                        list.add(new Pair<>("물품 이름", history.getTypeName()));
                        list.add(new Pair<>("물품 이모지", history.getTypeEmoji()));
                        list.add(new Pair<>("물품 번호", String.valueOf(history.getItemNum())));
                        list.add(new Pair<>("상태", history.getStatus().toKoreanString()));
                        list.add(new Pair<>("요청자 이름", history.getRequesterName()));
                        list.add(new Pair<>("요청자 학번", String.valueOf(history.getRequesterId())));
                        list.add(new Pair<>("요청 시간", formatter.format(history.getRequestTimeStamp())));
                        break;
                    case USING:
                    case DELAYED:
                        list.add(new Pair<>("물품 이름", history.getTypeName()));
                        list.add(new Pair<>("물품 이모지", history.getTypeEmoji()));
                        list.add(new Pair<>("물품 번호", String.valueOf(history.getItemNum())));
                        list.add(new Pair<>("상태", history.getStatus().toKoreanString()));
                        list.add(new Pair<>("요청자 이름", history.getRequesterName()));
                        list.add(new Pair<>("요청자 학번", String.valueOf(history.getRequesterId())));
                        list.add(new Pair<>("요청 시간", formatter.format(history.getRequestTimeStamp())));
                        list.add(new Pair<>("요청 승인자 이름", history.getResponseManagerName()));
                        list.add(new Pair<>("요청 승인자 학번", String.valueOf(history.getResponseManagerId())));
                        list.add(new Pair<>("요청 승인 시간", formatter.format(history.getResponseTimeStamp())));
                        break;
                    case RETURNED:
                        list.add(new Pair<>("물품 이름", history.getTypeName()));
                        list.add(new Pair<>("물품 이모지", history.getTypeEmoji()));
                        list.add(new Pair<>("물품 번호", String.valueOf(history.getItemNum())));
                        list.add(new Pair<>("상태", history.getStatus().toKoreanString()));
                        list.add(new Pair<>("요청자 이름", history.getRequesterName()));
                        list.add(new Pair<>("요청자 학번", String.valueOf(history.getRequesterId())));
                        list.add(new Pair<>("요청 시간", formatter.format(history.getRequestTimeStamp())));
                        list.add(new Pair<>("요청 승인자 이름", history.getResponseManagerName()));
                        list.add(new Pair<>("요청 승인자 학번", String.valueOf(history.getResponseManagerId())));
                        list.add(new Pair<>("요청 승인 시간", formatter.format(history.getResponseTimeStamp())));
                        list.add(new Pair<>("반납 승인자 이름", history.getReturnManagerName()));
                        list.add(new Pair<>("반납 승인자 학번", String.valueOf(history.getReturnManagerId())));
                        list.add(new Pair<>("반납 시간", formatter.format(history.getReturnTimeStamp())));
                        break;
                    case EXPIRED:
                        list.add(new Pair<>("물품 이름", history.getTypeName()));
                        list.add(new Pair<>("물품 이모지", history.getTypeEmoji()));
                        list.add(new Pair<>("물품 번호", String.valueOf(history.getItemNum())));
                        list.add(new Pair<>("상태", history.getStatus().toKoreanString()));
                        list.add(new Pair<>("요청자 이름", history.getRequesterName()));
                        list.add(new Pair<>("요청자 학번", String.valueOf(history.getRequesterId())));
                        list.add(new Pair<>("요청 시간", formatter.format(history.getRequestTimeStamp())));
                        list.add(new Pair<>("취소 시간", formatter.format(history.getExpiredDate())));
                        break;
                    case ERROR:
                        break;
                }
                adapter.update(list);
            }
            else {
                Toast.makeText(getApplicationContext(), result.getException().getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
