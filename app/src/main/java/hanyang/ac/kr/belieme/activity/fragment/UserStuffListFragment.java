package hanyang.ac.kr.belieme.activity.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.adapter.UserStuffAdapter;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.Globals;
import hanyang.ac.kr.belieme.R;

public class UserStuffListFragment extends Fragment {
    Context context;
    View layoutView;

    private UserStuffAdapter adapter;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_stuff_list, container, false);
        context = getActivity();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new UserStuffAdapter(context);

        RecyclerView recyclerView = layoutView.findViewById(R.id.stuff_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ItemTypeReceiveTask receiveTask = new ItemTypeReceiveTask();
        receiveTask.execute();

        ImageView addBtn = layoutView.findViewById(R.id.stuff_button_addBtn);
        addBtn.setVisibility(View.GONE);

        return layoutView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
        itemTypeReceiveTask.execute();
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
            adapter.update(result);
        }
    }
}