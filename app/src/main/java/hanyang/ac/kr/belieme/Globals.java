package hanyang.ac.kr.belieme;

import android.util.Pair;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.dataType.UserInfo;

public class Globals {
    public static ArrayList<Pair<String, String>> adminInfo = new ArrayList<>();

    public static UserInfo userInfo;

    public static boolean isAdminMode;

    public static boolean isAdmin() {
        for(int i = 0; i < adminInfo.size(); i++) {
            if(adminInfo.get(i).first.equals(userInfo.getStudentId())) {
                return true;
            }
        }
        return false;
    }
}
