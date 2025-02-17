package com.example.winterapplication.activity;

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

import com.example.winterapplication.R;
import com.example.winterapplication.model.UsefulWebsiteModel;
import com.example.winterapplication.news.WebNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.winterapplication.presenter.UsefulWebsitePresenter;
import com.example.winterapplication.view.UsefulWebsiteView;

import java.util.ArrayList;
import java.util.List;

public class UsefulWebsiteActivity extends AppCompatActivity implements UsefulWebsiteView {
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private RecyclerView.Adapter mAdapter;
    private TextView BackTextView;
    private FloatingActionButton returnTop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UsefulWebsitePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usefulwebsite);

        UsefulWebsiteModel model = new UsefulWebsiteModel();
        presenter = new UsefulWebsitePresenter(this, model);

        initViews();
        setupListeners();

        presenter.fetchData();
    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        mRecyclerView = findViewById(R.id.re);
        returnTop = findViewById(R.id.fab);
        searchView = findViewById(R.id.searchView);
        BackTextView = findViewById(R.id.net_back);

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());

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
                        returnTop.setOnClickListener(v -> recyclerView.scrollToPosition(0));
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    returnTop.hide();
                }
            }
        });

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.filter(newText);
                return true;
            }
        });

        if (BackTextView != null) {
            BackTextView.setOnClickListener(v -> presenter.onBackButtonClick());
        } else {
            Log.e("UsefulWebsite", "BackTextView is null");
        }
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
    public void displayData(List<WebNew.Data> data) {
        ((Adapter) mAdapter).setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        ((Adapter) mAdapter).clearData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        presenter.filter(text);
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(UsefulWebsiteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToUsefulWebsiteContent(String link, String name) {
        Intent intent = new Intent(UsefulWebsiteActivity.this, UsefulWebsiteContentActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private List<WebNew.Data> data = new ArrayList<>();

        public void setData(List<WebNew.Data> newData) {
            data.clear();
            data.addAll(newData);
        }

        public void clearData() {
            data.clear();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_usefulwebsite, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            WebNew.Data datas = data.get(position);
            holder.name.setText(datas.name);
            holder.itemView.setOnClickListener(v -> presenter.onItemClick(datas));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rv2textView3);
            }
        }
    }
}