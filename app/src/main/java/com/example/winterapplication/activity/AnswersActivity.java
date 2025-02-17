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
import com.example.winterapplication.news.AnswersNew;
import com.example.winterapplication.presenter.AnswersPresenter;
import com.example.winterapplication.view.AnswersView;
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

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);//下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());//调用 presenter 的 refreshData 方法来刷新数据

        mRecyclerView = findViewById(R.id.re);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);//设置适配器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 以线性布局的方式展示 item

        returnTop = findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();//线性布局
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();//初始化第一个item的位置
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//滚动状态为空闲
                    if (firstVisibleItemPosition == 0) {//第一项位置为0
                        returnTop.hide();//点击事件隐藏
                    } else {
                        returnTop.show();//点击事件显示
                        returnTop.setOnClickListener(view -> recyclerView.scrollToPosition(0));
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动状态
                    returnTop.hide();
                }
            }
        });

        searchView = findViewById(R.id.searchView);//搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//用户提交
                return false;//不进行特定的处理，有其他的逻辑
            }

            @Override
            public boolean onQueryTextChange(String text) {//用户搜索
                presenter.filter(text);//调用方法
                return true;
            }
        });

        initView();
        initClick();//点击事件

        presenter.fetchData();//网络解析方法
    }

    private void initClick() {//点击事件
        if (BackTextView != null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(AnswersActivity.this, MainActivity.class);//跳转
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("Answers", "BackTextView is null");
        }
    }

    private void initView() {//视图
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
    public void displayAnswers(List<AnswersNew.Data> data) {//展示数据
        updateRecyclerViewData(data);
    }

    @Override
    public void showError(String message) {//有错误时
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {//刷新列表
        clearRecyclerViewData();
    }

    @Override
    public void filterList(String text) {//搜索过滤
        presenter.filter(text);
    }

    private void updateRecyclerViewData(List<AnswersNew.Data> data) {//更新数据
        ((Adapter) mAdapter).setData(data);
        mAdapter.notifyDataSetChanged();
    }

    private void clearRecyclerViewData() {//清除数据
        ((Adapter) mAdapter).clearData();
        mAdapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {//适配器
        private final List<AnswersNew.Data> data = new ArrayList<>();

        public void setData(List<AnswersNew.Data> newData) {//更新数据
            data.clear();
            data.addAll(newData);
        }

        public void clearData() {//清除数据
            data.clear();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//视图
            View view = getLayoutInflater().inflate(R.layout.news_item_answers, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemCount() {//列表大小
            return data.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {//将数据放入相应的文本控件中
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

        class MyViewHolder extends RecyclerView.ViewHolder {//视图
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