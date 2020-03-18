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
import hanyang.ac.kr.belieme.adapter.AdminStuffAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.ItemType;
import hanyang.ac.kr.belieme.dataType.ItemTypeRequest;
import hanyang.ac.kr.belieme.R;

public class AdminStuffListFragment extends Fragment {
    View layoutView;

    Context context;
    private AdminStuffAdapter adapter;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_stuff_list, container, false);

        context = getActivity();

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
        ItemTypeReceiveTask itemTypeReceiveTask = new ItemTypeReceiveTask();
        itemTypeReceiveTask.execute();
    }

    private class ItemTypeReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<ItemType>>> {

        @Override
        protected ExceptionAdder<ArrayList<ItemType>> doInBackground(Void... voids) {
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
                ArrayList<ItemType> error = new ArrayList<>();
                error.add(new ItemType(result.getException().getMessage()));
                adapter.update(error);
            }
        }
    }
}