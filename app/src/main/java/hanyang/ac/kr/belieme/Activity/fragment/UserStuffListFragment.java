package hanyang.ac.kr.belieme.Activity.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import hanyang.ac.kr.belieme.DataType.History;
import hanyang.ac.kr.belieme.DataType.HistoryRequest;
import hanyang.ac.kr.belieme.DataType.HistoryStatus;
import hanyang.ac.kr.belieme.DataType.Item;
import hanyang.ac.kr.belieme.DataType.ItemRequest;
import hanyang.ac.kr.belieme.DataType.ItemType;
import hanyang.ac.kr.belieme.DataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;

public class UserStuffListFragment extends Fragment {
    ArrayList<Itemholder> itemholders = new ArrayList<>();
    ArrayList<ItemType> itemTypes = new ArrayList<ItemType>();
    View layoutView;
    Button testBtn;

    private class Itemholder extends LinearLayout {
        public Itemholder(Context context) {
            super(context);

            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.user_item_cell, this, true);
        }
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_user_stuff_list, container, false);
        testBtn = layoutView.findViewById(R.id.test_btn);

        ItemTypeReceiveTask receiveTask = new ItemTypeReceiveTask();
        receiveTask.execute();
        return layoutView;
    }

    public void addItem(final ItemType itemType) {
        final Itemholder itemholder = new Itemholder(getActivity());
        LinearLayout linearLayout;
        TextView itemName = itemholder.findViewById(R.id.userItemCell_textView_name);
        final TextView itemCount = itemholder.findViewById(R.id.userItemCell_textView_count);
        TextView itemEmoji = itemholder.findViewById(R.id.userItemCell_textView_itemEmoji);
        Button rentButton = itemholder.findViewById(R.id.userItemCell_btn_rent);

        itemName.setText(itemType.getName());
        itemCount.setText(itemType.getAmount() + "개 중" + " " + itemType.getCount() + "개 사용 가능");
        itemEmoji.setText(itemType.getEmoji());


        if(itemType.getCount() > 0) {//서버에서 불러오는걸로 바꾸기?
            linearLayout = (LinearLayout)(layoutView.findViewById(R.id.userStuff_linear_possible));
            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView itemName = itemholder.findViewById(R.id.userItemCell_textView_name);

                    HistoryPostTask historyPostTask = new HistoryPostTask();
                    historyPostTask.execute(new History(
                            itemType.getId(),
                            0,
                            Globals.userInfo.getName(),
                            Integer.parseInt(Globals.userInfo.getStudentId()))
                    );
                }
            });
        }
        else {
            linearLayout = (LinearLayout)(layoutView.findViewById(R.id.userStuff_linear_impossible));
        }

        linearLayout.addView(itemholder);
        itemholders.add(itemholder);
    }

    public void updateAll(ArrayList<ItemType> itemTypes) {
        Log.d("update", String.valueOf(itemTypes.size()));
        while(itemholders.size() != 0) {
            ((LinearLayout)itemholders.get(0).getParent()).removeView(itemholders.get(0));
            itemholders.remove(0);
        }
        for(int i = 0; i < itemTypes.size(); i++) {
            addItem(itemTypes.get(i));
            System.out.println("j : " + i);
        }
    }

    private class ItemTypeReceiveTask extends AsyncTask<Void, Void, ArrayList<ItemType>> {

        @Override
        protected ArrayList<ItemType> doInBackground(Void... voids) {
            try {
                ArrayList<ItemType> result = ItemTypeRequest.getList();
                if(result == null) {
                    result = new ArrayList<>();
                }
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemType> result) {
            itemTypes = result;
            updateAll(itemTypes);
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
}