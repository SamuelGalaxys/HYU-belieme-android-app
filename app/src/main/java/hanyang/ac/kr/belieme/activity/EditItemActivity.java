package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.adapter.EditItemAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.Item;
import hanyang.ac.kr.belieme.dataType.ItemRequest;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;

public class EditItemActivity extends AppCompatActivity {
    private EditItemAdapter adapter;
    private int typeId;

    private TextView nameView;
    private TextView emojiView;
    private TextView saveBtn;
    private ImageView addBtn;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        nameView = findViewById(R.id.editItem_editText_name);
        emojiView = findViewById(R.id.editItem_editText_emoji);
        saveBtn = findViewById(R.id.editItem_textView_save);
        addBtn = findViewById(R.id.editItem_btn_addBtn);
        recyclerView = findViewById(R.id.editItem_recyclerView);

        Intent thisIntent = getIntent();

        typeId = thisIntent.getIntExtra("typeId", -1);
        nameView.setText(thisIntent.getStringExtra("name"));
        emojiView.setText(thisIntent.getStringExtra("emoji"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new EditItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        ItemGetTask itemGetTask = new ItemGetTask();
        itemGetTask.execute(typeId);

        addBtn.setOnClickListener(new View.OnClickListener() { //TODO 좀 더 추가버튼을 신중히 누를 수 있게 바꾸기
            @Override
            public void onClick(View v) {
                ItemPostTask itemPostTask = new ItemPostTask();
                itemPostTask.execute();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() { //Item Type name, emoji 바꿀 때만 사용 됨
            @Override
            public void onClick(View v) {
                ItemType itemType = new ItemType(
                        typeId,
                        nameView.getText().toString(),
                        emojiView.getText().toString(),
                        0,
                        0);
                ItemTypeEditTask editTask = new ItemTypeEditTask();
                editTask.execute(itemType);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ItemGetTask itemGetTask = new ItemGetTask();
        itemGetTask.execute(typeId);
    }

    public class ItemGetTask extends AsyncTask<Integer, Void, ExceptionAdder<ArrayList<Item>>> {
        @Override
        protected ExceptionAdder<ArrayList<Item>> doInBackground(Integer... integers) {
            try {
                return new ExceptionAdder<>(ItemRequest.getListByTypeId(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<Item>> result) {
            if (result.getBody() != null) {
                adapter.update(result.getBody());
            } else {
                adapter.updateToError(result.getException().getMessage());
            }
        }
    }

    public class ItemPostTask extends AsyncTask<Void, Void, ExceptionAdder<Void>> {

        @Override
        protected ExceptionAdder<Void> doInBackground(Void... voids) {
            try {
                ItemRequest.addItem(new Item(typeId));
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
                        ItemGetTask itemGetTask = new ItemGetTask();
                        itemGetTask.execute(typeId);
                    }
                });
                AlertDialog dialog = builder.create();
            }
            else {
                ItemGetTask itemGetTask = new ItemGetTask();
                itemGetTask.execute(typeId);
            }
        }
    }

    public class ItemTypeEditTask extends AsyncTask<ItemType, Void, ExceptionAdder<Void>> {

        @Override
        protected ExceptionAdder<Void> doInBackground(ItemType... itemTypes) {
            try {
                ItemTypeRequest.editItem(itemTypes[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
            return new ExceptionAdder<>();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Void> result) {
            if(result.getException() == null) {
                Toast.makeText(getApplicationContext(), "물품의 정보가 변경되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
