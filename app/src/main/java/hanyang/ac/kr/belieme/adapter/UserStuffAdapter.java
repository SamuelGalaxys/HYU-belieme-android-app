package hanyang.ac.kr.belieme.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.broadcastReceiver.AlarmReceiver;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;

public class UserStuffAdapter extends RecyclerView.Adapter {

    Context context;
    List<ItemType> itemTypeList;

    public UserStuffAdapter() {
    }

    public UserStuffAdapter(Context context) {
        this.context = context;
        itemTypeList = new ArrayList<>();
    }

    public UserStuffAdapter(Context context, List<ItemType> itemTypeList) {
        this.context = context;
        this.itemTypeList = itemTypeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == ItemType.VIEW_TYPE_HEADER) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.header_cell, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(group);
            return headerViewHolder;
        } else if(viewType == ItemType.VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.stuff_cell, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(group);
            return itemViewHolder;
        } else if(viewType == ItemType.VIEW_TYPE_ERROR) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.error_cell, parent, false);
            ErrorViewHolder errorViewHolder = new ErrorViewHolder(group);
            return errorViewHolder;
        } else if(viewType == ItemType.VIEW_TYPE_PROGRESS) {
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
            HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
            headerViewHolder.headerTitle.setText(itemTypeList.get(position).getStatus().toKoreanString());
        } else if(holder instanceof  ItemViewHolder) {
            final ItemType itemType = itemTypeList.get(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
            itemViewHolder.id.setText(String.valueOf(itemType.getId()));
            itemViewHolder.emoji.setText(itemType.getEmoji());
            itemViewHolder.name.setText(itemType.getName());
            itemViewHolder.count.setText(itemType.getAmount() + "개 중" + " " + itemType.getCount() + "개 사용 가능");

            switch (itemType.getStatus()) {
                case USABLE:
                    itemViewHolder.btn.setVisibility(View.VISIBLE);
                    itemViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HistoryPostTask historyPostTask = new HistoryPostTask();
                            historyPostTask.execute(new History(
                                    itemType.getId(),
                                    0,
                                    Globals.userInfo.getName(),
                                    Integer.parseInt(Globals.userInfo.getStudentId()))
                            );
                        }
                    });
                    break;
                case UNUSABLE:
                    itemViewHolder.btn.setVisibility(View.GONE);
                    break;
                default: {
                    break;
                }
            }

        } else if(holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder)holder;
            errorViewHolder.errorMessage.setText(itemTypeList.get(position).getErrorMessage());
        }  else if(holder instanceof ProgressViewHolder) {
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return itemTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemTypeList.get(position).getViewType();
    }

    public void update(ArrayList<ItemType> list) {
        itemTypeList.clear();
        itemTypeList.addAll(ItemType.addHeaders(list));
        notifyDataSetChanged();
    }

    public void updateToError(String message) {
        itemTypeList.clear();
        itemTypeList.add(ItemType.getErrorItemType(message));
        notifyDataSetChanged();
    }

    public void updateToProgress() {
        itemTypeList.clear();
        itemTypeList.add(ItemType.getProgressItemType());
        notifyDataSetChanged();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.listHeaderCell_textView);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.leftMargin = 16;
            itemView.setLayoutParams(params);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView emoji;
        TextView name;
        TextView count;
        Button btn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.stuffCell_textView_id);
            emoji = itemView.findViewById(R.id.stuffCell_textView_Emoji);
            name = itemView.findViewById(R.id.stuffCell_textView_name);
            count = itemView.findViewById(R.id.stuffCell_textView_count);
            btn = itemView.findViewById(R.id.stuffCell_btn_rent);

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

    private class HistoryPostTask extends AsyncTask<History, Void, ExceptionAdder<History>> {

        @Override
        protected ExceptionAdder<History> doInBackground(History... items) {
            ExceptionAdder<History> result;
            try {
                return new ExceptionAdder<>(HistoryRequest.addItem(items[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        protected void onPostExecute(ExceptionAdder<History> result) {
            if (result.getBody() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getBody().getExpiredDate());
                calendar.add(Calendar.SECOND, 1);

                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                alarmIntent.putExtra("historyId", result.getBody().getId());
                alarmIntent.putExtra("type", "forExpired");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis() / 1000, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
                ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
                itemTypeReceiveTask.execute();
            }
            else {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
                ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
                itemTypeReceiveTask.execute();
            }
        }
    }

    private class ItemTypeReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<ItemType>>> {

        @Override
        protected ExceptionAdder<ArrayList<ItemType>> doInBackground(Void... voids) {
            try {
                return new ExceptionAdder<>(ItemTypeRequest.getList());
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<ItemType>> result) {
            if (result.getException() == null) {
                update(result.getBody());
            } else {
                updateToError(result.getException().getMessage());
            }
        }
    }
}