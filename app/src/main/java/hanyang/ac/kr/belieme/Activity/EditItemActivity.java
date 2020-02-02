package hanyang.ac.kr.belieme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import hanyang.ac.kr.belieme.DataType.History;
import hanyang.ac.kr.belieme.DataType.HistoryRequest;
import hanyang.ac.kr.belieme.DataType.Item;
import hanyang.ac.kr.belieme.DataType.ItemRequest;
import hanyang.ac.kr.belieme.DataType.ItemStatus;
import hanyang.ac.kr.belieme.DataType.ItemType;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.DataType.ItemTypeRequest;

public class EditItemActivity extends AppCompatActivity {
    ArrayList<Itemholder> itemholders = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<Item>();

    History tmpHistory;

    private int typeId;

    private String typeName;

    private TextView nameView;
    private TextView emojiView;
    private TextView saveBtn;
    private ImageView addBtn;

    private class Itemholder extends LinearLayout {
        public Itemholder(Context context) {
            super(context);

            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.edit_item_cell, this, true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent thisIntent = getIntent();

        nameView = findViewById(R.id.editItem_editText_name);
        emojiView = findViewById(R.id.editItem_editText_emoji);
        saveBtn = findViewById(R.id.editItem_textView_save);
        addBtn = findViewById(R.id.editItem_btn_addBtn);

        typeId = thisIntent.getIntExtra("typeId", -1);
        typeName = thisIntent.getStringExtra("name");
        nameView.setText(thisIntent.getStringExtra("name"));
//        if(thisIntent.getStringExtra("emoji").equals("")) {
//            emojiView.setText("X");
//        }
//        else {
            emojiView.setText(thisIntent.getStringExtra("emoji"));
//        }

        String tmp = thisIntent.getStringExtra("countAndAmount").replace("개 사용 가능", "");
        tmp = tmp.replace("개 중", "");
        boolean isAmount = true;
        String tmpCount = new String("");
        String tmpAmount = new String("");
        for(int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == ' ') {
                isAmount = false;
            } else if (isAmount) {
                tmpAmount += tmp.charAt(i);
            } else {
                tmpCount += tmp.charAt(i);
            }
        }

        ItemGetTask itemGetTask = new ItemGetTask();
        itemGetTask.execute(typeId);

        addBtn.setOnClickListener(new View.OnClickListener() { //좀 더 추가버튼을 신중히 누를 수 있게 바꾸기
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

    public void addItem(Item item) {
        final Itemholder itemholder = new Itemholder(getApplicationContext());
        LinearLayout linearLayout;
        TextView textView = itemholder.findViewById(R.id.editItemCell_textView_item);
        linearLayout = (LinearLayout)(findViewById(R.id.editItem_linear_itemList));
        String output = "";
        if(item.getLastHistoryId() == -1) {
            output = item.getNum() + " " + "대여 가능";
            textView.setText(output);
        }
        else if(item.getStatus() == ItemStatus.ERROR) {
            return;
        }
        else {
            HistoryGetTask historyGetTask = new HistoryGetTask(textView, item);
            historyGetTask.execute(item.getLastHistoryId());
        }
        linearLayout.addView(itemholder);
        itemholders.add(itemholder);
    }

    public void updateAll(ArrayList<Item> items) {
        Log.d("update", String.valueOf(items.size()));
        while(itemholders.size() != 0) {
            ((LinearLayout)itemholders.get(0).getParent()).removeView(itemholders.get(0));
            itemholders.remove(0);
        }
        for(int i = 0; i < items.size(); i++) {
            addItem(items.get(i));
            System.out.println("j : " + i);
        }
    }

    public class ItemGetTask extends AsyncTask<Integer, Void, ArrayList<Item>> {
        @Override
        protected ArrayList<Item> doInBackground(Integer... integers) {
            try {
                return ItemRequest.getListByTypeId(integers[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            for(int i = 0; i < items.size(); i++) {
                Log.d("lastHistoryId", String.valueOf(items.get(i).getLastHistoryId()));
            }
            updateAll(items);
        }
    }

    public class ItemPostTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ItemRequest.addItem(new Item(typeId));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ItemGetTask itemGetTask = new ItemGetTask();
            itemGetTask.execute(typeId);
        }
    }

    public class HistoryGetTask extends AsyncTask<Integer, Void, History> {

        TextView view;
        Item item;

        public HistoryGetTask(TextView view, Item item) {
            this.view = view;
            this.item = item;
        }

        @Override
        protected History doInBackground(Integer... integers) {
            try {
                return HistoryRequest.getHistoryById(integers[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(History history) {
            super.onPostExecute(history);
            String output = "";
            switch (history.getStatus()) {
                case USING:
                case DELAYED:
                    output = item.getNum() + " " + history.getRequesterName() + "(" + history.getRequesterId() + ") 이(가) 사용 중";
                    break;
                case REQUESTED:
                    output = item.getNum() + " " + history.getRequesterName() + "(" + history.getRequesterId() + ") 이(가) 대여 요청 함";
                    break;
                case EXPIRED:
                case RETURNED:
                    output = item.getNum() + " " + "대여 가능";
                    break;
                case ERROR:
                    break;
            }
            view.setText(output);
        }
    }

    public class ItemTypeEditTask extends AsyncTask<ItemType, Void, Void> {

        @Override
        protected Void doInBackground(ItemType... itemTypes) {
            try {
                ItemTypeRequest.editItem(itemTypes[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
