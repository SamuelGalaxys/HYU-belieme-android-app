package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;

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

        saveBtn.setEnabled(true);
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
                saveBtn.setEnabled(false);
                amount = Integer.parseInt(amountView.getText().toString());
                if (amount <= 0) {
                    Toast.makeText(getApplicationContext(), "수량이 0인 물품은 등록할 수 없습니다.", Toast.LENGTH_LONG).show();
                    saveBtn.setEnabled(true);
                } else {
                    ItemType itemType = new ItemType(0,
                            nameView.getText().toString(),
                            emojiView.getText().toString(),
                            amount,
                            amount);
                    ItemTypePostTask postTask = new ItemTypePostTask();
                    postTask.execute(itemType);
                }
            }
        });
    }

    private class ItemTypePostTask extends AsyncTask<ItemType, Void, ExceptionAdder<ItemType>> {

        @Override
        protected ExceptionAdder<ItemType> doInBackground(ItemType... itemTypes) {
            try {
                return new ExceptionAdder<>(ItemTypeRequest.addItem(itemTypes[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ItemType> result) {
            super.onPostExecute(result);
            if(result.getException() == null) {
                finish();
            }
            else {
                saveBtn.setEnabled(true);
                Toast.makeText(getApplicationContext(), result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
