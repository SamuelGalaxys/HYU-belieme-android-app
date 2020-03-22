package hanyang.ac.kr.belieme.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Exception.InternalServerException;
import hanyang.ac.kr.belieme.activity.MainActivity;
import hanyang.ac.kr.belieme.adapter.AdminHistoryAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.R;

public class AdminHistoryFragment extends Fragment {
    private MainActivity context;
    private View layoutView;

    private AdminHistoryAdapter adapter;

    private boolean onlyResume;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_history, container, false);
        context = (MainActivity)getActivity();

        onlyResume = false;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new AdminHistoryAdapter(context);

        RecyclerView recyclerView = layoutView.findViewById(R.id.history_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
        historyReceiveTask.execute();

        return layoutView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onlyResume == false) {
            onlyResume = true;
        }
        else {
            HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
            historyReceiveTask.execute();
        }
    }

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<History>>> {
        @Override
        protected ExceptionAdder<ArrayList<History>> doInBackground(Void... voids) {
            publishProgress();
            try {
                return new ExceptionAdder<>(HistoryRequest.getList());
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<ArrayList<History>> result) {
            if(result.getBody() != null) {
                adapter.update(result.getBody());
            }
            else {
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

