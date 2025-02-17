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
import com.example.winterapplication.presenter.UsefulWebsitePresenter;
import com.example.winterapplication.view.UsefulWebsiteView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        // 创建 UsefulWebsiteModel 实例，用于处理数据获取等操作
        UsefulWebsiteModel model = new UsefulWebsiteModel();
        // 创建 UsefulWebsitePresenter 实例，传入当前活动实例和 UsefulWebsiteModel 实例
        presenter = new UsefulWebsitePresenter(this, model);


        initViews();
        // 设置各种事件监听器
        setupListeners();

        // 发起获取有用网站数据的请求
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
    public void navigateToMainActivity() {
        Intent intent = new Intent(UsefulWebsiteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToUsefulWebsiteContent(String link, String name) {
        // 创建一个 Intent，用于从当前活动（UsefulWebsiteActivity）跳转到有用网站详情活动（UsefulWebsiteContentActivity）
        Intent intent = new Intent(UsefulWebsiteActivity.this, UsefulWebsiteContentActivity.class);
        // 传递网站链接和名称到详情活动
        intent.putExtra("link", link);
        intent.putExtra("name", name);
        // 启动 UsefulWebsiteContentActivity
        startActivity(intent);
    }

    // RecyclerView 的适配器类
    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        //
        private final List<WebNew.Data> data = new ArrayList<>();

        // 设置新的数据到适配器
        public void setData(List<WebNew.Data> newData) {
            // 清空原有数据
            data.clear();
            // 添加新数据
            data.addAll(newData);
        }

        // 清空适配器中的数据
        public void clearData() {
            data.clear();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 加载网站列表项的布局文件
            View view = getLayoutInflater().inflate(R.layout.news_item_usefulwebsite, parent, false);
            // 创建并返回 ViewHolder 实例
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // 获取当前位置的网站数据
            WebNew.Data datas = data.get(position);

            holder.name.setText(datas.name);
            holder.itemView.setOnClickListener(v -> presenter.onItemClick(datas));
        }

        @Override
        public int getItemCount() {
            // 返回网站数据列表的大小
            return data.size();
        }

        // 用于缓存列表项中的视图
        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rv2textView3);
            }
        }
    }
}