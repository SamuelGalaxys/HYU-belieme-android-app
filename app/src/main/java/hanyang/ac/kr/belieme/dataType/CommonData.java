package hanyang.ac.kr.belieme.dataType;

import java.util.ArrayList;

public class CommonData {
    public static ArrayList<ItemType> itemTypeList = new ArrayList<>();

    public static ArrayList<Item> itemList = new ArrayList<>();

    public static ArrayList<History> historyList = new ArrayList<>();

    public void updateToError(String message) {
        historyList.clear();
        historyList.add(History.getErrorHistory(message));
    }

    public void updateToProgress() {
        historyList.clear();
        historyList.add(History.getProgressHistory());
    }

    public ArrayList<History> reverseList(ArrayList<History> list) {
        ArrayList<History> result = new ArrayList<>();
        for(int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }
        return result;
    }

    public ArrayList<History> sortWithStatus(ArrayList<History> list, boolean isReturnedHidden, boolean isExpiredHidden) {
        ArrayList<History> requestedList = new ArrayList<>();
        ArrayList<History> usingList = new ArrayList<>();
        ArrayList<History> delayedList = new ArrayList<>();
        ArrayList<History> returnedList = new ArrayList<>();
        ArrayList<History> expiredList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            History history = list.get(i);
            switch (history.getStatus()) {
                case REQUESTED:
                    requestedList.add(history);
                    break;
                case USING:
                    usingList.add(history);
                    break;
                case DELAYED:
                    delayedList.add(history);
                    break;
                case RETURNED:
                    if(!isReturnedHidden) {
                        returnedList.add(history);
                    }
                    break;
                case EXPIRED:
                    if(!isExpiredHidden) {
                        expiredList.add(history);
                    }
                    break;
                default:
                    break;
            }
        }
        ArrayList<History> result = new ArrayList<>();
        result.addAll(reverseList(requestedList));
        result.addAll(reverseList(usingList));
        result.addAll(reverseList(delayedList));
        result.addAll(reverseList(returnedList));
        result.addAll(reverseList(expiredList));

        return result;
    }

    public ArrayList<History> addHeaders(ArrayList<History> list, boolean isReturnedHidden, boolean isExpiredHidden) {
        ArrayList<History> sortedList = sortWithStatus(list, isReturnedHidden, isExpiredHidden);
        ArrayList<History> listWithHeaders = new ArrayList<>();
        History firstOfList = new History();
        firstOfList.setStatus(HistoryStatus.REQUESTED);
        firstOfList.setViewType(History.VIEW_TYPE_HEADER);
        listWithHeaders.add(firstOfList);

        HistoryStatus prevStatus = HistoryStatus.REQUESTED;
        int i = 0;
        while(prevStatus.nextStatus() != HistoryStatus.ERROR || i < sortedList.size()) {
            if(i >= sortedList.size()) {
                prevStatus = prevStatus.nextStatus();
                History header = new History();
                header.setViewType(History.VIEW_TYPE_HEADER);
                header.setStatus(prevStatus);
                listWithHeaders.add(header);
            }
            else if(sortedList.get(i).getStatus() == prevStatus) {
                sortedList.get(i).setViewType(History.VIEW_TYPE_ITEM);
                listWithHeaders.add(sortedList.get(i));
                i++;
            }
            else if (sortedList.get(i).getStatus() != prevStatus) {
                prevStatus = prevStatus.nextStatus();
                History header = new History();
                header.setViewType(History.VIEW_TYPE_HEADER);
                header.setStatus(prevStatus);
                listWithHeaders.add(header);
            }
        }
        return listWithHeaders;
    }
}
