package hanyang.ac.kr.belieme.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.AdminManageActivity;
import hanyang.ac.kr.belieme.adapter.AdminAdapter;
import hanyang.ac.kr.belieme.dataType.AdminInfo;
import hanyang.ac.kr.belieme.dataType.AdminInfoRequest;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.Permission;

public class AddAdminDialog {
    private AdminAdapter adminAdapter;
    private Context context;

    private Dialog dialog;

    private ConstraintLayout mainLayout;
    private ProgressBar progressBar;

    private TextView studentId;
    private TextView name;
    private Button addButton;

    public AddAdminDialog(Context context, AdminAdapter adminAdapter) {
        this.adminAdapter = adminAdapter;
        this.context = context;
    }

    public void showAddAdminDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.add_admin_dialog);

        dialog.show();

        mainLayout = dialog.findViewById(R.id.addAdminDialog_layout_main);
        progressBar = dialog.findViewById(R.id.addAdminDialog_progressBar);
        studentId = dialog.findViewById(R.id.addAdminDialog_editText_studentId);
        name = dialog.findViewById(R.id.addAdminDialog_editText_name);
        addButton = dialog.findViewById(R.id.addAdminDialog_button_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentId.getText().toString().equals("")) {
                    Toast.makeText(context, "학번을 입력하세요.", Toast.LENGTH_LONG).show();
                } else if(name.getText().toString().equals("")) {
                    Toast.makeText(context, "이름을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    AddAdminTask addAdminTask = new AddAdminTask();
                    addAdminTask.execute(new AdminInfo(Integer.parseInt(studentId.getText().toString()), name.getText().toString(), Permission.ADMIN));
                }
            }
        });
    }

    public class AddAdminTask extends AsyncTask<AdminInfo, Void, ExceptionAdder<ArrayList<AdminInfo>>> {

        @Override
        protected void onPreExecute() {
            addButton.setEnabled(false);
        }

        @Override
        protected ExceptionAdder<ArrayList<AdminInfo>> doInBackground(AdminInfo... adminInfos) {
            publishProgress();
            try {
                return new ExceptionAdder<>(AdminInfoRequest.addAdmin(adminInfos[0]));
            } catch (Exception e) {
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mainLayout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<AdminInfo>> arrayListExceptionAdder) {
            if(arrayListExceptionAdder.getException() == null) {
                dialog.dismiss();
                Toast.makeText(context, "추가되었습니다.", Toast.LENGTH_LONG).show();
                if(adminAdapter != null) {
                    Globals.adminInfo = arrayListExceptionAdder.getBody();
                    Globals.userInfo.setPermission(Globals.getPermission(Integer.parseInt(Globals.userInfo.getStudentId())));
                    adminAdapter.update(Globals.adminInfo);
                    if(Globals.userInfo.getPermission() == Permission.USER) {
                        Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_LONG).show();
                        ((AdminManageActivity)context).finish();
                    }
                }
            } else {
                Toast.makeText(context, arrayListExceptionAdder.getException().getMessage(), Toast.LENGTH_LONG).show();
                mainLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                addButton.setEnabled(true);
            }
        }
    }
}
