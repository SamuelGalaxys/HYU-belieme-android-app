package hanyang.ac.kr.belieme.dataType;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemType {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_ERROR = 2;
    public static final int VIEW_TYPE_PROGRESS = 3;


    private int viewType;

    private int id;
    private String name;
    private String emoji;
    private int amount;
    private int count;
    private ItemStatus status;

    private String errorMessage;

    public ItemType() {
    }

    public ItemType(int id, String name, String emoji, int amount, int count) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.count = count;
        this.amount = amount;
        this.viewType = VIEW_TYPE_ITEM;
    }

    public ItemType(int id, String name, String emoji, int amount, int count, ItemStatus status) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.amount = amount;
        this.count = count;
        this.status = status;
        this.viewType = VIEW_TYPE_ITEM;
    }

    public static ItemType getErrorItemType(String errorMessage) {
        ItemType itemType = new ItemType();
        itemType.setViewType(VIEW_TYPE_ERROR);
        itemType.setErrorMessage(errorMessage);
        return itemType;
    }

    public static ItemType getProgressItemType() {
        ItemType itemType = new ItemType();
        itemType.setViewType(VIEW_TYPE_PROGRESS);
        return itemType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getAmount() {
        return amount;
    }

    public int getCount() {
        return count;
    }

    public int getViewType() {
        return viewType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public static ArrayList<ItemType> sortWithStatus(ArrayList<ItemType> list) {
        ArrayList<ItemType> usableList = new ArrayList<>();
        ArrayList<ItemType> unusableList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            ItemType itemType = list.get(i);
            switch (itemType.getStatus()) {
                case INACTIVE:
                    break;
                case USABLE:
                    usableList.add(itemType);
                    break;
                case UNUSABLE:
                    unusableList.add(itemType);
                    break;
                case ERROR:
                    break;
            }
        }
        ArrayList<ItemType> result = new ArrayList<>();
        result.addAll(usableList);
        result.addAll(unusableList);

        return result;
    }

    public static ArrayList<ItemType> addHeaders(ArrayList<ItemType> list) {
        ArrayList<ItemType> sortedList = sortWithStatus(list);
        ArrayList<ItemType> listWithHeaders = new ArrayList<>();
        if(sortedList.size() != 0 && sortedList.get(0).getStatus() == ItemStatus.USABLE) {
            ItemType firstOfList = new ItemType();
            firstOfList.setStatus(ItemStatus.USABLE);//usable header
            firstOfList.setViewType(ItemType.VIEW_TYPE_HEADER);
            listWithHeaders.add(firstOfList);
        }

        boolean unusableHeaderAdded = false;
        for(int i = 0; i < sortedList.size(); i++) {
            if(!unusableHeaderAdded && sortedList.get(i).getStatus() == ItemStatus.UNUSABLE) {
                ItemType header = new ItemType();
                header.setViewType(ItemType.VIEW_TYPE_HEADER);
                header.setStatus(ItemStatus.UNUSABLE);//unusable header
                listWithHeaders.add(header);
                unusableHeaderAdded = true;
                i--;
            }
            else {
                sortedList.get(i).setViewType(ItemType.VIEW_TYPE_ITEM);
                listWithHeaders.add(sortedList.get(i));
            }
        }
//        if(!unusableHeaderAdded) {
//            ItemType header = new ItemType();
//            header.setViewType(ItemType.VIEW_TYPE_HEADER);
//            header.setStatus(ItemStatus.UNUSABLE);//unusable header
//            listWithHeaders.add(header);
//        }
        return listWithHeaders;
    }
}
