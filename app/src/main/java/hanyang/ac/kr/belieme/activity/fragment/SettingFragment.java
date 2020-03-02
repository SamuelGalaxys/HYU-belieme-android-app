package hanyang.ac.kr.belieme.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.activity.UserInfoActivity;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.SettingAdapter;
import hanyang.ac.kr.belieme.dataType.Setting;

public class SettingFragment extends Fragment {
    View layoutView;
    Context context;

    SettingAdapter adapter;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_setting, container, false);
        context = getActivity();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new SettingAdapter(context);

        RecyclerView recyclerView = layoutView.findViewById(R.id.setting_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ArrayList<Setting> settings = new ArrayList<>();

        settings.add(new Setting("개인정보", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                startActivity(intent);
            }
        }));

        adapter.update(settings);

        return layoutView;
    }
}
