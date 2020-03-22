package hanyang.ac.kr.belieme.activity.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import hanyang.ac.kr.belieme.activity.MainActivity;
import hanyang.ac.kr.belieme.adapter.UserStuffAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.R;

public class UserStuffListFragment extends Fragment {
    MainActivity context;
    View layoutView;

    boolean onlyResume;

    private UserStuffAdapter adapter;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_stuff_list, container, false);
        context = (MainActivity)getActivity();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new UserStuffAdapter(context);

        onlyResume = false;

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
        if(onlyResume == false) {
            onlyResume = true;
        }
        else {
            ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
            itemTypeReceiveTask.execute();
        }
    }

    private class ItemTypeReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<ItemType>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ExceptionAdder<ArrayList<ItemType>> doInBackground(Void... voids) {
            publishProgress();
            try {
                return new ExceptionAdder<>(ItemTypeRequest.getList());
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<ItemType>> result) {
            if (result.getException() == null) {
                adapter.update(result.getBody());
            } else {
                adapter.updateToError(result.getException().getMessage());
            }
            context.setChangeModeBtnEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            context.setChangeModeBtnEnabled(false);
            adapter.updateToProgress();
        }
    }
}