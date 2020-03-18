package hanyang.ac.kr.belieme.activity.fragment;

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
import hanyang.ac.kr.belieme.adapter.AdminHistoryAdapter;
import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
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

    private class HistoryReceiveTask extends AsyncTask<Void, Void, ExceptionAdder<ArrayList<History>>> {
        @Override
        protected ExceptionAdder<ArrayList<History>> doInBackground(Void... voids) {
            try {
                return new ExceptionAdder<>(HistoryRequest.getList());
            } catch (JSONException e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            } catch (IOException e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            } catch (InternalServerException e) {
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
                ArrayList<History> error = new ArrayList<>();
                error.add(new History(result.getException().getMessage()));
                adapter.update(error);
            }
        }
    }
}

