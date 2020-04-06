package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.DetailItemTypeAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.Item;
import hanyang.ac.kr.belieme.dataType.ItemRequest;

public class DetailItemTypeActivity extends AppCompatActivity {
    private DetailItemTypeAdapter adapter;

    private DetailItemTypeActivity context;

    private TextView title;
    private TextView emoji;
    private ImageView addBtn;

    private RecyclerView recyclerView;

    private boolean onlyResume;

    private int typeId;
    private boolean isAdminMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item_type);

        context = this;
        onlyResume = false;

        title = findViewById(R.id.detailItemType_textView_title);
        emoji = findViewById(R.id.detailItemType_textView_emoji);
        addBtn = findViewById(R.id.detailItemType_btn_addBtn);
        recyclerView = findViewById(R.id.detailItemType_recyclerView);

        Intent thisIntent = getIntent();

        typeId = thisIntent.getIntExtra("typeId", -1);
        isAdminMode = thisIntent.getBooleanExtra("isAdminMode", false);
        title.setText(thisIntent.getStringExtra("name") + " 세부사항");
        emoji.setText(thisIntent.getStringExtra("emoji"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new DetailItemTypeAdapter(this, isAdminMode);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        ItemGetTask itemGetTask = new ItemGetTask();
        itemGetTask.execute(typeId);

        if(isAdminMode) {
            addBtn.setVisibility(View.VISIBLE);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("물품을 하나 더 추가하시겠습니까?")
                            .setPositiveButton("추가하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ItemPostTask itemPostTask = new ItemPostTask();
                                    itemPostTask.execute();
                                }
                            })
                            .setNegativeButton("취소", null)
                            .create()
                            .show();
                }
            });
        } else {
            addBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onlyResume == false) {
            onlyResume = true;
        } else {
            ItemGetTask itemGetTask = new ItemGetTask();
            itemGetTask.execute(typeId);
        }
    }

    public class ItemGetTask extends AsyncTask<Integer, Void, ExceptionAdder<ArrayList<Item>>> {
        @Override
        protected ExceptionAdder<ArrayList<Item>> doInBackground(Integer... integers) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemRequest.getListByTypeId(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            addBtn.setEnabled(false);
            adapter.updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<Item>> result) {
            if (result.getBody() != null) {
                adapter.update(result.getBody());
            } else {
                adapter.updateToError(result.getException().getMessage());
            }
            addBtn.setEnabled(true);
        }
    }

    public class ItemPostTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<Item>>> {

        @Override
        protected ExceptionAdder<ArrayList<Item>> doInBackground(Void... voids) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemRequest.addItem(new Item(typeId)));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            addBtn.setEnabled(false);
            adapter.updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<Item>> result) {
            if(result.getException() != null) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(result.getException().getMessage())
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ItemGetTask itemGetTask = new ItemGetTask();
                                itemGetTask.execute(typeId);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                adapter.update(result.getBody());
                Toast.makeText(context, "물품이 하나 늘었습니다.", Toast.LENGTH_LONG).show();
            }
            addBtn.setEnabled(true);
        }
    }
}
