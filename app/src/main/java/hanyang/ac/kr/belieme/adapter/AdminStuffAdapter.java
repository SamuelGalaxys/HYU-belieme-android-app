package hanyang.ac.kr.belieme.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import hanyang.ac.kr.belieme.activity.DetailItemTypeActivity;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.MainActivity;
import hanyang.ac.kr.belieme.activity.dialog.EditItemTypeDialog;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;

public class AdminStuffAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ItemType> itemTypeList;

    public AdminStuffAdapter() {
    }

    public AdminStuffAdapter(Context context) {
        this.context = context;
        itemTypeList = new ArrayList<>();
    }

    public AdminStuffAdapter(Context context, List<ItemType> itemTypeList) {
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
        }
        else {
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
            itemViewHolder.btn.setVisibility(View.GONE);
        } else if(holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder)holder;
            errorViewHolder.errorMessage.setText(itemTypeList.get(position).getErrorMessage());
        } else if(holder instanceof ProgressViewHolder) {
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

    private class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailItemTypeActivity.class);

                    intent.putExtra("typeId", Integer.parseInt(id.getText().toString()));
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("countAndAmount", count.getText().toString());
                    intent.putExtra("emoji", emoji.getText().toString());
                    intent.putExtra("isAdminMode", true);
                    context.startActivity(intent);
                }
            });

           itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, R.id.item_menu_edit, 1, "편집하기");
            MenuItem Delete = menu.add(Menu.NONE, R.id.item_menu_delete, 2, "삭제하기");
            Edit.setOnMenuItemClickListener(onMenuItemClickListener);
            Delete.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_menu_delete:
                        new MaterialAlertDialogBuilder(context)
                                .setTitle("이 물품을 정말로 삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ItemTypeDeleteTask itemTypeDeleteTask = new ItemTypeDeleteTask();
                                        itemTypeDeleteTask.execute(Integer.valueOf(id.getText().toString()));
                                    }
                                })
                                .setNegativeButton("취소", null)
                                .create()
                                .show();
                        return true;
                    case R.id.item_menu_edit:
                        EditItemTypeDialog dialog = new EditItemTypeDialog(context, AdminStuffAdapter.this, Integer.parseInt(id.getText().toString()), name.getText().toString(), emoji.getText().toString());
                        dialog.showAddItemDialog();
//                        Intent intent = new Intent(context, EditItemActivity.class);
//
//                        intent.putExtra("typeId", Integer.parseInt(id.getText().toString()));
//                        intent.putExtra("name", name.getText().toString());
//                        intent.putExtra("countAndAmount", count.getText().toString());
//                        intent.putExtra("emoji", emoji.getText().toString());
//                        context.startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        };
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

    private class ItemTypeReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<ItemType>>> {

        @Override
        protected ExceptionAdder<ArrayList<ItemType>> doInBackground(Void... voids) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemTypeRequest.getList());
            } catch (Exception e) {
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            ((MainActivity)context).setChangeModeBtnEnabled(false);
            updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<ItemType>> result) {
            if (result.getBody() != null) {
                update(result.getBody());
            } else {
                updateToError(result.getException().getMessage());
            }
            ((MainActivity)context).setChangeModeBtnEnabled(true);
        }
    }

    private class ItemTypeDeleteTask extends AsyncTask<Integer, Void, ExceptionAdder<Void>> {
        ProgressDialog progressDialog = new ProgressDialog(context);

        @Override
        protected ExceptionAdder<Void> doInBackground(Integer... integers) {
            publishProgress();
            try {
                ItemTypeRequest.deactivateItem(integers[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
            return new ExceptionAdder<>();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressDialog.setMessage("진행 중입니다.");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<Void> result) {
            if(result.getException() == null) {
                Toast.makeText(context, "물품이 삭제되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
            ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
            itemTypeReceiveTask.execute();
            progressDialog.cancel();
        }
    }
}

