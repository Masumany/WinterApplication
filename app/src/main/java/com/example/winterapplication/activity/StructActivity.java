package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.winterapplication.R;
import com.example.winterapplication.news.StructNew;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.winterapplication.presenter.StructPresenter;
import com.example.winterapplication.model.StructModel;
import com.example.winterapplication.view.StructView;

import java.util.ArrayList;
import java.util.List;

public class StructActivity extends AppCompatActivity implements StructView {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SearchView searchView;
    private FloatingActionButton returnTop;
    private BottomNavigationView bottomNavigationView;
    private StructPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struct);

        StructModel model = new StructModel();
        presenter = new StructPresenter(this, model);

        initViews();
        setupListeners();

        presenter.fetchData();
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        mRecyclerView = findViewById(R.id.struct_rv);
        returnTop = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.filter(newText);
                return true;
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                presenter.onNavigationItemSelected(item.getItemId());
                return true;
            }
        });

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

        bottomNavigationView.setSelectedItemId(R.id.navigation_structure);

        Button button1 = findViewById(R.id.st_article);
        button1.setOnClickListener(v -> presenter.onStructArticleButtonClick());
    }

    @Override
    public void showLoading() {
        // 这里可以添加显示加载状态的逻辑，比如显示进度条
    }

    @Override
    public void hideLoading() {
        // 这里可以添加隐藏加载状态的逻辑，比如隐藏进度条
    }

    @Override
    public void displayData(List<StructNew.Category.SubCategory> data) {
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
        Intent intent = new Intent(StructActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToDirectorActivity() {
        Intent intent = new Intent(StructActivity.this, DirectorActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToMineActivity() {
        Intent intent = new Intent(StructActivity.this, MineActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToStructArticle() {
        Intent intent = new Intent(StructActivity.this, StructArticleActivity.class);
        startActivity(intent);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private List<StructNew.Category.SubCategory> data = new ArrayList<>();

        public void setData(List<StructNew.Category.SubCategory> newData) {
            data.clear();
            data.addAll(newData);
        }

        public void clearData() {
            data.clear();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.new_item_struct, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StructNew.Category.SubCategory newsDTO = data.get(position);
            holder.name.setText(newsDTO.name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            MyViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rvtextView2);
            }
        }
    }
}