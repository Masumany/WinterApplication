package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.winterapplication.R;
import com.example.winterapplication.news.StudyNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.winterapplication.presenter.StudyPresenter;
import com.example.winterapplication.model.StudyModel;
import com.example.winterapplication.view.StudyView;

import java.util.ArrayList;
import java.util.List;

public class StudyActivity extends AppCompatActivity implements StudyView {
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private RecyclerView.Adapter mAdapter;
    private TextView BackTextView;
    private FloatingActionButton returnTop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StudyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        StudyModel model = new StudyModel();
        presenter = new StudyPresenter(this, model);

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
            Log.e("StudyActivity", "BackTextView is null");
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
    public void displayData(List<StudyNew.Data> data) {
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
    public void navigateToMineActivity() {
        Intent intent = new Intent(StudyActivity.this, MineActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToStudyContentActivity(String link, String name, String desc) {
        Intent intent = new Intent(StudyActivity.this, StudyContentActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("name", name);
        intent.putExtra("desc", desc);
        startActivity(intent);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private List<StudyNew.Data> data = new ArrayList<>();

        public void setData(List<StudyNew.Data> newData) {
            data.clear();
            data.addAll(newData);
        }

        public void clearData() {
            data.clear();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_study, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StudyNew.Data datas = data.get(position);
            holder.desc.setText(datas.desc);
            holder.name.setText(datas.name);
            holder.itemView.setOnClickListener(view -> {
                presenter.onItemClick(datas);
            });

            Glide.with(holder.itemView.getContext())
                    .load(datas.cover)
                    .into(holder.imageViewCover);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView desc;
            TextView name;
            ImageView imageViewCover;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                desc = itemView.findViewById(R.id.rv2textView2);
                name = itemView.findViewById(R.id.rv2textView3);
                imageViewCover = itemView.findViewById(R.id.imageViewCover);
            }
        }
    }
}