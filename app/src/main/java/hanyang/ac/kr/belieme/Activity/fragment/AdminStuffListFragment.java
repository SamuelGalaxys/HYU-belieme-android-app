package hanyang.ac.kr.belieme.Activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Activity.AddItemActivity;
import hanyang.ac.kr.belieme.Activity.EditItemActivity;
import hanyang.ac.kr.belieme.DataType.ItemType;
import hanyang.ac.kr.belieme.DataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.R;

public class AdminStuffListFragment extends Fragment {
    ArrayList<Itemholder> itemholders = new ArrayList<>();
    ArrayList<ItemType> itemTypes = new ArrayList<ItemType>();
    View layoutView;

    private View tmpView;

    public class Itemholder extends LinearLayout {
        public Itemholder(Context context) {
            super(context);

            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.admin_item_cell, this, true);
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
            itemTypes = result;
            updateAll(itemTypes);
        }
    }



    private class ItemTypeDeleteTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            try {
                ItemTypeRequest.deleteItem(integers[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return integers[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            for(int i = 0; i < itemTypes.size(); i++) {
                if(itemTypes.get(i).getId() == result.intValue()) {
                    itemTypes.remove(i);
                }
            }
            updateAll(itemTypes);
        }
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_admin_stuff_list, container, false);

        ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
        itemTypeReceiveTask.execute();

        ImageView addBtn = layoutView.findViewById(R.id.adminStuff_button_addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
        itemTypeReceiveTask.execute();
        System.out.println("onResume");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        tmpView = view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView nameView;
        TextView countView;
        TextView emojiView;
        TextView idView;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.item_menu_delete:
                idView = tmpView.findViewById(R.id.adminItemCell_textView_id);
                nameView = tmpView.findViewById(R.id.adminItemCell_textView_name);
                ItemTypeDeleteTask itemTypeDeleteTask = new ItemTypeDeleteTask();
                itemTypeDeleteTask.execute(Integer.valueOf(idView.getText().toString()));
                return true;
            case R.id.item_menu_edit:
                Intent intent = new Intent(getContext(), EditItemActivity.class);
                idView = tmpView.findViewById(R.id.adminItemCell_textView_id);
                nameView = tmpView.findViewById(R.id.adminItemCell_textView_name);
                countView = tmpView.findViewById(R.id.adminItemCell_textView_count);
                emojiView = tmpView.findViewById(R.id.adminItemCell_textView_itemEmoji);

                intent.putExtra("typeId", Integer.parseInt(idView.getText().toString()));
                intent.putExtra("name", nameView.getText().toString());
                intent.putExtra("countAndAmount", countView.getText().toString());
                intent.putExtra("emoji", emojiView.getText().toString());
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void addItem(ItemType itemType) {
        final Itemholder itemholder = new Itemholder(getActivity());
        LinearLayout linearLayout;
        if(itemType.getCount() > 0) {
            linearLayout = (LinearLayout)(layoutView.findViewById(R.id.adminStuff_linear_possible));
        }
        else {
            linearLayout = (LinearLayout)(layoutView.findViewById(R.id.adminStuff_linear_impossible));
        }
        linearLayout.addView(itemholder);
        TextView itemId = itemholder.findViewById(R.id.adminItemCell_textView_id);
        TextView itemName = itemholder.findViewById(R.id.adminItemCell_textView_name);
        TextView itemCount = itemholder.findViewById(R.id.adminItemCell_textView_count);
        TextView itemEmoji = itemholder.findViewById(R.id.adminItemCell_textView_itemEmoji);

        itemId.setText(String.valueOf(itemType.getId()));
        itemName.setText(itemType.getName());
        itemCount.setText(itemType.getAmount() + "개 중" + " " + itemType.getCount() + "개 사용 가능");
        itemEmoji.setText(itemType.getEmoji());

        registerForContextMenu(itemholder);
        itemholders.add(itemholder);
    }

    public void updateAll(ArrayList<ItemType> itemTypes) {
        while(itemholders.size() != 0) {
            ((LinearLayout)itemholders.get(0).getParent()).removeView(itemholders.get(0));
            itemholders.remove(0);
        }
        for(int i = 0; i < itemTypes.size(); i++) {
            addItem(itemTypes.get(i));
        }
    }
}

//Item count ++ and -- function (In addItem function)

//        minusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView itemName = itemholder.findViewById(R.id.item_textView_name);
//                TextView itemCount = itemholder.findViewById(R.id.item_textView_count);
//                TextView itemEmoji = itemholder.findViewById(R.id.item_textView_itemEmoji);
//                String tmp = itemCount.getText().toString().replace("개 사용 가능", "");
//                tmp = tmp.replace("개 중", "");
//                boolean isAmount = true;
//                String tmpCount = new String("");
//                String tmpAmount = new String("");
//                for(int i = 0; i < tmp.length(); i++) {
//                    if (tmp.charAt(i) == ' ') {
//                        isAmount = false;
//                    } else if (isAmount) {
//                        tmpAmount += tmp.charAt(i);
//                    } else {
//                        tmpCount += tmp.charAt(i);
//                    }
//                }
//                System.out.println(tmpAmount);
//                ItemType itemType = new ItemType(itemName.getText().toString(),
//                        itemEmoji.getText().toString(),
//                        Integer.parseInt(tmpCount),
//                        Integer.parseInt(tmpAmount)
//                );
//
//                itemType.decreaseCount();
//                PostTask postTask = new PostTask();
//                postTask.execute(itemType);
//            }
//        });
//
//        plusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView itemName = itemholder.findViewById(R.id.item_textView_name);
//                TextView itemCount = itemholder.findViewById(R.id.item_textView_count);
//                TextView itemEmoji = itemholder.findViewById(R.id.item_textView_itemEmoji);
//                String tmp = itemCount.getText().toString().replace("개 사용 가능", "");
//                tmp = tmp.replace("개 중", "");
//                boolean isAmount = true;
//                String tmpCount = new String("");
//                String tmpAmount = new String("");
//                for(int i = 0; i < tmp.length(); i++) {
//                    if (tmp.charAt(i) == ' ') {
//                        isAmount = false;
//                    } else if (isAmount) {
//                        tmpAmount += tmp.charAt(i);
//                    } else {
//                        tmpCount += tmp.charAt(i);
//                    }
//                }
//                ItemType itemType = new ItemType(itemName.getText().toString(),
//                        itemEmoji.getText().toString(),
//                        Integer.parseInt(tmpCount),
//                        Integer.parseInt(tmpAmount)
//                );
//
//                itemType.increaseCount();
//                PostTask postTask = new PostTask();
//                postTask.execute(itemType);
//            }
//        });
//
//private class ItemTypePostTask extends AsyncTask<ItemType, Void, ItemType> {
//
//    @Override
//    protected ItemType doInBackground(ItemType... itemTypes) {
//        try {
//            return ItemTypeRequest.addItem(itemTypes[0]);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    protected void onPostExecute(ItemType result) {
//        for(int i = 0; i < itemTypes.size(); i++) {
//            if(itemTypes.get(i).getName().equals(result.getName())) {
//                itemTypes.set(i, result);
//            }
//        }
//        updateAll(itemTypes);
//    }
//}