package hanyang.ac.kr.belieme.Exception;

import androidx.annotation.Nullable;

public class InternalServerException extends Exception {
    public static final int OK = 0;

    public static final int NOT_FOUND_EXCEPTION = 1;
    public static final int LACK_OF_REQUEST_BODY_EXCEPTION = 2;
    public static final int OVER_THREE_CURRENT_HISTORY_EXCEPTION = 3;
    public static final int HISTORY_FOR_SAME_ITEM_TYPE_EXCEPTION = 4;
    public static final int ITEM_NOT_AVAILABLE_EXCEPTION = 5;
    public static final int WRONG_HISTORY_STATUS_EXCEPTION = 6;

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
            case NOT_FOUND_EXCEPTION :
                return "찾을 수 없습니다.";
            case LACK_OF_REQUEST_BODY_EXCEPTION:
                return "request body에 정보가 부족합니다.";
            case OVER_THREE_CURRENT_HISTORY_EXCEPTION :
                return "이미 세 개의 물품에 대여 요청을 하였습니다.";
            case HISTORY_FOR_SAME_ITEM_TYPE_EXCEPTION :
                return "이 물품을 이미 대여 요청 하였습니다.";
            case ITEM_NOT_AVAILABLE_EXCEPTION :
                return "현재 대여 가능한 물품이 없습니다.";
            case WRONG_HISTORY_STATUS_EXCEPTION :
                return "잘못된 Request입니다.";
            default:
                return super.getMessage();
        }
    }
}
