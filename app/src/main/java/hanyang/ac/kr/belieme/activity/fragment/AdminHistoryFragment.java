package hanyang.ac.kr.belieme.activity.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.adapter.AdminHistoryAdapter;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.R;

public class AdminHistoryFragment extends Fragment {
    private Context context;
    private View layoutView;

    private AdminHistoryAdapter adapter;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = inflater.inflate(R.layout.fragment_history, container, false);
        context = getContext();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new AdminHistoryAdapter(context);

        RecyclerView recyclerView = layoutView.findViewById(R.id.history_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        HistoryReceiveTask historyReceiveTask = new HistoryReceiveTask();
        historyReceiveTask.execute();

        return layoutView;
    }

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ArrayList<History>> {
        @Override
        protected ArrayList<History> doInBackground(Void... voids) {
            try {
                return HistoryRequest.getList();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<History> result) {
            adapter.update(result);
        }
    }
}

