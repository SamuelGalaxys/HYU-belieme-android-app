package hanyang.ac.kr.belieme.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import hanyang.ac.kr.belieme.dataType.Item;
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
        if(viewType == Item.VIEW_TYPE_HEADER) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.header_cell, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(group);
            return headerViewHolder;
        } else if(viewType == Item.VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup)inflater.inflate(R.layout.stuff_cell, parent, false);
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
            headerViewHolder.headerTitle.setText(itemTypeList.get(position).getStatusKorean());
        } else if(holder instanceof  ItemViewHolder) {
            final ItemType itemType = itemTypeList.get(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
            itemViewHolder.id.setText(String.valueOf(itemType.getId()));
            itemViewHolder.emoji.setText(itemType.getEmoji());
            itemViewHolder.name.setText(itemType.getName());
            itemViewHolder.count.setText(itemType.getAmount() + "개 중" + " " + itemType.getCount() + "개 사용 가능");

            switch (itemType.getStatusKorean()) {
                case "대여 가능" :
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
                case "대여 불가" :
                    itemViewHolder.btn.setVisibility(View.GONE);
                    break;
                default: {
                    break;
                }
            }

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
        itemTypeList.addAll(addHeaders(list));
        notifyDataSetChanged();
    }

    public static ArrayList<ItemType> sortWithStatus(ArrayList<ItemType> list) {
        ArrayList<ItemType> usableList = new ArrayList<>();
        ArrayList<ItemType> unusableList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            ItemType itemType = list.get(i);
            if(itemType.getCount() > 0 ) {
                usableList.add(itemType);
            }
            else {
                unusableList.add(itemType);
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
        ItemType firstOfList = new ItemType();
        firstOfList.setCount(1);//usable header
        firstOfList.setViewType(ItemType.VIEW_TYPE_HEADER);
        listWithHeaders.add(firstOfList);

        boolean unusableHeaderAdded = false;
        for(int i = 0; i < sortedList.size(); i++) {
            if(!unusableHeaderAdded && sortedList.get(i).getCount() <= 0) {
                ItemType header = new ItemType();
                header.setViewType(ItemType.VIEW_TYPE_HEADER);
                header.setCount(0);//unusable header
                listWithHeaders.add(header);
                unusableHeaderAdded = true;
                i--;
            }
            else {
                sortedList.get(i).setViewType(ItemType.VIEW_TYPE_ITEM);
                listWithHeaders.add(sortedList.get(i));
            }
        }
        if(!unusableHeaderAdded) {
            ItemType header = new ItemType();
            header.setViewType(ItemType.VIEW_TYPE_HEADER);
            header.setCount(0);//unusable header
            listWithHeaders.add(header);
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

    private class HistoryPostTask extends AsyncTask<History, Void, History> {

        @Override
        protected History doInBackground(History... items) {
            try {
                return HistoryRequest.addItem(items[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(History result) {
            ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
            itemTypeReceiveTask.execute();
        }
    }

    private class ItemTypeReceiveTask extends AsyncTask<Void, Void, ArrayList<ItemType>> {

        @Override
        protected ArrayList<ItemType> doInBackground(Void... voids) {
            try {
                return ItemTypeRequest.getList();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemType> result) {
            update(result);
        }
    }
}