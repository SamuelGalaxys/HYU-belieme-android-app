package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;

import java.io.IOException;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;

public class AddItemActivity extends AppCompatActivity {
    private AddItemActivity context;

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

        context = this;

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
                new MaterialAlertDialogBuilder(context)
                        .setTitle("물품을 추가하시겠습니까?")
                        .setPositiveButton("추가하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                amount = Integer.parseInt(amountView.getText().toString());
                                if (amount <= 0) {
                                    Toast.makeText(getApplicationContext(), "수량이 0인 물품은 등록할 수 없습니다.", Toast.LENGTH_LONG).show();
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
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
            }
        });
    }

    private class ItemTypePostTask extends AsyncTask<ItemType, Void, ExceptionAdder<ItemType>> {
        ProgressDialog progressDialog = new ProgressDialog(context);

        @Override
        protected ExceptionAdder<ItemType> doInBackground(ItemType... itemTypes) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemTypeRequest.addItem(itemTypes[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressDialog.setMessage("진행 중입니다.");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ItemType> result) {
            super.onPostExecute(result);
            if(result.getException() == null) {
                progressDialog.cancel();
                Toast.makeText(context, "추가되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
