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
import com.example.winterapplication.news.StructArticleNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.winterapplication.presenter.StructArticlePresenter;
import com.example.winterapplication.model.StructArticleModel;
import com.example.winterapplication.view.StructArticleView;

import java.util.ArrayList;
import java.util.List;

public class StructArticleActivity extends AppCompatActivity implements StructArticleView {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView BackTextView;
    private SearchView searchView;
    private FloatingActionButton returnTop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StructArticlePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structarticle);

        StructArticleModel model = new StructArticleModel();
        presenter = new StructArticlePresenter(this, model);

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
            Log.e("StructArticle", "BackTextView is null");
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
    public void displayArticles(List<StructArticleNew.Data.Article> articles) {
        ((Adapter) mAdapter).setData(articles);
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
    public void filterArticles(String text) {
        presenter.filter(text);
    }

    @Override
    public void navigateToStructActivity() {
        Intent intent = new Intent(StructArticleActivity.this, StructActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToStructContentActivity(String link, String title, String author, String niceDate) {
        Intent intent = new Intent(StructArticleActivity.this, StructContentActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("niceData", niceDate);
        startActivity(intent);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private List<StructArticleNew.Data.Article> data = new ArrayList<>();

        public void setData(List<StructArticleNew.Data.Article> newData) {
            data.clear();
            data.addAll(newData);
        }

        public void clearData() {
            data.clear();
        }

        @Override
        public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_sa, parent, false);
            return new Adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StructArticleNew.Data.Article article = data.get(position);
            holder.title.setText(article.title);
            holder.niceDate.setText(article.niceDate);
            holder.zan.setText(String.valueOf(article.zan));
            holder.itemView.setOnClickListener(v -> presenter.onItemClick(article));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView niceDate;
            TextView zan;

            MyViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.rv2textView2);
                niceDate = itemView.findViewById(R.id.rv2textView3);
                zan = itemView.findViewById(R.id.rv2textView4);
            }
        }
    }
}

