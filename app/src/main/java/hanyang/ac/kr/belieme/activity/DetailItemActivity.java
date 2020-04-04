package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.HistoryStatus;
import hanyang.ac.kr.belieme.dataType.Item;
import hanyang.ac.kr.belieme.dataType.ItemRequest;
import hanyang.ac.kr.belieme.dataType.ItemStatus;

public class DetailItemActivity extends AppCompatActivity {
    DetailItemActivity context;

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

        context = this;

        findViewById(R.id.info_button_roundButton).setVisibility(View.GONE);

        title = findViewById(R.id.info_textView_title);
        recyclerView = findViewById(R.id.info_recyclerView);
        activateBtn = findViewById(R.id.info_button_bottomButton);

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
        protected void onProgressUpdate(Void... values) {
            activateBtn.setVisibility(View.GONE);
            ArrayList<Pair<String, String>> list = new ArrayList<>();
            list.add(new Pair<String, String>("__PROGRESS__", ""));
            adapter.update(list);
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
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
                formatter.setTimeZone(timeZone);
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

                    activateBtn.setText("활성화하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("이 물품을 활성화하시겠습니까?")
                                    .setPositiveButton("활성화하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivateItemTask activateItemTask = new ActivateItemTask();
                                            activateItemTask.execute(id);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
                        }
                    });
                } else if (item.getStatus() == ItemStatus.USABLE) {
                    list.add(new Pair<>("물품 이름", item.getTypeName()));
                    list.add(new Pair<>("물품 이모지", item.getTypeEmoji()));
                    list.add(new Pair<>("물품 번호", String.valueOf(item.getNum())));
                    list.add(new Pair<>("상태", item.getStatus().toKoreanString()));

                    activateBtn.setText("비활성화하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("이 물품을 비활성화하시겠습니까?")
                                    .setPositiveButton("비활성화하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DeactivateItemTask deactivateItemTask = new DeactivateItemTask();
                                            deactivateItemTask.execute(id);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
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


                    activateBtn.setText("비활성화하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("이 물품을 비활성화하시겠습니까?")
                                    .setPositiveButton("비활성화하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DeactivateItemTask deactivateItemTask = new DeactivateItemTask();
                                            deactivateItemTask.execute(id);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
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
    }

    private class ActivateItemTask extends AsyncTask<Integer, Void, ExceptionAdder<Item>> {
        private int id;

        @Override
        protected ExceptionAdder<Item> doInBackground(Integer... integers) {
            publishProgress();
            try {
                id = integers[0];
                return new ExceptionAdder<>(ItemRequest.activateItem(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            activateBtn.setVisibility(View.GONE);
            ArrayList<Pair<String, String>> list = new ArrayList<>();
            list.add(new Pair<String, String>("__PROGRESS__", ""));
            adapter.update(list);
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

            if(result.getException() != null) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("문제가 발생했습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GetItemTask getItemTask = new GetItemTask();
                                getItemTask.execute(id);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                Item item = result.getBody();
                SimpleDateFormat formatter = new SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분");
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
                formatter.setTimeZone(timeZone);
                if (item.getStatus() == ItemStatus.USABLE) {
                    list.add(new Pair<>("물품 이름", item.getTypeName()));
                    list.add(new Pair<>("물품 이모지", item.getTypeEmoji()));
                    list.add(new Pair<>("물품 번호", String.valueOf(item.getNum())));
                    list.add(new Pair<>("상태", item.getStatus().toKoreanString()));

                    activateBtn.setText("비활성화 하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("이 물품을 비활성화하시겠습니까?")
                                    .setPositiveButton("비활성화하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DeactivateItemTask deactivateItemTask = new DeactivateItemTask();
                                            deactivateItemTask.execute(id);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
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

                    activateBtn.setText("비활성화하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("이 물품을 비활성화하시겠습니까?")
                                    .setPositiveButton("비활성화하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DeactivateItemTask deactivateItemTask = new DeactivateItemTask();
                                            deactivateItemTask.execute(id);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
                        }
                    });

                }
                adapter.update(list);
                Toast.makeText(context, "활성화되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DeactivateItemTask extends AsyncTask<Integer, Void, ExceptionAdder<Item>> {
        private int id;

        @Override
        protected ExceptionAdder<Item> doInBackground(Integer... integers) {
            publishProgress();
            try {
                id = integers[0];
                return new ExceptionAdder<>(ItemRequest.deactivateItem(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            activateBtn.setVisibility(View.GONE);
            ArrayList<Pair<String, String>> list = new ArrayList<>();
            list.add(new Pair<String, String>("__PROGRESS__", ""));
            adapter.update(list);
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Item> result) {
            if(!hasButton) {
                activateBtn.setVisibility(View.GONE);
            }
            else {
                activateBtn.setVisibility(View.VISIBLE);
            }
            if(result.getException() != null) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(result.getException().getMessage())
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GetItemTask getItemTask = new GetItemTask();
                                getItemTask.execute(id);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ArrayList<Pair<String, String>> list = new ArrayList<>();
                Item item = result.getBody();
                SimpleDateFormat formatter = new SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분");
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
                formatter.setTimeZone(timeZone);
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

                    activateBtn.setText("활성화하기");
                    activateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("이 물품을 활성화하시겠습니까?")
                                    .setPositiveButton("활성화하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivateItemTask activateItemTask = new ActivateItemTask();
                                            activateItemTask.execute(id);
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
                        }
                    });
                    adapter.update(list);
                    Toast.makeText(context, "비활성화되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
