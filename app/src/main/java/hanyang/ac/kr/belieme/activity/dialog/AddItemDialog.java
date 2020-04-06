package hanyang.ac.kr.belieme.activity.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.AdminStuffAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;

public class AddItemDialog {
    private Dialog dialog;

    private Context context;
    private AdminStuffAdapter adapter;

    private ConstraintLayout infoPartLayout;
    private ProgressBar progressBar;

    private TextView nameView;
    private TextView amountView;
    private TextView emojiView;
    private TextView saveBtn;
    private ImageView plusBtn;
    private ImageView minusBtn;

    private int amount;

    public AddItemDialog(Context context, AdminStuffAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    public void showAddItemDialog() {
        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_set_item_type_info);

        dialog.show();

        progressBar = dialog.findViewById(R.id.setItemTypeInfo_prgressBar);
        infoPartLayout = dialog.findViewById(R.id.setItemTypeInfo_layout_infoPart);

        nameView = dialog.findViewById(R.id.setItemTypeInfo_editText_name);
        emojiView = dialog.findViewById(R.id.setItemTypeInfo_editText_emoji);
        saveBtn = dialog.findViewById(R.id.setItemTypeInfo_textView_save);
        amountView = dialog.findViewById(R.id.setItemTypeInfo_textView_amount);
        plusBtn = dialog.findViewById(R.id.setItemTypeInfo_imageView_plusBtn);
        minusBtn = dialog.findViewById(R.id.setItemTypeInfo_imageView_minusBtn);

        amountView.setVisibility(View.VISIBLE);
        plusBtn.setVisibility(View.VISIBLE);
        minusBtn.setVisibility(View.VISIBLE);

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
                                    Toast.makeText(context, "수량이 0인 물품은 등록할 수 없습니다.", Toast.LENGTH_LONG).show();
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

    private class ItemTypePostTask extends AsyncTask<ItemType, Void, ExceptionAdder<ArrayList<ItemType>>> {
        @Override
        protected void onPreExecute() {
            plusBtn.setEnabled(false);
            minusBtn.setEnabled(false);
            amountView.setEnabled(false);
            nameView.setEnabled(false);
            emojiView.setEnabled(false);
            saveBtn.setEnabled(false);
            dialog.setCancelable(false);
        }

        @Override
        protected ExceptionAdder<ArrayList<ItemType>> doInBackground(ItemType... itemTypes) {
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
            infoPartLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<ItemType>> result) {
            super.onPostExecute(result);
            if(result.getException() == null) {
                adapter.update(result.getBody());
                dialog.dismiss();
                Toast.makeText(context, "추가되었습니다.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
                plusBtn.setEnabled(true);
                minusBtn.setEnabled(true);
                amountView.setEnabled(true);
                nameView.setEnabled(true);
                emojiView.setEnabled(true);
                saveBtn.setEnabled(true);

                infoPartLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                dialog.setCancelable(true);
            }
        }
    }
}
