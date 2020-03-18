package hanyang.ac.kr.belieme.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanyang.ac.kr.belieme.R;

public class InfoAdapter extends RecyclerView.Adapter {
    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_ERROR = 2;

    private Context context;
    private List<Pair<String, String>> itemList;
    private View.OnClickListener onClickListener;


    public InfoAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == VIEW_TYPE_HEADER) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.header_cell, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(group);
            return headerViewHolder;
        } else if(viewType == VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.info_item_cell, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(group);
            return itemViewHolder;
        } else if(viewType == VIEW_TYPE_ERROR) {
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
            headerViewHolder.headerTitle.setText(itemList.get(position).second);
        } else if(holder instanceof  ItemViewHolder) {
            Pair<String, String> item = itemList.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
            itemViewHolder.key.setText(item.first);
            itemViewHolder.value.setText(item.second);
        } else if(holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder)holder;
            errorViewHolder.errorMessage.setText(itemList.get(position).second);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(itemList.get(position).first.equals("__HEADER__")) {
            return VIEW_TYPE_HEADER;
        }
        else if(itemList.get(position).first.equals("__ERROR__")) {
            return VIEW_TYPE_ERROR;
        }
        else {
            return VIEW_TYPE_ITEM;
        }
    }

    public void update(ArrayList<Pair<String, String>> list) {
        itemList.clear();
        itemList.addAll(list);
        notifyDataSetChanged();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.listHeaderCell_textView);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView key;
        TextView value;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.infoCell_textView_key);
            value = itemView.findViewById(R.id.infoCell_textView_value);

            itemView.setOnClickListener(onClickListener);
        }
    }

    private class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView errorMessage;
        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            errorMessage = itemView.findViewById(R.id.errorCell_textView_message);
        }
    }
}
