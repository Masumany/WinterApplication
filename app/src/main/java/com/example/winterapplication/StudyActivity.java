package com.example.winterapplication;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudyActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private SearchView searchView;
    RecyclerView.Adapter mAdapter;
    List<StudyNew.Data> data = new ArrayList<>();
    private TextView BackTextView;
    private FloatingActionButton returnTop;
    List<StudyNew.Data> filteredList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mRecyclerView = findViewById(R.id.re);
        fetchDataFromApi(); // 发起网络请求

        returnTop = findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition == 0) {
                        returnTop.hide();
                    } else {
                        //显示回到顶部按钮
                        returnTop.show();
                        returnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(0);
                            }
                        });

                    }//获取RecyclerView滑动时候的状态
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                    returnTop.hide();
                }
            }
        });
        searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initVIew();
        initClick();

    }


    private void initClick() {
        if (BackTextView!= null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(StudyActivity.this, MineActivity.class);
                // 启动该意图，实现页面跳转到首页面
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("StructArticle", "BackTextView is null");
        }
    }

    private void initVIew() {
        BackTextView = findViewById(R.id.net_back);
    }
    private void refreshData() {
        swipeRefreshLayout.setRefreshing(true);
        new android.os.Handler().postDelayed(() -> {
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            runOnUiThread(() -> {
                data.clear();
                filteredList.clear();
                mAdapter.notifyDataSetChanged();
            });
            fetchDataFromApi();
        }, 1500);
    }

    private void filter(String text) {
        runOnUiThread(() -> {
            filteredList.clear();
            if (TextUtils.isEmpty(text)) {
                filteredList.addAll(data);
            } else {
                for (StudyNew.Data item : data) {
                    if (item.name.contains(text)) {
                        filteredList.add(item);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();


        });
    }

    private void fetchDataFromApi() {
        OkHttpClient okHttpClient = new OkHttpClient(); // 创建一个 OkHttpClient 实例，用于执行网络请求
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/chapter/547/sublist/json")
                .get()
                .build(); // 使用 Request.Builder 构建一个 GET 请求
        okHttpClient.newCall(request).enqueue(new Callback() { // 将请求加入请求队列，使用 enqueue 方法异步执行请求，并添加一个回调函数，当请求成功时调用 onResponse 方法，失败时调用 onFailure 方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string(); // 从响应中获取响应体的内容并存储在 result 字符串中
                handleResponse(result); // 调用 handleResponse 方法处理响应数据
            }


            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("StudyActivity", "网络请求失败: " + e.getMessage());
            }
        });

    }

    private void handleResponse(String result) {
        Gson gson = new Gson();
        StudyNew response = gson.fromJson(result, StudyNew.class);
        if (response != null && response.data != null) {
            runOnUiThread(() -> {
                data.clear();
                filteredList.clear();
                data.addAll(response.data);
                filteredList.addAll(data);
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // 创建 ViewHolder 的方法，使用 getLayoutInflater().inflate 方法将 news_item_layout 布局文件转换为视图，并创建 MyViewHolder 实例
            View view = getLayoutInflater().inflate(R.layout.news_item_study, parent, false);
            return new StudyActivity.Adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StudyNew.Data datas = filteredList.get(position);
            holder.desc.setText(datas.desc);
            holder.name.setText(datas.name);
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(StudyActivity.this, StudyContentActivity.class);
                intent.putExtra("link",datas.lisenseLink);
                intent.putExtra("name",datas.name);
                intent.putExtra("desc",datas.desc);
                startActivity(intent);

            });

            Glide.with(holder.itemView.getContext())
                .load(datas.cover)
                .into(holder.imageViewCover);

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


        @Override
        public int getItemCount() {
            return filteredList.size();
        }


    }
}
class StudyNew {
    List<Data> data;
    private int errorCode;
    private String errorMsg;
    static class Data{

        private String author;
        private int courseId;
        String cover;
        String desc;
        private int id;
        private String lisense;
        String lisenseLink;
        String name;
        private int order;
        private int parentChapterId;
        private int type;
        private boolean userControlSetTop;
        private int visible;
    }
}
