package hanyang.ac.kr.belieme.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
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

public class EditItemTypeDialog {
    private Dialog dialog;

    private Context context;
    private AdminStuffAdapter adapter;

    private TextView nameView;
    private TextView emojiView;
    private TextView saveBtn;

    private ConstraintLayout infoPartLayout;
    private ProgressBar progressBar;

    private int typeId;

    private String initName;
    private String initEmoji;

    public EditItemTypeDialog(Context context, AdminStuffAdapter adapter, int typeId, String initName, String initEmoji) {
        this.context = context;
        this.adapter = adapter;
        this.typeId = typeId;
        this.initName = initName;
        this.initEmoji = initEmoji;
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

        nameView.setText(initName);
        emojiView.setText(initEmoji);

        saveBtn.setEnabled(true);

        saveBtn.setOnClickListener(new View.OnClickListener() { //Item Type name, emoji 바꿀 때만 사용 됨
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("물품정보를 변경하시겠습니까?")
                        .setPositiveButton("변경하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ItemType itemType = new ItemType(
                                        typeId,
                                        nameView.getText().toString(),
                                        emojiView.getText().toString(),
                                        0,
                                        0);
                                ItemTypeEditTask editTask = new ItemTypeEditTask();
                                editTask.execute(itemType);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
            }
        });
    }

    public class ItemTypeEditTask extends AsyncTask<ItemType, Void, ExceptionAdder<ArrayList<ItemType>>> {

        @Override
        protected void onPreExecute() {
            nameView.setEnabled(false);
            emojiView.setEnabled(false);
            saveBtn.setEnabled(false);

            dialog.setCancelable(false);
        }

        @Override
        protected ExceptionAdder<ArrayList<ItemType>> doInBackground(ItemType... itemTypes) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemTypeRequest.editItem(itemTypes[0]));
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
            if(result.getException() == null) {
                adapter.update(result.getBody());
                dialog.dismiss();
                Toast.makeText(context, "물품의 정보가 변경되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
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
