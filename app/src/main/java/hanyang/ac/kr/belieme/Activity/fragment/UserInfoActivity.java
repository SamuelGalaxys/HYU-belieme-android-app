package hanyang.ac.kr.belieme.Activity.fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.DataType.UserInfo;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        addItem("이름", Globals.userInfo.getName());
        addItem("학번", Globals.userInfo.getStudentId());
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
        LinearLayout linearLayout = findViewById(R.id.userInfo_linear_layout);

        TextView keyTextView = itemHolder.findViewById(R.id.userInfoCell_textView_key);
        TextView valueTextView = itemHolder.findViewById(R.id.userInfoCell_textView_value);
        keyTextView.setText(key);
        valueTextView.setText(value);

        linearLayout.addView(itemHolder);
        itemHolders.add(itemHolder);
    }
}
