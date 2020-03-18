package hanyang.ac.kr.belieme.dataType;

import androidx.annotation.NonNull;

public enum ItemStatus {USABLE, UNUSABLE, INACTIVE, ERROR;

    public String toKoreanString() {
        switch (this) {
            case USABLE:
                return "대여 가능";
            case UNUSABLE:
                return "대여 불가";
            case INACTIVE:
                return "사용 불가";
            default :
                return "ERROR";
        }
    }
    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case USABLE:
                return "USABLE";
            case UNUSABLE:
                return "UNUSABLE";
            case INACTIVE:
                return "INACTIVE";
            default :
                return "ERROR";
        }
    }

    public static ItemStatus stringToItemStatus(String input) {
        switch (input) {
            case "USABLE" :
                return USABLE;
            case "UNUSABLE" :
                return UNUSABLE;
            case "INACTIVE" :
                return INACTIVE;
            default :
                return ERROR;
        }
    }
}
