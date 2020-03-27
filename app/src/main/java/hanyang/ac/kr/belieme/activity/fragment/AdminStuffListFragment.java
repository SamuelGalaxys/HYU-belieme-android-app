package hanyang.ac.kr.belieme.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.activity.AddItemActivity;
import hanyang.ac.kr.belieme.activity.MainActivity;
import hanyang.ac.kr.belieme.adapter.AdminStuffAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.R;

public class AdminStuffListFragment extends Fragment {
    private View layoutView;
    private MainActivity context;
    private AdminStuffAdapter adapter;
    private boolean onlyResume;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_stuff_list, container, false);

        context = (MainActivity)getActivity();

        context.setChangeModeBtnVisibility(View.VISIBLE);

        onlyResume = false;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new AdminStuffAdapter(context);

        RecyclerView recyclerView = layoutView.findViewById(R.id.stuff_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
        itemTypeReceiveTask.execute();

        ImageView addBtn = layoutView.findViewById(R.id.stuff_button_addBtn);
        addBtn.setVisibility(View.VISIBLE);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddItemActivity.class);
                startActivity(intent);
            }
        });

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
            if (result.getBody() != null) {
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