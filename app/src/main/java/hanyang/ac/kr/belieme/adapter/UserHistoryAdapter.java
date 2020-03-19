package hanyang.ac.kr.belieme.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.DetailHistoryActivity;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.dataType.HistoryStatus;

public class UserHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<History> historyList;

    public UserHistoryAdapter() {
    }

    public UserHistoryAdapter(Context context) {
        this.context = context;
        historyList = new ArrayList<>();
    }

    public UserHistoryAdapter(Context context, List<History> historyList) {
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
        } else if(viewType == History.VIEW_TYPE_ERROR) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.error_cell, parent, false);
            ErrorViewHolder errorViewHolder = new ErrorViewHolder(group);
            return errorViewHolder;
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
            ((ItemViewHolder)holder).bind(historyList.get(position));
        }
        else if(holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder)holder;
            errorViewHolder.errorMessage.setText(historyList.get(position).getErrorMessage());
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

    public void updateToError(String message) {
        historyList.clear();
        historyList.add(new History(message));
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

        History history;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.historyCell_textView_item);
            timeStamps = itemView.findViewById(R.id.historyCell_textView_date);
            btn = itemView.findViewById(R.id.historyCell_btn_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailHistoryActivity.class);
                    intent.putExtra("id", history.getId());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(final History history) {
            itemName.setText(history.getTypeName() + " " + history.getItemNum());
            timeStamps.setText(history.dateToString());
            switch (history.getStatus()) {
                case REQUESTED:
                    itemName.setTextColor(context.getResources().getColor(R.color.colorHanyangBlue));
                    timeStamps.setTextColor(context.getResources().getColor(R.color.colorHanyangBlue));
                    btn.setVisibility(View.VISIBLE);
                    btn.setText("취소");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            history.setStatus(HistoryStatus.EXPIRED);
                            HistoryCancelTask historyCancelTask = new HistoryCancelTask();
                            historyCancelTask.execute(history.getId());
                        }
                    });
                    break;
                case USING: {
                    itemName.setTextColor(context.getResources().getColor(R.color.colorUsedGreen));
                    timeStamps.setTextColor(context.getResources().getColor(R.color.colorUsedGreen));
                    btn.setVisibility(View.INVISIBLE);
                    break;
                }
                case DELAYED: {
                    itemName.setTextColor(context.getResources().getColor(R.color.colorWarnRed));
                    timeStamps.setTextColor(context.getResources().getColor(R.color.colorWarnRed));
                    btn.setVisibility(View.INVISIBLE);
                    break;
                }
                case RETURNED:
                case EXPIRED: {
                    itemName.setTextColor(context.getResources().getColor(R.color.colorDisableGray));
                    timeStamps.setTextColor(context.getResources().getColor(R.color.colorDisableGray));
                    btn.setVisibility(View.INVISIBLE);
                    break;
                }
                default: {
                    break;
                }
            }
            this.history = history;
        }
    }

    private class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView errorMessage;
        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            errorMessage = itemView.findViewById(R.id.errorCell_textView_message);
        }
    }

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<History>>> {

        @Override
        protected ExceptionAdder<ArrayList<History>> doInBackground(Void... voids) {
            try {
                return new ExceptionAdder<>(HistoryRequest.getListByRequesterId(Integer.parseInt(Globals.userInfo.getStudentId())));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<History>> result) {
            if (result.getBody() != null) {
                update(result.getBody());
            } else {
                updateToError(result.getException().getMessage());
            }
        }
    }

    private class HistoryCancelTask extends AsyncTask<Integer, Void, ExceptionAdder<Void>> {

        TextView view;
        int itemNum;

        @Override
        protected ExceptionAdder<Void> doInBackground(Integer... integers) {
            try {
                HistoryRequest.cancelItem(integers[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
            return new ExceptionAdder<>();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Void> result) {
            if(result.getException() != null) {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
                        historyReceiveTask.execute();
                    }
                });
                AlertDialog dialog = builder.create();
            }
            else {
                HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
                historyReceiveTask.execute();
            }
        }
    }
}
