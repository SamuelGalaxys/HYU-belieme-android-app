package hanyang.ac.kr.belieme.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hanyang.ac.kr.belieme.Activity.fragment.AdminStuffListFragment;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.Activity.fragment.UserHistoryFragment;
import hanyang.ac.kr.belieme.Activity.fragment.UserSettingFragment;

public class AdminMainActivity extends AppCompatActivity {


    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private AdminStuffListFragment adminStuffListFragment = new AdminStuffListFragment();
    private UserHistoryFragment userHistoryFragment = new UserHistoryFragment();
    private UserSettingFragment userSettingFragment = new UserSettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottomNavigationView);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, adminStuffListFragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_userStuffList: {
                        transaction.replace(R.id.main_frame, adminStuffListFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_history: {
                        transaction.replace(R.id.main_frame, userHistoryFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_setting: {
                        transaction.replace(R.id.main_frame, userSettingFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
