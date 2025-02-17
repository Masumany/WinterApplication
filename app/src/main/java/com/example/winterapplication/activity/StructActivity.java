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
import com.example.winterapplication.model.StructModel;
import com.example.winterapplication.news.StructNew;
import com.example.winterapplication.presenter.StructPresenter;
import com.example.winterapplication.view.StructView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        // 创建 StructModel 实例，用于处理数据获取等操作
        StructModel model = new StructModel();
        // 创建 StructPresenter 实例，传入当前活动实例和 StructModel 实例
        presenter = new StructPresenter(this, model);


        initViews();
        // 设置各种事件监听器
        setupListeners();

        // 发起获取结构数据的请求
        presenter.fetchData();
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        mRecyclerView = findViewById(R.id.struct_rv);
        returnTop = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        mAdapter = new Adapter();
        // 为 RecyclerView 设置适配器
        mRecyclerView.setAdapter(mAdapter);
        // 为 RecyclerView 设置线性布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // 设置各种事件监听器的方法
    private void setupListeners() {
        // 为搜索框设置文本变化监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

        // 为底部导航栏设置菜单项选择监听器
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 当菜单项被选中时，调用 Presenter 的相应方法处理
                presenter.onNavigationItemSelected(item.getItemId());
                return true;
            }
        });

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

        // 设置底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.navigation_structure);

        Button button1 = findViewById(R.id.st_article);
        button1.setOnClickListener(v -> presenter.onStructArticleButtonClick());
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void displayData(List<StructNew.Category.SubCategory> data) {
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

    // RecyclerView 的适配器类
    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        // 存储结构数据的列表
        private final List<StructNew.Category.SubCategory> data = new ArrayList<>();

        // 设置新的数据到适配器
        public void setData(List<StructNew.Category.SubCategory> newData) {
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
            // 加载结构数据列表项的布局文件
            View view = getLayoutInflater().inflate(R.layout.new_item_struct, parent, false);
            // 创建并返回 ViewHolder 实例
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // 获取当前位置的结构数据
            StructNew.Category.SubCategory newsDTO = data.get(position);
            // 设置结构名称
            holder.name.setText(newsDTO.name);
        }

        @Override
        public int getItemCount() {
            // 返回结构数据列表的大小
            return data.size();
        }

        // ViewHolder 类，用于缓存列表项中的视图
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rvtextView2);
            }
        }
    }
}