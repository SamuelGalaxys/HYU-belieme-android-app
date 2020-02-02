package hanyang.ac.kr.belieme.DataType;

import androidx.annotation.NonNull;

public enum HistoryStatus {REQUESTED, USING, DELAYED, RETURNED, EXPIRED, ERROR;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case REQUESTED :
                return "REQUESTED";
            case USING :
                return "USING";
            case DELAYED :
                return "DELAYED";
            case RETURNED :
                return "RETURNED";
            case EXPIRED:
                return "EXPIRED";
            default :
                return "ERROR";
        }
    }

    public static HistoryStatus stringToHistoryStatus(String input) {
        switch (input) {
            case "REQUESTED" :
                return HistoryStatus.REQUESTED;
            case "USING" :
                return HistoryStatus.USING;
            case "DELAYED" :
                return HistoryStatus.DELAYED;
            case "RETURNED" :
                return  HistoryStatus.RETURNED;
            case "EXPIRED" :
                return HistoryStatus.EXPIRED;
            default :
                return HistoryStatus.ERROR;
        }
    }
}

