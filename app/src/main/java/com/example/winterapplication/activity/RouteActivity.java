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
import com.example.winterapplication.model.RouteModel;
import com.example.winterapplication.presenter.RoutePresenter;
import com.example.winterapplication.view.RouteView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class RouteActivity extends AppCompatActivity implements RouteView {

    private RecyclerView mRecyclerView;

    private SearchView searchView;

    private RecyclerView.Adapter mAdapter;

    private final List<RouteNew.Data> data = new ArrayList<>();

    private TextView BackTextView;

    private FloatingActionButton returnTop;

    private final List<RouteNew.Data> filteredList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    private RoutePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_route);


        RouteModel model = new RouteModel();
        // 创建 RoutePresenter 实例，传入当前活动实例和 RouteModel 实例
        presenter = new RoutePresenter(this, model);


        initViews();
        // 设置各种事件监听器
        setupListeners();

        // 发起获取路线数据的请求
        presenter.fetchData();
    }

    // 初始化界面视图的方法
    private void initViews() {

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        mRecyclerView = findViewById(R.id.re);

        returnTop = findViewById(R.id.fab);

        searchView = findViewById(R.id.searchView);

        BackTextView = findViewById(R.id.net_back);


        mAdapter = new Adapter();
        // 为 RecyclerView 设置适配器
        mRecyclerView.setAdapter(mAdapter);
        // 为 RecyclerView 设置线性布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // 设置各种事件监听器的方法
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
        // 清空原始路线数据列表
        this.data.clear();
        // 清空过滤后路线数据列表
        filteredList.clear();
        // 将新获取的路线数据添加到原始列表
        this.data.addAll(data);
        // 将新获取的路线数据添加到过滤后列表
        filteredList.addAll(data);
        // 通知适配器数据发生变化，刷新 RecyclerView
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        // 显示错误提示信息
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        // 清空原始路线数据列表
        data.clear();
        // 清空过滤后路线数据列表
        filteredList.clear();
        // 通知适配器数据发生变化
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        // 调用 Presenter 的过滤方法
        presenter.filter(text);
    }

    @Override
    public void navigateToMainActivity() {

        Intent intent = new Intent(RouteActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    // RecyclerView 的适配器类
    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 加载路线数据列表项的布局文件
            View view = getLayoutInflater().inflate(R.layout.news_item_route, parent, false);
            // 创建并返回 ViewHolder 实例
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // 获取当前位置的路线数据
            RouteNew.Data datas = filteredList.get(position);

            holder.desc.setText(datas.desc);

            holder.name.setText(datas.name);

            holder.author.setText(datas.author);
            // 使用 Glide 加载路线封面图片
            Glide.with(holder.itemView.getContext()).load(datas.cover).into(holder.imageViewCover);
        }

        @Override
        public int getItemCount() {
            // 返回过滤后路线数据列表的大小
            return filteredList.size();
        }

        // ViewHolder 类，用于缓存列表项中的视图
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

            public String name;
            String author;
            String cover;
            String desc;
        }
    }
}