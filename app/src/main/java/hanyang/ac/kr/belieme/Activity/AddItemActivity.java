package hanyang.ac.kr.belieme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import hanyang.ac.kr.belieme.DataType.Item;
import hanyang.ac.kr.belieme.DataType.ItemRequest;
import hanyang.ac.kr.belieme.DataType.ItemType;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.DataType.ItemTypeRequest;

public class AddItemActivity extends AppCompatActivity {

    private TextView nameView;
    private TextView amountView;
    private TextView emojiView;
    private TextView saveBtn;
    private ImageView plusBtn;
    private ImageView minusBtn;

    private int amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameView = findViewById(R.id.addItem_editText_name);
        amountView = findViewById(R.id.addItem_textView_amount);
        emojiView = findViewById(R.id.addItem_editText_emoji);
        saveBtn = findViewById(R.id.addItem_textView_save);
        plusBtn = findViewById(R.id.addItem_imageView_plusBtn);
        minusBtn = findViewById(R.id.addItem_imageView_minusBtn);

        amountView.setText("0");

        amount = 0;
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(amountView.getText().toString());
                if(amount < 99) {
                    amountView.setText(String.valueOf(++amount));
                }
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(amountView.getText().toString());
                if(amount > 0) {
                    amount--;
                }
                amountView.setText(String.valueOf(amount));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(amountView.getText().toString());
                ItemType itemType = new ItemType(0,
                        nameView.getText().toString(),
                        emojiView.getText().toString(),
                        amount,
                        amount);
                ItemTypePostTask postTask = new ItemTypePostTask();
                postTask.execute(itemType);
            }
        });
    }

    private class ItemTypePostTask extends AsyncTask<ItemType, Void, Integer> {

        @Override
        protected Integer doInBackground(ItemType... itemTypes) {
            try {
                return ItemTypeRequest.addItem(itemTypes[0]).getId();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            ItemPostTask itemPostTask = new ItemPostTask();
            itemPostTask.execute(new Item(result));
        }
    }

    private class ItemPostTask extends AsyncTask<Item, Void, Void> {

        @Override
        protected Void doInBackground(Item... items) {
            try {
                for(int i = 0; i < amount; i++) {
                    ItemRequest.addItem(items[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }
}
