package hanyang.ac.kr.belieme.dataType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class History {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_ERROR = 2;
    public static final int VIEW_TYPE_PROGRESS = 3;

    private int viewType;

    private int id;

    private int typeId;
    private int itemNum;

    private String requesterName;
    private int requesterId;

    private String responseManagerName;
    private int responseManagerId;

    private String returnManagerName;
    private int returnManagerId;

    private Date requestTimeStamp;
    private Date responseTimeStamp;
    private Date returnTimeStamp;
    private Date cancelTimeStamp;

    private HistoryStatus status;

    private String typeName;
    private String typeEmoji;

    private String errorMessage;

    public History() {
    }

    public History(int id, int typeId, int itemNum, String requesterName, int requesterId, String responseManagerName, int responseManagerId, String returnManagerName, int returnManagerId, Date requestTimeStamp, Date responseTimeStamp, Date returnTimeStamp, Date cancelTimeStamp, HistoryStatus status, String typeName, String typeEmoji) {
        this.id = id;
        this.typeId = typeId;
        this.itemNum = itemNum;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.responseManagerName = responseManagerName;
        this.responseManagerId = responseManagerId;
        this.returnManagerName = returnManagerName;
        this.returnManagerId = returnManagerId;
        this.requestTimeStamp = requestTimeStamp;
        this.responseTimeStamp = responseTimeStamp;
        this.returnTimeStamp = returnTimeStamp;
        this.cancelTimeStamp = cancelTimeStamp;
        this.status = status;
        this.typeName = typeName;
        this.typeEmoji = typeEmoji;

    }

    public History(int id, int typeId, int itemNum, String requesterName, int requesterId, String responseManagerName, int responseManagerId, String returnManagerName, int returnManagerId, Date requestTimeStamp, Date responseTimeStamp, Date returnTimeStamp, Date cancelTimeStamp, HistoryStatus status) {
        this.id = id;
        this.typeId = typeId;
        this.itemNum = itemNum;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.responseManagerName = responseManagerName;
        this.responseManagerId = responseManagerId;
        this.returnManagerName = returnManagerName;
        this.returnManagerId = returnManagerId;
        this.requestTimeStamp = requestTimeStamp;
        this.responseTimeStamp = responseTimeStamp;
        this.returnTimeStamp = returnTimeStamp;
        this.cancelTimeStamp = cancelTimeStamp;
        this.status = status;
    }

    public History(int typeId, int itemNum, String requesterName, int requesterId) {
        this.id = 0;
        this.typeId = typeId;
        this.itemNum = itemNum;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.responseManagerName = "";
        this.responseManagerId = 0;
        this.requestTimeStamp = new Date(0);
        this.responseTimeStamp = new Date(0);
        this.returnTimeStamp = new Date(0);
        this.cancelTimeStamp = new Date(0);
        this.status = HistoryStatus.REQUESTED;
        this.typeName = "";
        this.typeEmoji = "";
    }

    public static History getErrorHistory(String errorMessage) {
        History history = new History();
        history.setErrorMessage(errorMessage);
        history.setViewType(VIEW_TYPE_ERROR);
        return history;
    }

    public static History getProgressHistory() {
        History history = new History();
        history.setViewType(VIEW_TYPE_PROGRESS);
        return history;
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public String getResponseManagerName() {
        return responseManagerName;
    }

    public int getResponseManagerId() {
        return responseManagerId;
    }

    public String getReturnManagerName() {
        return returnManagerName;
    }

    public int getReturnManagerId() {
        return returnManagerId;
    }

    public Date getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public Date getResponseTimeStamp() {
        return responseTimeStamp;
    }

    public Date getReturnTimeStamp() {
        return returnTimeStamp;
    }

    public Date getCancelTimeStamp() {
        return cancelTimeStamp;
    }

    public HistoryStatus getStatus() {
        return status;
    }

    public int getViewType() {
        return viewType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeEmoji() {
        return typeEmoji;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setResponseManagerName(String responseManagerName) {
        this.responseManagerName = responseManagerName;
    }

    public void setResponseManagerId(int responseManagerId) {
        this.responseManagerId = responseManagerId;
    }

    public void setReturnManagerName(String returnManagerName) {
        this.returnManagerName = returnManagerName;
    }

    public void setReturnManagerId(int returnManagerId) {
        this.returnManagerId = returnManagerId;
    }

    public void setStatus(HistoryStatus status) {
        this.status = status;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //TODO 생각 좀 해보기
    public Date getExpiredDate() {
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(requestTimeStamp);
        tmp.add(Calendar.MINUTE, 15);
        return tmp.getTime();
    }

    public Date getDueDate() {
//        Calendar tmp = Calendar.getInstance();
//        tmp.setTime(responseTimeStamp);
//        tmp.add(Calendar.SECOND, 30);
//        return tmp.getTime();
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(responseTimeStamp);
        tmp.add(Calendar.DATE, 7);
        if(tmp.get(Calendar.HOUR_OF_DAY) > 17 ) {
            tmp.add(Calendar.DATE, 1);
        }
        tmp.set(Calendar.HOUR_OF_DAY, 17);
        tmp.set(Calendar.MINUTE, 59);
        tmp.set(Calendar.SECOND, 59);
        if(tmp.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tmp.add(Calendar.DATE, 2);
        }
        else if(tmp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tmp.add(Calendar.DATE, 1);
        }
        return tmp.getTime();
    }

    public String getNotificationTitle() {
        String result = "";
        if(status == HistoryStatus.USING) {
            result = "빌려가신 " + getTypeName() + "의 반납 기한이 다가왔습니다. ";

        }
        else if(status == HistoryStatus.DELAYED) {
            long time = System.currentTimeMillis() - getDueDate().getTime();
            long days = time/(24*60*60*1000);
            result =  "빌려가신 " + getTypeName() + "이(가) " + days + "일 연체 되었습니다.";
        }
        else if(status == HistoryStatus.EXPIRED) {
            result = "대여 요청 하신 " + getTypeName() + "이(가) 15분이 지나 요청이 자동으로 취소 되었습니다.";
        }
        return result;
    }

    public String getNotificationMessage() {
        String result = "";
        if(status == HistoryStatus.USING) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM월 DD일");
            result = formatter.format(getDueDate()) + " 오후 6시까지 반납해주세요.";
            result = "내일 오후 6시까지 반납해주세요.";

        }
        else if(status == HistoryStatus.DELAYED) {
            result =  "빠른 시일 내에 반납해주세요.";
        }
        else if(status == HistoryStatus.EXPIRED) {
            result = "뭘 써야 한담";
        }
        return result;
    }

    public String dateToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        switch (status) {
            case DELAYED :
            case USING :
                return formatter.format(responseTimeStamp) + " ~ " + formatter.format(getDueDate());
            case RETURNED :
                return formatter.format(responseTimeStamp) + " ~ " + formatter.format(returnTimeStamp);
            case REQUESTED :
            case EXPIRED :
                return formatter.format(requestTimeStamp);
            case ERROR :
            default :
                return null;

        }
    }
}
