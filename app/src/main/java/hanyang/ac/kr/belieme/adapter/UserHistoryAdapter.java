package hanyang.ac.kr.belieme.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.DetailHistoryActivity;
import hanyang.ac.kr.belieme.activity.MainActivity;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.dataType.HistoryStatus;

public class UserHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<History> displayedHistoryList;
    ArrayList<History> historyList;

    private boolean isReturnedHidden;
    private boolean isExpiredHidden;

    public UserHistoryAdapter() {
    }

    public UserHistoryAdapter(Context context) {
        this.context = context;
        displayedHistoryList = new ArrayList<>();
        historyList = new ArrayList<>();
        isReturnedHidden = true;
        isExpiredHidden = true;
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
        } else if(viewType == History.VIEW_TYPE_PROGRESS) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.progress_cell, parent, false);
            ProgressViewHolder viewHolder = new ProgressViewHolder(group);
            return viewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
            headerViewHolder.headerTitle.setText(displayedHistoryList.get(position).getStatus().toKoreanString());
            if(displayedHistoryList.get(position).getStatus() == HistoryStatus.RETURNED) {
                headerViewHolder.checkBox.setVisibility(View.VISIBLE);
                headerViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isReturnedHidden = !isChecked;
                        displayedHistoryList = addHeaders(sortWithStatus(historyList));
                        notifyDataSetChanged();
                    }
                });
            } else if(displayedHistoryList.get(position).getStatus() == HistoryStatus.EXPIRED) {
                headerViewHolder.checkBox.setVisibility(View.VISIBLE);
                headerViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isExpiredHidden = !isChecked;
                        displayedHistoryList = addHeaders(sortWithStatus(historyList));
                        notifyDataSetChanged();
                    }
                });
            } else {
                headerViewHolder.checkBox.setVisibility(View.INVISIBLE);
            }
        } else if(holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = ((ItemViewHolder)holder);
            final History history = displayedHistoryList.get(position);
            itemViewHolder.itemName.setText(history.getTypeName() + " " + history.getItemNum());
            itemViewHolder.timeStamps.setText(history.dateToString());
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailHistoryActivity.class);
                    intent.putExtra("id", history.getId());
                    context.startActivity(intent);
                }
            });
            switch (history.getStatus()) {
                case REQUESTED:
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorHanyangBlue));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorHanyangBlue));
                    itemViewHolder.btn.setVisibility(View.VISIBLE);
                    itemViewHolder.btn.setText("취소");
                    itemViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("요청을 취소하겠습니까?")
                                    .setPositiveButton("요청 취소하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            history.setStatus(HistoryStatus.EXPIRED);
                                            HistoryCancelTask historyCancelTask = new HistoryCancelTask();
                                            historyCancelTask.execute(history.getId());
                                        }
                                    })
                                    .setNegativeButton("취소", null)
                                    .create()
                                    .show();
                        }
                    });
                    break;
                case USING: {
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorUsedGreen));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorUsedGreen));
                    itemViewHolder.btn.setVisibility(View.INVISIBLE);
                    break;
                }
                case DELAYED: {
                    itemViewHolder.itemName.setTextColor(context.getResources().getColor(R.color.colorWarnRed));
                    itemViewHolder.timeStamps.setTextColor(context.getResources().getColor(R.color.colorWarnRed));
                    itemViewHolder.btn.setVisibility(View.INVISIBLE);
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
        else if(holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder)holder;
            errorViewHolder.errorMessage.setText(displayedHistoryList.get(position).getErrorMessage());
        } else if(holder instanceof ProgressViewHolder) {
            ProgressViewHolder viewHolder = (ProgressViewHolder)holder;
        }
    }

    @Override
    public int getItemCount() {
        return displayedHistoryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return displayedHistoryList.get(position).getViewType();
    }

    public void update(ArrayList<History> list) {
        historyList.clear();
        historyList.addAll(list);
        displayedHistoryList.clear();
        displayedHistoryList.addAll(addHeaders(list));
        notifyDataSetChanged();
    }

    public void updateToError(String message) {
        historyList.clear();
        historyList.add(History.getErrorHistory(message));
        displayedHistoryList.clear();
        displayedHistoryList.add(History.getErrorHistory(message));
        notifyDataSetChanged();
    }

    public void updateToProgress() {
        historyList.clear();
        historyList.add(History.getProgressHistory());
        displayedHistoryList.clear();
        displayedHistoryList.add(History.getProgressHistory());
        notifyDataSetChanged();
    }


    public ArrayList<History> reverseList(ArrayList<History> list) {
        ArrayList<History> result = new ArrayList<>();
        for(int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }
        return result;
    }

    public ArrayList<History> sortWithStatus(ArrayList<History> list) {
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

    public ArrayList<History> addHeaders(ArrayList<History> list) {
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
        CheckBox checkBox;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.listHeaderCell_textView);
            checkBox = itemView.findViewById(R.id.listHeaderCell_hider);
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

    private class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView errorMessage;
        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            errorMessage = itemView.findViewById(R.id.errorCell_textView_message);
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<History>>> {

        @Override
        protected ExceptionAdder<ArrayList<History>> doInBackground(Void... voids) {
            publishProgress();
            try {
                return new ExceptionAdder<>(HistoryRequest.getListByRequesterId(Integer.parseInt(Globals.userInfo.getStudentId())));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            ((MainActivity)context).setChangeModeBtnEnabled(false);
            updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<History>> result) {
            if (result.getBody() != null) {
                update(result.getBody());
            } else {
                updateToError(result.getException().getMessage());
            }
            ((MainActivity)context).setChangeModeBtnEnabled(true);
        }
    }

    private class HistoryCancelTask extends AsyncTask<Integer, Void, ExceptionAdder<ArrayList<History>>> {
        ProgressDialog progressDialog = new ProgressDialog(context);
        @Override
        protected ExceptionAdder<ArrayList<History>> doInBackground(Integer... integers) {
            publishProgress();
            try {
                return new ExceptionAdder<>(HistoryRequest.cancelItem(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressDialog.setMessage("처리 중입니다.");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<History>> result) {
            if(result.getException() != null) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(result.getException().getMessage())
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
                                historyReceiveTask.execute();
                            }
                        })
                        .create()
                        .show();
            }
            else {
                update(result.getBody());
                Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
            progressDialog.cancel();
        }
    }
}
