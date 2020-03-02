package hanyang.ac.kr.belieme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.dataType.Setting;

public class SettingAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Setting> settingList;

    public SettingAdapter(Context context) {
        this.context = context;
        settingList = new ArrayList<>();
    }

    public SettingAdapter(Context context, ArrayList<Setting> settingList) {
        this.context = context;
        this.settingList = settingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == Setting.VIEW_TYPE_HEADER) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.header_cell, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(group);
            return headerViewHolder;
        } else if(viewType == Setting.VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.setting_cell, parent, false);
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
            //make header
        } else if(holder instanceof  ItemViewHolder) {
            final Setting setting = settingList.get(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
            itemViewHolder.settingTitle.setText(setting.getTitle());
            itemViewHolder.setOnClickListener(settingList.get(position).getOnClickListener());
        }
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return settingList.get(position).getViewType();
    }

    public void update(ArrayList<Setting> list) {
        settingList.clear();
        settingList.addAll(list);
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
        TextView settingTitle;
        private View.OnClickListener onClickListener;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            settingTitle = itemView.findViewById(R.id.settingCell_textView_title);
            itemView.setOnClickListener(onClickListener);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(onClickListener);
        }
    }
}
