package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.manager.PreferenceManager;

public class UserInfoActivity extends AppCompatActivity { // TODO 필요한 정보 더 있지 않을까? ㅎㅎ
    LinearLayout linearLayout;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        linearLayout = findViewById(R.id.userInfo_linear_layout);
        logoutBtn = findViewById(R.id.userInfo_btn_logout);

        addItem("이름", Globals.userInfo.getName());
        addItem("학번", Globals.userInfo.getStudentId());
        addItem("단과대학", Globals.userInfo.getDaehakName());
        addItem("학과", Globals.userInfo.getMajorName());
        addItem("재학상태", Globals.userInfo.getStatus());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setString(getApplicationContext(), "gaeinNo", "");
                PreferenceManager.setString(getApplicationContext(), "userNm", "");
                PreferenceManager.setString(getApplicationContext(), "sosokId", "");
                PreferenceManager.setString(getApplicationContext(), "userGb", "");
                PreferenceManager.setString(getApplicationContext(), "daehakNm", "");
                PreferenceManager.setString(getApplicationContext(), "sosokNm","");
                PreferenceManager.setString(getApplicationContext(), "userGbNm", "");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("logout", true);
                startActivity(intent);
            }
        });
    }

    ArrayList<ItemHolder> itemHolders = new ArrayList<>();

    private class ItemHolder extends LinearLayout {
        public ItemHolder(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.user_info_item_cell, this, true);
        }
    }

    public void addItem(String key, String value) {
        ItemHolder itemHolder = new ItemHolder(getApplicationContext());

        TextView keyTextView = itemHolder.findViewById(R.id.userInfoCell_textView_key);
        TextView valueTextView = itemHolder.findViewById(R.id.userInfoCell_textView_value);
        keyTextView.setText(key);
        valueTextView.setText(value);

        linearLayout.addView(itemHolder);
        itemHolders.add(itemHolder);
    }
}
