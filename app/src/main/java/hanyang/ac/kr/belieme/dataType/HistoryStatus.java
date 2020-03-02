package hanyang.ac.kr.belieme.dataType;

import androidx.annotation.NonNull;

public enum HistoryStatus {REQUESTED, USING, DELAYED, RETURNED, EXPIRED, ERROR;

    public String toKoreanString() {
        switch (this) {
            case REQUESTED :
                return "요청됨";
            case USING :
                return "사용중";
            case DELAYED :
                return "연체됨";
            case RETURNED :
                return "반납됨";
            case EXPIRED:
                return "만료됨";
            default :
                return "ERROR";
        }
    }

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

    public HistoryStatus nextStatus() {
        switch (this) {
            case REQUESTED:
                return USING;
            case USING:
                return DELAYED;
            case DELAYED:
                return RETURNED;
            case RETURNED:
                return EXPIRED;
            case EXPIRED:
                return ERROR;
            default:
                return null;
        }
    }
}

