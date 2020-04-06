package hanyang.ac.kr.belieme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.dialog.AddAdminDialog;
import hanyang.ac.kr.belieme.adapter.AdminAdapter;
import hanyang.ac.kr.belieme.adapter.InfoAdapter;
import hanyang.ac.kr.belieme.dataType.AdminInfo;
import hanyang.ac.kr.belieme.dataType.AdminInfoRequest;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.Permission;

public class AdminManageActivity extends AppCompatActivity {
    private Context context;

    private TextView title;
    private ImageView btn;
    private RecyclerView recyclerView;

    private AdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        context = this;

        findViewById(R.id.info_button_bottomButton).setVisibility(View.GONE);

        title = findViewById(R.id.info_textView_title);
        title.setText("관리자 명단");

        recyclerView = findViewById(R.id.info_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AdminAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        AdminGetTask adminGetTask = new AdminGetTask();
        adminGetTask.execute();

        btn = findViewById(R.id.info_button_roundButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAdminDialog dialog = new AddAdminDialog(context, adapter);
                dialog.showAddAdminDialog();
            }
        });

        if(Globals.userInfo.getPermission() == Permission.MASTER || Globals.userInfo.getPermission() == Permission.DEVELOPER) {
            btn.setVisibility(View.VISIBLE);
        }
    }

    public void setBtnVisibility() {
        if(Globals.userInfo.getPermission() == Permission.MASTER || Globals.userInfo.getPermission() == Permission.DEVELOPER) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private class AdminGetTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<AdminInfo>>> {

        @Override
        protected ExceptionAdder<ArrayList<AdminInfo>> doInBackground(Void... voids) {
            publishProgress();
            try {
                return new ExceptionAdder<>(AdminInfoRequest.getList());
            } catch (Exception e) {
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            adapter.updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<AdminInfo>> arrayListExceptionAdder) {
            if(arrayListExceptionAdder.getException() == null) {
                Globals.adminInfo = arrayListExceptionAdder.getBody();
                Globals.userInfo.setPermission(Globals.getPermission(Integer.parseInt(Globals.userInfo.getStudentId())));
                if(context instanceof AdminManageActivity) {
                    ((AdminManageActivity)context).setBtnVisibility();
                    if(Globals.userInfo.getPermission() == Permission.USER) {
                        Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                adapter.update(Globals.adminInfo);
            } else {
                adapter.updateToError(arrayListExceptionAdder.getException().getMessage());
            }
        }
    }
}
