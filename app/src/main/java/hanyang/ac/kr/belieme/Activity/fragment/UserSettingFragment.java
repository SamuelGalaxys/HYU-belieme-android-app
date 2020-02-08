package hanyang.ac.kr.belieme.Activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Activity.UserInfoActivity;
import hanyang.ac.kr.belieme.R;

public class UserSettingFragment extends Fragment {
    ArrayList<ItemHolder> itemHolders = new ArrayList<>();
    View layoutView;

    private class ItemHolder extends LinearLayout {
        public ItemHolder(Context context) {
            super(context);

            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.setting_item_cell, this, true);
        }
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_user_setting, container, false);
        addItem("개인정보", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(intent);

            }
        });

        return layoutView;
    }

    public void addItem(String settingName, View.OnClickListener onClickListener) {
        ItemHolder itemHolder = new ItemHolder(getActivity());
        LinearLayout linearLayout = layoutView.findViewById(R.id.setting_linear_layout);

        TextView itemName = itemHolder.findViewById(R.id.settingCell_textView_userInfo);
        itemName.setText(settingName);
        itemHolder.setOnClickListener(onClickListener);

        linearLayout.addView(itemHolder);
        itemHolders.add(itemHolder);
    }
}
