package hanyang.ac.kr.belieme.DataType;

import androidx.annotation.NonNull;

public enum ItemStatus {USABLE, UNUSABLE, ERROR;
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
