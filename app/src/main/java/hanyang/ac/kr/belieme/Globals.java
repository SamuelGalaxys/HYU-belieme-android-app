package hanyang.ac.kr.belieme;

import android.util.Pair;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.dataType.AdminInfo;
import hanyang.ac.kr.belieme.dataType.Permission;
import hanyang.ac.kr.belieme.dataType.UserInfo;

public class Globals {
    public static ArrayList<AdminInfo> adminInfo = new ArrayList<>();

    public static UserInfo userInfo;

    public static boolean isAdminMode;

    public static Permission getPermission(int studentId) {
        for(int i = 0; i < adminInfo.size(); i++) {
            if(adminInfo.get(i).getStudentId() == studentId) {
                return adminInfo.get(i).getPermission();
            }
        }
        return Permission.USER;
    }
}
