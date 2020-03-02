package hanyang.ac.kr.belieme.dataType;

public class Item {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    private int viewType;

    private int id;
    private int typeId;
    private int num;
    private ItemStatus status;
    private int lastHistoryId;

    public Item(int typeId) {
        this.typeId = typeId;
    }

    public Item(int id, int typeId, int num, ItemStatus status, int lastHistoryId) {
        this.id = id;
        this.typeId = typeId;
        this.num = num;
        this.status = status;
        this.lastHistoryId = lastHistoryId;
        this.viewType = VIEW_TYPE_ITEM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public int getLastHistoryId() {
        return lastHistoryId;
    }

    public void setLastHistoryId(int lastHistoryId) {
        this.lastHistoryId = lastHistoryId;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
