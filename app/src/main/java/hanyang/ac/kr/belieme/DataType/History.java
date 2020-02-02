package hanyang.ac.kr.belieme.DataType;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class History {


    private int id;

    private int typeId;
    private int itemNum;

    private String requesterName;
    private int requesterId;

    private String managerName;
    private int managerId;

    private Date requestTimeStamp;
    private Date responseTimeStamp;
    private Date returnedTimeStamp;

    private HistoryStatus status;

    private String tmpTypeName;


    public History(int id, int typeId, int itemNum, String requesterName, int requesterId, String managerName, int managerId, Date requestTimeStamp, Date responseTimeStamp, Date returnedTimeStamp, HistoryStatus status) {
        this.id = id;
        this.typeId = typeId;
        this.itemNum = itemNum;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.managerName = managerName;
        this.managerId = managerId;
        this.requestTimeStamp = requestTimeStamp;
        this.responseTimeStamp = responseTimeStamp;
        this.returnedTimeStamp = returnedTimeStamp;
        this.status = status;
    }

    public History(int typeId, int itemNum, String requesterName, int requesterId) {
        this.id = 0;
        this.typeId = typeId;
        this.itemNum = itemNum;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.managerName = "";
        this.managerId = 0;
        this.requestTimeStamp = new Date(0);
        this.responseTimeStamp = new Date(0);
        this.returnedTimeStamp = new Date(0);
        this.status = HistoryStatus.REQUESTED;
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

    public String getManagerName() {
        return managerName;
    }

    public int getManagerId() {
        return managerId;
    }

    public Date getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public Date getResponseTimeStamp() {
        return responseTimeStamp;
    }

    public Date getReturnedTimeStamp() {
        return returnedTimeStamp;
    }

    public HistoryStatus getStatus() {
        return status;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public void setResponseTimeStamp(Date responseTimeStamp) {
        this.responseTimeStamp = responseTimeStamp;
    }

    public void setReturnedTimeStamp(Date resultTimeStamp) {
        this.returnedTimeStamp = resultTimeStamp;
    }

    public void setStatus(HistoryStatus status) {
        this.status = status;
    }

    public Date getDueDate() {
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

    public String dateToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        switch (status) {
            case DELAYED :
            case USING :
                return formatter.format(responseTimeStamp) + " ~ " + formatter.format(getDueDate());
            case RETURNED :
                return formatter.format(responseTimeStamp) + " ~ " + formatter.format(returnedTimeStamp);
            case REQUESTED :
            case EXPIRED :
                return formatter.format(requestTimeStamp);
            case ERROR :
            default :
                return null;

        }
    }
}
