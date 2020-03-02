package hanyang.ac.kr.belieme.dataType;

import androidx.annotation.NonNull;

public enum ItemStatus {USABLE, UNUSABLE, ERROR;

    public String toKoreanString() {
        switch (this) {
            case USABLE:
                return "대여 가능";
            case UNUSABLE:
                return "대여 불가";
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
            default :
                return ERROR;
        }
    }
}
