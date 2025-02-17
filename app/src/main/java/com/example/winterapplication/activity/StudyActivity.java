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
import com.example.winterapplication.model.StudyModel;
import com.example.winterapplication.news.StudyNew;
import com.example.winterapplication.presenter.StudyPresenter;
import com.example.winterapplication.view.StudyView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        // 创建 StudyModel 实例，用于处理数据获取等操作
        StudyModel model = new StudyModel();
        // 创建 StudyPresenter 实例，传入当前活动实例和 StudyModel 实例
        presenter = new StudyPresenter(this, model);


        initViews();
        // 设置各种事件监听器
        setupListeners();

        // 发起获取学习数据的请求
        presenter.fetchData();
    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        mRecyclerView = findViewById(R.id.re);
        returnTop = findViewById(R.id.fab);
        searchView = findViewById(R.id.searchView);
        BackTextView = findViewById(R.id.net_back);

        // 创建 RecyclerView 的适配器
        mAdapter = new Adapter();
        // 为 RecyclerView 设置适配器
        mRecyclerView.setAdapter(mAdapter);
        // 为 RecyclerView 设置线性布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        // 为下拉刷新布局设置刷新监听器，当用户下拉刷新时，调用 Presenter 的刷新数据方法
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());

        // 为 RecyclerView 添加滚动监听器
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取 RecyclerView 的线性布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 获取第一个可见项的位置
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // 当 RecyclerView 停止滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果第一个可见项是列表的第一项，隐藏返回顶部按钮
                    if (firstVisibleItemPosition == 0) {
                        returnTop.hide();
                    } else {
                        // 否则显示返回顶部按钮，并设置点击事件，点击后将 RecyclerView 滚动到顶部
                        returnTop.show();
                        returnTop.setOnClickListener(v -> recyclerView.scrollToPosition(0));
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // 当 RecyclerView 正在拖动时，隐藏返回顶部按钮
                    returnTop.hide();
                }
            }
        });

        // 设置搜索框默认不收缩
        searchView.setIconified(false);
        // 为搜索框设置文本变化监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 当用户提交搜索时，这里不做处理，返回 false
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 当搜索框文本发生变化时，调用 Presenter 的过滤方法
                presenter.filter(newText);
                return true;
            }
        });

        // 检查返回按钮对应的 TextView 是否为空
        if (BackTextView != null) {
            // 若不为空，为其设置点击事件监听器，点击时调用 Presenter 的返回按钮点击处理方法
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
        // 调用适配器的 setData 方法更新数据
        ((Adapter) mAdapter).setData(data);
        // 通知适配器数据发生变化，刷新 RecyclerView
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        // 调用适配器的 clearData 方法清空数据
        ((Adapter) mAdapter).clearData();
        // 通知适配器数据发生变化，刷新 RecyclerView
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        // 调用 Presenter 的过滤方法
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
        // 传递学习内容链接、名称和描述到详情活动
        intent.putExtra("link", link);
        intent.putExtra("name", name);
        intent.putExtra("desc", desc);
        startActivity(intent);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private final List<StudyNew.Data> data = new ArrayList<>();


        public void setData(List<StudyNew.Data> newData) {

            data.clear();

            data.addAll(newData);
        }

        // 清空适配器中的数据
        public void clearData() {
            data.clear();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_study, parent, false);
            // 创建并返回 ViewHolder 实例
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StudyNew.Data datas = data.get(position);
            holder.desc.setText(datas.desc);
            holder.name.setText(datas.name);
            // 为列表项设置点击事件监听器，点击时调用 Presenter 的 onItemClick 方法
            holder.itemView.setOnClickListener(view -> {
                presenter.onItemClick(datas);
            });

            // 使用 Glide 加载学习内容封面图片
            Glide.with(holder.itemView.getContext()).load(datas.cover).into(holder.imageViewCover);
        }

        @Override
        public int getItemCount() {
            // 返回学习数据列表的大小
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