package hanyang.ac.kr.belieme.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;
import hanyang.ac.kr.belieme.activity.AdminManageActivity;
import hanyang.ac.kr.belieme.dataType.AdminInfo;
import hanyang.ac.kr.belieme.dataType.AdminInfoRequest;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.Permission;

public class AdminAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<AdminInfo> itemList;

    public AdminAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == AdminInfo.VIEW_TYPE_HEADER) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.header_cell, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(group);
            return headerViewHolder;
        } else if (viewType == AdminInfo.VIEW_TYPE_ITEM) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.info_item_cell, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(group);
            return itemViewHolder;
        } else if (viewType == AdminInfo.VIEW_TYPE_ERROR) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.error_cell, parent, false);
            ErrorViewHolder errorViewHolder = new ErrorViewHolder(group);
            return errorViewHolder;
        } else if (viewType == AdminInfo.VIEW_TYPE_PROGRESS) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.progress_cell, parent, false);
            ProgressViewHolder viewHolder = new ProgressViewHolder(group);
            return viewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.headerTitle.setText(itemList.get(position).getPermission().toKoreanString());
        } else if (holder instanceof ItemViewHolder) {
            AdminInfo item = itemList.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.key.setText(item.getName() + "(" + String.valueOf(item.getStudentId()) + ")");
            itemViewHolder.key.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            itemViewHolder.value.setText(item.getPermission().toKoreanString());
            itemViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    if(Globals.userInfo.getPermission() == Permission.MASTER || Globals.userInfo.getPermission() == Permission.DEVELOPER) {
                        if(itemList.get(position).getPermission() == Permission.ADMIN) {
                            MenuItem Edit = menu.add(Menu.NONE, R.id.item_menu_edit, 1, "으뜸 관리자 권한부여하기");
                            MenuItem Delete = menu.add(Menu.NONE, R.id.item_menu_delete, 2, "관리자 권한제거하기");
                            Edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    new MaterialAlertDialogBuilder(context)
                                            .setTitle("정말로 이 관리자에게 으뜸 관리자 권한을 부여하겠습니까?")
                                            .setMessage("으뜸 관리자는 다른 관리자들을 관리할 권한을 갖습니다.")
                                            .setPositiveButton("권한 부여하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SetPermissionMasterTask setPermissionMasterTask = new SetPermissionMasterTask();
                                                    setPermissionMasterTask.execute(itemList.get(position).getStudentId());
                                                }
                                            })
                                            .setNegativeButton("취소", null)
                                            .create()
                                            .show();
                                    return true;
                                }
                            });
                            Delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    new MaterialAlertDialogBuilder(context)
                                            .setTitle("정말로 이 관리자의 권한을 제거하시겠습니까?")
                                            .setPositiveButton("제거하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DeleteAdminTask deleteAdminTask = new DeleteAdminTask();
                                                    deleteAdminTask.execute(itemList.get(position).getStudentId());
                                                }
                                            })
                                            .setNegativeButton("취소", null)
                                            .create()
                                            .show();
                                    return true;
                                }
                            });
                        } else if(itemList.get(position).getPermission() == Permission.MASTER && itemList.get(position).getStudentId() == Integer.parseInt(Globals.userInfo.getStudentId())) {
                            MenuItem Edit = menu.add(Menu.NONE, R.id.item_menu_edit, 1, "으뜸 관리자 권한포기하기");
                            Edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    new MaterialAlertDialogBuilder(context)
                                            .setTitle("으뜸 관리자 권한을 포기하시겠습니까?")
                                            .setMessage("포기하시면 되돌릴 수 없습니다.")
                                            .setPositiveButton("포기하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SetPermissionAdminTask setPermissionAdminTask = new SetPermissionAdminTask();
                                                    setPermissionAdminTask.execute(itemList.get(position).getStudentId());
                                                }
                                            })
                                            .setNegativeButton("취소", null)
                                            .create()
                                            .show();
                                    return true;
                                }
                            });
                        }
                    }
                }
            });

        } else if (holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorViewHolder = (ErrorViewHolder) holder;
            errorViewHolder.errorMessage.setText(itemList.get(position).getName());//name을 error message로 재활용
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
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

    public ArrayList<AdminInfo> sortWithPermission(ArrayList<AdminInfo> list) {
        ArrayList<AdminInfo> developerList = new ArrayList<>();
        ArrayList<AdminInfo> masterList = new ArrayList<>();
        ArrayList<AdminInfo> adminList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            AdminInfo adminInfo = list.get(i);
            switch (adminInfo.getPermission()) {
                case DEVELOPER:
                    developerList.add(adminInfo);
                    break;
                case MASTER:
                    masterList.add(adminInfo);
                    break;
                case ADMIN:
                    adminList.add(adminInfo);
                    break;
                default:
                    break;
            }
        }
        ArrayList<AdminInfo> result = new ArrayList<>();
//        result.addAll(developerList);
        result.addAll(masterList);
        result.addAll(adminList);

        return result;
    }

    public ArrayList<AdminInfo> addHeaders(ArrayList<AdminInfo> list) {
        ArrayList<AdminInfo> sortedList = sortWithPermission(list);
        ArrayList<AdminInfo> listWithHeaders = new ArrayList<>();
        AdminInfo firstOfList = new AdminInfo();
        firstOfList.setPermission(Permission.MASTER);
        firstOfList.setViewType(AdminInfo.VIEW_TYPE_HEADER);
        listWithHeaders.add(firstOfList);

        Permission prevPermission = Permission.MASTER;
        int i = 0;
        while(prevPermission.nextPermission() != Permission.USER || i < sortedList.size()) {
            if(i >= sortedList.size()) {
                prevPermission = prevPermission.nextPermission();
                AdminInfo header = new AdminInfo();
                header.setViewType(AdminInfo.VIEW_TYPE_HEADER);
                header.setPermission(prevPermission);
                listWithHeaders.add(header);
            }
            else if(sortedList.get(i).getPermission() == prevPermission) {
                sortedList.get(i).setViewType(History.VIEW_TYPE_ITEM);
                listWithHeaders.add(sortedList.get(i));
                i++;
            }
            else if (sortedList.get(i).getPermission() != prevPermission) {
                prevPermission = prevPermission.nextPermission();
                AdminInfo header = new AdminInfo();
                header.setViewType(AdminInfo.VIEW_TYPE_HEADER);
                header.setPermission(prevPermission);
                listWithHeaders.add(header);
            }
        }
        return listWithHeaders;
    }

    public void update(ArrayList<AdminInfo> list) {
        itemList.clear();
        itemList.addAll(addHeaders(list));
        notifyDataSetChanged();
    }

    public void updateToError(String message) {
        itemList.clear();
        itemList.add(AdminInfo.getErrorAdminInfo(message));
        notifyDataSetChanged();
    }

    public void updateToProgress() {
        itemList.clear();
        itemList.add(AdminInfo.getProgressAdminInfo());
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
        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class SetPermissionMasterTask extends AsyncTask<Integer, Void, ExceptionAdder<ArrayList<AdminInfo>>> {
        @Override
        protected ExceptionAdder<ArrayList<AdminInfo>> doInBackground(Integer... integers) {
            publishProgress();
            try {
                return new ExceptionAdder<>(AdminInfoRequest.setPermissionMaster(integers[0]));
            } catch (Exception e) {
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<AdminInfo>> arrayListExceptionAdder) {
            if(arrayListExceptionAdder.getException() == null) {
                Globals.adminInfo = arrayListExceptionAdder.getBody();
                Globals.userInfo.setPermission(Globals.getPermission(Integer.parseInt(Globals.userInfo.getStudentId())));
                if(context instanceof AdminManageActivity) {
                    ((AdminManageActivity)context).setBtnVisibility();
                }
                update(Globals.adminInfo);
                Toast.makeText(context, "권한을 부여했습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, arrayListExceptionAdder.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SetPermissionAdminTask extends AsyncTask<Integer, Void, ExceptionAdder<ArrayList<AdminInfo>>> {
        @Override
        protected ExceptionAdder<ArrayList<AdminInfo>> doInBackground(Integer... integers) {
            publishProgress();
            try {
                return new ExceptionAdder<>(AdminInfoRequest.setPermissionAdmin(integers[0]));
            } catch (Exception e) {
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<AdminInfo>> arrayListExceptionAdder) {
            if(arrayListExceptionAdder.getException() == null) {
                Globals.adminInfo = arrayListExceptionAdder.getBody();
                Globals.userInfo.setPermission(Globals.getPermission(Integer.parseInt(Globals.userInfo.getStudentId())));
                if(context instanceof AdminManageActivity) {
                    ((AdminManageActivity)context).setBtnVisibility();
                }
                update(Globals.adminInfo);
                Toast.makeText(context, "으뜸 관리자 권한을 포기하였습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, arrayListExceptionAdder.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DeleteAdminTask extends AsyncTask<Integer, Void, ExceptionAdder<ArrayList<AdminInfo>>> {
        @Override
        protected ExceptionAdder<ArrayList<AdminInfo>> doInBackground(Integer... integers) {
            publishProgress();
            try {
                return new ExceptionAdder<>(AdminInfoRequest.deleteAdmin(integers[0]));
            } catch (Exception e) {
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            updateToProgress();
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<AdminInfo>> arrayListExceptionAdder) {
            if(arrayListExceptionAdder.getException() == null) {
                Globals.adminInfo = arrayListExceptionAdder.getBody();
                Globals.userInfo.setPermission(Globals.getPermission(Integer.parseInt(Globals.userInfo.getStudentId())));
                if(context instanceof AdminManageActivity) {
                    ((AdminManageActivity)context).setBtnVisibility();
                }
                update(Globals.adminInfo);
                Toast.makeText(context, "권한이 제거되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, arrayListExceptionAdder.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
