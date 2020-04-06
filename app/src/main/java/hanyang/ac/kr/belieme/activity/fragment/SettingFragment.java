package hanyang.ac.kr.belieme.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.activity.AdminManageActivity;
import hanyang.ac.kr.belieme.activity.AppInfoActivity;
import hanyang.ac.kr.belieme.activity.MainActivity;
import hanyang.ac.kr.belieme.activity.UserInfoActivity;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.adapter.SettingAdapter;
import hanyang.ac.kr.belieme.dataType.Permission;
import hanyang.ac.kr.belieme.dataType.Setting;

public class SettingFragment extends Fragment {
    View layoutView;
    MainActivity context;

    SettingAdapter adapter;

    private boolean onlyResume;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_setting, container, false);
        context = (MainActivity)getActivity();

        onlyResume = false;

        context.setChangeModeBtnVisibility(View.INVISIBLE);

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

        if(Globals.userInfo.getPermission() != Permission.USER && Globals.userInfo.getPermission() != Permission.ERROR) {
            settings.add(new Setting("관리자 명단 관리", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdminManageActivity.class);
                    startActivity(intent);
                }
            }));
        }

        settings.add(new Setting("정보", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppInfoActivity.class);
                startActivity(intent);
            }
        }));

        adapter.update(settings);

        return layoutView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onlyResume == false) {
            onlyResume = true;
        } else {
            ArrayList<Setting> settings = new ArrayList<>();

            settings.add(new Setting("개인정보", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    startActivity(intent);
                }
            }));

            if (Globals.userInfo.getPermission() != Permission.USER && Globals.userInfo.getPermission() != Permission.ERROR) {
                settings.add(new Setting("관리자 명단 관리", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AdminManageActivity.class);
                        startActivity(intent);
                    }
                }));
            }

            settings.add(new Setting("정보", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AppInfoActivity.class);
                    startActivity(intent);
                }
            }));

            adapter.update(settings);
        }
    }

    @Override
    public void onPause() {
        Log.d("OnPause", "");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("OnStop", "");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("OnDestroy", "");
    }
}
