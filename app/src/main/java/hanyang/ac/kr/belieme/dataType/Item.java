package hanyang.ac.kr.belieme.dataType;

public class Item {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_ERROR = 2;
    private int viewType;

    private int id;
    private int typeId;
    private int num;
    private ItemStatus status;
    private int lastHistoryId;
    private String typeName;
    private String typeEmoji;

    private History lastHistory;

    private String errorMessage;

    public Item(int typeId) {
        this.typeId = typeId;
    }

    public Item(String errorMessage) {
        this.viewType = VIEW_TYPE_ERROR;
        this.errorMessage = errorMessage;
    }

    public Item(int id, int typeId, int num, ItemStatus status, int lastHistoryId, String typeName, String typeEmoji, History lastHistory) {
        this.id = id;
        this.typeId = typeId;
        this.num = num;
        this.status = status;
        this.lastHistoryId = lastHistoryId;
        this.typeName = typeName;
        this.typeEmoji = typeEmoji;
        this.viewType = VIEW_TYPE_ITEM;
        this.lastHistory = lastHistory;
    }

    public int getViewType() {
        return viewType;
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getNum() {
        return num;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public int getLastHistoryId() {
        return lastHistoryId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeEmoji() {
        return typeEmoji;
    }

    public History getLastHistory() {
        return lastHistory;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void setLastHistoryId(int lastHistoryId) {
        this.lastHistoryId = lastHistoryId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeEmoji(String typeEmoji) {
        this.typeEmoji = typeEmoji;
    }

    public void setLastHistory(History lastHistory) {
        this.lastHistory = lastHistory;
    }
}
