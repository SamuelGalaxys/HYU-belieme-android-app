package hanyang.ac.kr.belieme.Exception;

import androidx.annotation.Nullable;

public class InternalServerException extends Exception {
    public static final int OK = 0;

    public static final int OVER_THREE_CURRENT_HISTORY_EXCEPTION = 1;

    public static final int HISTORY_FOR_SAME_ITEM_TYPE_EXCEPTION = 2;

    public static final int ITEM_NOT_AVAILABLE_EXCEPTION = 3;

    private int exceptionCode;

    public InternalServerException(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public InternalServerException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }

    @Nullable
    @Override
    public String getMessage() {
        switch (exceptionCode) {
            case OVER_THREE_CURRENT_HISTORY_EXCEPTION :
                return "This requester requests three item now";
            case HISTORY_FOR_SAME_ITEM_TYPE_EXCEPTION :
                return "This requester requests item with same item type";
            case ITEM_NOT_AVAILABLE_EXCEPTION :
                return "There is no available item";
            default:
                return super.getMessage();
        }
    }
}
