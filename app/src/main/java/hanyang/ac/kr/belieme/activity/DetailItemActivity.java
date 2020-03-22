package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.HistoryStatus;
import hanyang.ac.kr.belieme.dataType.Item;
import hanyang.ac.kr.belieme.dataType.ItemRequest;
import hanyang.ac.kr.belieme.dataType.ItemStatus;

public class DetailItemActivity extends AppCompatActivity {
    TextView title;
    Button activateBtn;
    RecyclerView recyclerView;

    InfoAdapter adapter;

    private int id;
    private boolean hasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        title = findViewById(R.id.info_textView_title);
        recyclerView = findViewById(R.id.info_recyclerView);
        activateBtn = findViewById(R.id.info_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new InfoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        title = findViewById(R.id.info_textView_title);
        title.setText("물품 세부사항");

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        hasButton = intent.getBooleanExtra("hasButton", false);

        GetItemTask getItemTask = new GetItemTask();
        getItemTask.execute(id);
    }



    private class GetItemTask extends AsyncTask<Integer, Void, ExceptionAdder<Item>> {

        @Override
        protected ExceptionAdder<Item> doInBackground(Integer... integers) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemRequest.getItem(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Item> result) {
            ArrayList<Pair<String, String>> list = new ArrayList<>();
            if(!hasButton) {
                activateBtn.setVisibility(View.GONE);
            }
            else {
                activateBtn.setVisibility(View.VISIBLE);
            }

            if(result.getException() == null) {
                Item item = result.getBody();
                SimpleDateFormat formatter = new SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분");
                if (item.getStatus() == ItemStatus.INACTIVE) {
                    list.add(new Pair<>("물품 이름", item.getTypeName()));
                    list.add(new Pair<>("물품 이모지", item.getTypeEmoji()));
                    list.add(new Pair<>("물품 번호", String.valueOf(item.getNum())));
                    list.add(new Pair<>("상태", item.getStatus().toKoreanString()));
                    if (item.getLastHistory() != null) {
                        list.add(new Pair<>("최종 요청자 이름", item.getLastHistory().getRequesterName()));
                        list.add(new Pair<>("최종 요청자 학번", String.valueOf(item.getLastHistory().getRequesterId())));
                        list.add(new Pair<>("요청 시간", formatter.format(item.getLastHistory().getRequestTimeStamp())));
                        if (item.getLastHistory().getStatus() == HistoryStatus.USING || item.getLastHistory().getStatus() == HistoryStatus.DELAYED) {
                            list.add(new Pair<>("요청 승인자 이름", item.getLastHistory().getResponseManagerName()));
                            list.add(new Pair<>("요청 승인자 학번", String.valueOf(item.getLastHistory().getResponseManagerId())));
                            list.add(new Pair<>("요청 승인 시간", formatter.format(item.getLastHistory().getResponseTimeStamp())));
                        }
                    }

                    activateBtn.setText("활성화 하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivateItemTask activateItemTask = new ActivateItemTask();
                            activateItemTask.execute(id);
                        }
                    });
                } else if (item.getStatus() == ItemStatus.USABLE) {
                    list.add(new Pair<>("물품 이름", item.getTypeName()));
                    list.add(new Pair<>("물품 이모지", item.getTypeEmoji()));
                    list.add(new Pair<>("물품 번호", String.valueOf(item.getNum())));
                    list.add(new Pair<>("상태", item.getStatus().toKoreanString()));

                    activateBtn.setText("비활성화 하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeactivateItemTask deactivateItemTask = new DeactivateItemTask();
                            deactivateItemTask.execute(id);
                        }
                    });
                } else if (item.getStatus() == ItemStatus.UNUSABLE) {
                    list.add(new Pair<>("물품 이름", item.getTypeName()));
                    list.add(new Pair<>("물품 이모지", item.getTypeEmoji()));
                    list.add(new Pair<>("물품 번호", String.valueOf(item.getNum())));
                    list.add(new Pair<>("상태", item.getLastHistory().getStatus().toKoreanString()));
                    list.add(new Pair<>("요청자 이름", item.getLastHistory().getRequesterName()));
                    list.add(new Pair<>("요청자 학번", String.valueOf(item.getLastHistory().getRequesterId())));
                    list.add(new Pair<>("요청 시간", formatter.format(item.getLastHistory().getRequestTimeStamp())));
                    if (item.getLastHistory().getStatus() == HistoryStatus.USING || item.getLastHistory().getStatus() == HistoryStatus.DELAYED) {
                        list.add(new Pair<>("요청 승인자 이름", item.getLastHistory().getResponseManagerName()));
                        list.add(new Pair<>("요청 승인자 학번", String.valueOf(item.getLastHistory().getResponseManagerId())));
                        list.add(new Pair<>("요청 승인 시간", formatter.format(item.getLastHistory().getResponseTimeStamp())));
                    }


                    activateBtn.setText("비활성화 하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeactivateItemTask deactivateItemTask = new DeactivateItemTask();
                            deactivateItemTask.execute(id);
                        }
                    });

                }
                adapter.update(list);
            }
            else {
                ArrayList<Pair<String, String>> error = new ArrayList<>();
                error.add(new Pair("__ERROR__", result.getException().getMessage()));
                adapter.update(error);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            ArrayList<Pair<String, String>> list = new ArrayList<>();
            list.add(new Pair<String, String>("__PROGRESS__", ""));
            adapter.update(list);
        }
    }

    private class ActivateItemTask extends AsyncTask<Integer, Void, ExceptionAdder<Void>> {
        private int id;

        @Override
        protected ExceptionAdder<Void> doInBackground(Integer... integers) {
            try {
                id = integers[0];
                ItemRequest.activateItem(integers[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
            return new ExceptionAdder<>();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Void> result) {
            if(result.getException() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GetItemTask getItemTask = new GetItemTask();
                        getItemTask.execute(id);
                    }
                });
                AlertDialog dialog = builder.create();
            }
            else {
                GetItemTask getItemTask = new GetItemTask();
                getItemTask.execute(id);
            }
        }
    }

    private class DeactivateItemTask extends AsyncTask<Integer, Void, ExceptionAdder<Void>> {
        private int id;

        @Override
        protected ExceptionAdder<Void> doInBackground(Integer... integers) {
            try {
                id = integers[0];
                ItemRequest.deactivateItem(integers[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
            return new ExceptionAdder<>();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Void> result) {
            if(result.getException() != null) {
                //TODO 알림 상자 이거 맞냐??
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GetItemTask getItemTask = new GetItemTask();
                        getItemTask.execute(id);
                    }
                });
                AlertDialog dialog = builder.create();
            }
            else {
                GetItemTask getItemTask = new GetItemTask();
                getItemTask.execute(id);
            }
        }
    }
}
