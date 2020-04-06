package hanyang.ac.kr.belieme.dataType;

import androidx.annotation.NonNull;

public enum Permission { USER, ADMIN, MASTER, DEVELOPER ,ERROR;

    public String toKoreanString() {
        switch (this) {
            case USER :
                return "사용자";
            case ADMIN :
                return "관리자";
            case MASTER:
                return "으뜸 관리자";
            case DEVELOPER:
                return "개발자";
            default :
                return "ERROR";
        }
    }

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case USER :
                return "USER";
            case ADMIN:
                return "ADMIN";
            case MASTER:
                return "MASTER";
            case DEVELOPER:
                return "DEVELOPER";
            default :
                return "ERROR";
        }
    }

    public static Permission stringToHistoryStatus(String input) {
        switch (input) {
            case "USER" :
                return USER;
            case "ADMIN" :
                return ADMIN;
            case "MASTER" :
                return MASTER;
            case "DEVELOPER" :
                return DEVELOPER;
            default :
                return ERROR;
        }
    }

    public Permission nextPermission() {
        switch(this) {
            case DEVELOPER:
                return MASTER;
            case MASTER:
                return ADMIN;
            case ADMIN:
                return USER;
            case USER:
                return ERROR;
            default:
                return null;
        }
    }
}
