package com.example.winterapplication.activity;

import com.example.winterapplication.R;
import com.example.winterapplication.news.AnswersNew;
import com.example.winterapplication.presenter.AnswersPresenter;
import com.example.winterapplication.view.AnswersView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class AnswersActivity extends AppCompatActivity implements AnswersView {

    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private RecyclerView.Adapter mAdapter;
    private TextView BackTextView;
    private FloatingActionButton returnTop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AnswersPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        presenter = new AnswersPresenter(this);

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());

        mRecyclerView = findViewById(R.id.re);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        returnTop = findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (firstVisibleItemPosition == 0) {
                        returnTop.hide();
                    } else {
                        returnTop.show();
                        returnTop.setOnClickListener(view -> recyclerView.scrollToPosition(0));
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    returnTop.hide();
                }
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                presenter.filter(text);
                return true;
            }
        });

        initView();
        initClick();

        presenter.fetchData();
    }

    private void initClick() {
        if (BackTextView != null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(AnswersActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("Answers", "BackTextView is null");
        }
    }

    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayAnswers(List<AnswersNew.Data> data) {
        updateRecyclerViewData(data);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        clearRecyclerViewData();
    }

    @Override
    public void filterList(String text) {
        presenter.filter(text);
    }

    private void updateRecyclerViewData(List<AnswersNew.Data> data) {
        ((Adapter) mAdapter).setData(data);
        mAdapter.notifyDataSetChanged();
    }

    private void clearRecyclerViewData() {
        ((Adapter) mAdapter).clearData();
        mAdapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private List<AnswersNew.Data> data = new ArrayList<>();

        public void setData(List<AnswersNew.Data> newData) {
            data.clear();
            data.addAll(newData);
        }

        public void clearData() {
            data.clear();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_answers, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            AnswersNew.Data datas = data.get(position);
            holder.title.setText(datas.title);
            holder.niceDate.setText(datas.niceDate);
            holder.zan.setText(String.valueOf(datas.zan));
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(AnswersActivity.this, AnswersContentActivity.class);
                intent.putExtra("link", datas.link);
                intent.putExtra("title", datas.title);
                startActivity(intent);
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView niceDate;
            TextView zan;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.textView2);
                niceDate = itemView.findViewById(R.id.textView4);
                zan = itemView.findViewById(R.id.textView5);
            }

        }
    }
}