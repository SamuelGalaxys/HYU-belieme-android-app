package hanyang.ac.kr.belieme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.DetailItemActivity;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.Item;
import hanyang.ac.kr.belieme.dataType.ItemStatus;

public class EditItemAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Item> itemList;

    public EditItemAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    public EditItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == Item.VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.edit_item_cell, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(group);
            return itemViewHolder;
        } else if(viewType == Item.VIEW_TYPE_ERROR) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.error_cell, parent, false);
            ErrorViewHolder errorViewHolder = new ErrorViewHolder(group);
            return errorViewHolder;
        } else if(viewType == Item.VIEW_TYPE_PROGRESS) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.progress_cell, parent, false);
            ProgressViewHolder viewHolder = new ProgressViewHolder(group);
            return viewHolder;
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
            Item item = itemList.get(position);
            ((ItemViewHolder) holder).bind(item);
        } else if(holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder)holder;
            errorViewHolder.errorMessage.setText(itemList.get(position).getErrorMessage());
        } else if(holder instanceof ProgressViewHolder) {
            ProgressViewHolder progressViewHolder = (ProgressViewHolder)holder;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    public void update(ArrayList<Item> list) {
        itemList.clear();
        itemList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateToError(String message) {
        itemList.clear();
        itemList.add(Item.getErrorItem(message));
        notifyDataSetChanged();
    }

    public void updateToProgress() {
        itemList.clear();
        itemList.add(Item.getProgressItem());
        notifyDataSetChanged();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNum;
        private TextView content;
        private TextView date;

        private Item item;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);

            itemNum = itemView.findViewById(R.id.editItemCell_textView_itemNum);
            content = itemView.findViewById(R.id.editItemCell_textView_content);
            date = itemView.findViewById(R.id.editItemCell_textView_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailItemActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("hasButton", true);
                    context.startActivity(intent);
                }
            });
        }

        public Item getItem() {
            return item;
        }

        public void bind(Item item) {
            this.item = item;
            itemNum.setText(String.valueOf(item.getNum()));
            date.setVisibility(View.VISIBLE);

            String contentString = "";
            History lastHistory = item.getLastHistory();
            SimpleDateFormat formatter = new SimpleDateFormat("MM월 dd일");
            if (item.getStatus() != ItemStatus.INACTIVE) {
                if (lastHistory != null) {
                    switch (lastHistory.getStatus()) {
                        case USING:
                            date.setText(formatter.format(lastHistory.getResponseTimeStamp()));
                            contentString = lastHistory.getRequesterName() + "님이 대여 중 입니다.";
                            break;
                        case DELAYED:
                            date.setText(formatter.format(lastHistory.getResponseTimeStamp()));
                            contentString = lastHistory.getRequesterName() + "님이 대여 중 입니다(연체됨).";
                            break;
                        case REQUESTED:
                            formatter = new SimpleDateFormat("HH시 mm분");
                            date.setText(formatter.format(lastHistory.getRequestTimeStamp()));
                            contentString = lastHistory.getRequesterName() + "님이 대여 요청하셨습니다.";
                            break;
                        case EXPIRED:
                        case RETURNED:
                            contentString = "대여 가능합니다.";
                            date.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    contentString = "대여 가능합니다.";
                    date.setVisibility(View.GONE);
                }
            }
            else {
                contentString = "비활성화 되었습니다.";
                date.setVisibility(View.GONE);
            }
            content.setText(contentString);
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
}
