package hanyang.ac.kr.belieme.Activity.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import hanyang.ac.kr.belieme.DataType.History;
import hanyang.ac.kr.belieme.DataType.HistoryRequest;
import hanyang.ac.kr.belieme.DataType.HistoryStatus;
import hanyang.ac.kr.belieme.DataType.ItemType;
import hanyang.ac.kr.belieme.DataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;

public class UserHistoryFragment extends Fragment {
    ArrayList<ItemHolder> itemHolders = new ArrayList<>();
    ArrayList<History> items = new ArrayList<History>();
    View layoutView;

    private class ItemHolder extends LinearLayout {
        public ItemHolder(Context context) {
            super(context);

            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.user_history_cell, this, true);
        }
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_user_history, container, false);

        HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
        historyReceiveTask.execute();
        return layoutView;
    }

    public void addItem(final History item) {
        final ItemHolder itemHolder = new ItemHolder(getActivity());
        LinearLayout linearLayout = null;

        TextView itemName = itemHolder.findViewById(R.id.userHistoryCell_textView_item);
        TextView date = itemHolder.findViewById(R.id.userHistoryCell_textView_date);
        Button btn = itemHolder.findViewById(R.id.userHistoryCell_btn_btn);

        switch(item.getStatus()) {
            case REQUESTED: {
                linearLayout = (LinearLayout) (layoutView.findViewById(R.id.userHistory_linear_requested));
                itemName.setTextColor(getResources().getColor(R.color.colorHanyangBlue));
                date.setTextColor(getResources().getColor(R.color.colorHanyangBlue));
                btn.setText("취소");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setStatus(HistoryStatus.EXPIRED);
                        HistoryEditTask historyEditTask = new HistoryEditTask();
                        historyEditTask.execute(item);
                    }
                });
                break;
            }
            case USING: {
                linearLayout = (LinearLayout)(layoutView.findViewById(R.id.userHistory_linear_used));
                itemName.setTextColor(getResources().getColor(R.color.colorUsedGreen));
                date.setTextColor(getResources().getColor(R.color.colorUsedGreen));
                btn.setVisibility(View.INVISIBLE);
                break;
            }
            case DELAYED: {
                linearLayout = (LinearLayout)(layoutView.findViewById(R.id.userHistory_linear_delayed));
                itemName.setTextColor(getResources().getColor(R.color.colorWarnRed));
                date.setTextColor(getResources().getColor(R.color.colorWarnRed));
                btn.setVisibility(View.INVISIBLE);
                break;
            }
            case RETURNED: {
                linearLayout = (LinearLayout) (layoutView.findViewById(R.id.userHistory_linear_returned));
                itemName.setTextColor(getResources().getColor(R.color.colorDisableGray));
                date.setTextColor(getResources().getColor(R.color.colorDisableGray));
                btn.setVisibility(View.INVISIBLE);
                break;
            }
            case EXPIRED: {
                linearLayout = (LinearLayout) (layoutView.findViewById(R.id.userHistory_linear_expired));
                itemName.setTextColor(getResources().getColor(R.color.colorDisableGray));
                date.setTextColor(getResources().getColor(R.color.colorDisableGray));
                btn.setVisibility(View.INVISIBLE);
            }
            default: {
                break;
            }
        }

        if(item.getStatus() != HistoryStatus.ERROR) {
            ItemTypeGetTask itemTypeGetTask = new ItemTypeGetTask(itemName, item.getItemNum());
            itemTypeGetTask.execute(item.getTypeId());

            date.setText(item.dateToString());

            linearLayout.addView(itemHolder);

            itemHolders.add(itemHolder);
        }
    }

    public void updateAll(ArrayList<History> items) {
        while(itemHolders.size() != 0) {
            ((LinearLayout)itemHolders.get(0).getParent()).removeView(itemHolders.get(0));
            itemHolders.remove(0);
        }
        for(int i = 0; i < items.size();i++) {
            addItem(items.get(i));
            System.out.println("j : " + i);
        }
    }

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ArrayList<History>> {
        @Override
        protected ArrayList<History> doInBackground(Void... voids) {
            try {
                return HistoryRequest.getListByRequesterId(Integer.parseInt(Globals.userInfo.getStudentId()));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<History> result) {
            items = result;
            updateAll(items);
        }
    }

    private class HistoryEditTask extends AsyncTask<History, Void, Void> {

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

    private class ItemTypeGetTask extends AsyncTask <Integer, Void, ItemType> {

        TextView view;
        int itemNum;

        public ItemTypeGetTask(TextView view, int itemNum) {
            this.view = view;
            this.itemNum = itemNum;
        }

        @Override
        protected ItemType doInBackground(Integer... integers) {
            try {
                return ItemTypeRequest.getItem(integers[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ItemType result) {
            view.setText(result.getName() + " " + itemNum);
        }
    }
}
