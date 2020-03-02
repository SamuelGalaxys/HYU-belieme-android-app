package hanyang.ac.kr.belieme.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.dataType.HistoryStatus;

public class AdminHistoryAdapter extends RecyclerView.Adapter {

    Context context;
    List<History> historyList;

    public AdminHistoryAdapter() {
    }

    public AdminHistoryAdapter(Context context) {
        this.context = context;
        historyList = new ArrayList<>();
    }

    public AdminHistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == History.VIEW_TYPE_HEADER) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.header_cell, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(group);
            return headerViewHolder;
        } else if(viewType == History.VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.history_cell, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(group);
            return itemViewHolder;
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
            headerViewHolder.headerTitle.setText(historyList.get(position).getStatus().toKoreanString());
        } else if(holder instanceof  ItemViewHolder) {
            final History history = historyList.get(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
            itemViewHolder.itemName.setText(history.getTmpTypeName() + " " + history.getItemNum());
            itemViewHolder.timeStamps.setText(history.dateToString());

            switch (history.getStatus()) {
                case REQUESTED:
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorHanyangBlue));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorHanyangBlue));
                    itemViewHolder.btn.setVisibility(View.VISIBLE);
                    itemViewHolder.btn.setText("확인");
                    itemViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            history.setManagerId(Integer.parseInt(Globals.userInfo.getStudentId()));
                            history.setManagerName(Globals.userInfo.getName());
                            history.setStatus(HistoryStatus.USING);
                            HistoryEditTask historyEditTask = new HistoryEditTask();
                            historyEditTask.execute(history);
                        }
                    });
                    break;
                case USING: {
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorUsedGreen));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorUsedGreen));
                    itemViewHolder.btn.setVisibility(View.VISIBLE);
                    itemViewHolder.btn.setText("반납확인");
                    itemViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            history.setStatus(HistoryStatus.RETURNED);
                            HistoryEditTask historyEditTask = new HistoryEditTask();
                            historyEditTask.execute(history);
                        }
                    });
                    break;
                }
                case DELAYED: {
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorWarnRed));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorWarnRed));
                    itemViewHolder.btn.setVisibility(View.VISIBLE);
                    itemViewHolder.btn.setText("반납확인");
                    itemViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            history.setStatus(HistoryStatus.RETURNED);
                            HistoryEditTask editTask = new HistoryEditTask();
                            editTask.execute(history);
                        }
                    });
                    break;
                }
                case RETURNED:
                case EXPIRED: {
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorDisableGray));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorDisableGray));
                    itemViewHolder.btn.setVisibility(View.INVISIBLE);
                    break;
                }
                default: {
                    break;
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return historyList.get(position).getViewType();
    }

    public void update(ArrayList<History> list) {
        historyList.clear();
        historyList.addAll(addHeaders(list));
        notifyDataSetChanged();
    }

    public static ArrayList<History> sortWithStatus(ArrayList<History> list) {
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
                    returnedList.add(history);
                    break;
                case EXPIRED:
                    expiredList.add(history);
                    break;
                default:
                    break;
            }
        }
        ArrayList<History> result = new ArrayList<>();
        result.addAll(requestedList);
        result.addAll(usingList);
        result.addAll(delayedList);
        result.addAll(returnedList);
        result.addAll(expiredList);

        return result;
    }

    public static ArrayList<History> addHeaders(ArrayList<History> list) {
        ArrayList<History> sortedList = sortWithStatus(list);
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

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.listHeaderCell_textView);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView timeStamps;
        Button btn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.historyCell_textView_item);
            timeStamps = itemView.findViewById(R.id.historyCell_textView_date);
            btn = itemView.findViewById(R.id.historyCell_btn_btn);
        }
    }

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ArrayList<History>> {

        @Override
        protected ArrayList<History> doInBackground(Void... voids) {
            try {
                return HistoryRequest.getList();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<History> result) {
            update(result);
        }
    }

    private class HistoryEditTask extends AsyncTask<History, Void, Void> {

        TextView view;
        int itemNum;

        @Override
        protected Void doInBackground(History... histories) {
            try {
                HistoryRequest.editItem(histories[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
            historyReceiveTask.execute();
        }
    }
}
