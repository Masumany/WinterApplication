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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.winterapplication.presenter.RoutePresenter;
import com.example.winterapplication.model.RouteModel;
import com.example.winterapplication.view.RouteView;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements RouteView {
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private RecyclerView.Adapter mAdapter;
    private List<RouteNew.Data> data = new ArrayList<>();
    private TextView BackTextView;
    private FloatingActionButton returnTop;
    private List<RouteNew.Data> filteredList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RoutePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        RouteModel model = new RouteModel();
        presenter = new RoutePresenter(this, model);

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
            Log.e("Route", "BackTextView is null");
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
    public void displayData(List<RouteNew.Data> data) {
        this.data.clear();
        filteredList.clear();
        this.data.addAll(data);
        filteredList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        data.clear();
        filteredList.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        presenter.filter(text);
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(RouteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_route, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            RouteNew.Data datas = filteredList.get(position);
            holder.desc.setText(datas.desc);
            holder.name.setText(datas.name);
            holder.author.setText(datas.author);
            Glide.with(holder.itemView.getContext())
                    .load(datas.cover)
                    .into(holder.imageViewCover);
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView desc;
            TextView author;
            TextView name;
            ImageView imageViewCover;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                desc = itemView.findViewById(R.id.rv2textView2);
                author = itemView.findViewById(R.id.rv2textView4);
                name = itemView.findViewById(R.id.rv2textView3);
                imageViewCover = itemView.findViewById(R.id.imageViewCover);
            }
        }
    }
    public class RouteNew {
        public List<Data> data;
        private int errorCode;
        private String errorMsg;

        public class Data {

            String author;
            String cover;
            String desc;

            public String name;

        }
    }
}