package com.example.winterapplication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    List<News.Data.Datas> newsDTOList = new ArrayList<>();

    private List<Integer> list = new ArrayList<>();

    private SearchView searchView;

    List<News.Data.Datas> filteredList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycleview);
        fetchDataFromApi(); // 发起网络请求

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);//将数据通过适配器绑在RV上
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//线性布局

        Banner banner = findViewById(R.id.banner);
        initData();
        banner.setAdapter(new BannerImageAdapter<Integer>(list) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(list.get(position));
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // 初始化底部导航栏
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                        return true;
                    } else if (item.getItemId() == R.id.navigation_structure) {
                        Intent intent1 = new Intent(MainActivity.this, StructActivity.class);
                        startActivity(intent1);
                        return true;
                    } else if (item.getItemId() == R.id.navigation_director) {
                        Intent intent2 = new Intent(MainActivity.this, DirectorActivity.class);
                        startActivity(intent2);
                        return true;
                    } else if (item.getItemId() == R.id.navigation_mine) {
                        Intent intent3 = new Intent(MainActivity.this, MineActivity.class);
                        startActivity(intent3);
                        return true;
                    }
                    return false;
                }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(R.drawable.main_wan);
        list.add(R.drawable.main_wan3);
        list.add(R.drawable.main_wan2);
    }

    private void filter(String text) {
        filteredList.clear();
        if (TextUtils.isEmpty(text)) {
            filteredList.addAll(newsDTOList);
        } else {
            for (News.Data.Datas item : newsDTOList) {
                if (item.title.contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        mMyAdapter.notifyDataSetChanged();
    }

    private void fetchDataFromApi() {
        OkHttpClient okHttpClient = new OkHttpClient();//创建一个 OkHttpClient 实例，用于执行网络请求
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/article/list/1/json")
                .get()
                .build();//使用 Request.Builder 构建一个 GET 请求
        okHttpClient.newCall(request).enqueue(new Callback() {//将请求加入请求队列，使用 enqueue 方法异步执行请求，并添加一个回调函数，当请求成功时调用 onResponse 方法，失败时调用 onFailure 方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();//从响应中获取响应体的内容并存储在 result 字符串中
                handleResponse(result);//调用 handleResponse 方法处理响应数据
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "网络请求失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleResponse(String result) {
        try {
            Gson gson = new Gson();
            News news = gson.fromJson(result, News.class);//使用 Gson 将 result 字符串解析为 News 类型的对象
            if (news != null && news.data != null && news.data.datas != null) {
                newsDTOList.addAll(news.data.datas);
                filteredList.addAll(news.data.datas);
                runOnUiThread(() -> mMyAdapter.notifyDataSetChanged());//将 mMyAdapter.notifyDataSetChanged() 操作放在 runOnUiThread 中执行，因为更新 RecyclerView 的数据必须在 UI 线程中进行
            }
        } catch (JsonSyntaxException e) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "JSON 解析失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {//自定义的 RecyclerView 适配器类，用于将 newsDTOList 中的数据绑定到 RecyclerView 的列表项上
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建 ViewHolder 的方法，使用 getLayoutInflater().inflate 方法将 news_item_layout 布局文件转换为视图，并创建 MyViewHolder 实例
            View view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {//将数据绑定到 ViewHolder 的方法，根据位置从 newsDTOList 中获取 News.Data.Datas 类型的数据，将数据设置到 holder 的 TextView 中，并为 itemView 设置点击事件，点击后跳转到 ArticleContentActivity 并传递数据
            News.Data.Datas newsDTo = filteredList.get(position);
            holder.title.setText(newsDTo.title);
            holder.author.setText(newsDTo.author);
            holder.niceDate.setText(newsDTo.niceDate);
            holder.zan.setText(String.valueOf(newsDTo.zan));
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MainContentActivity.class);
                intent.putExtra("link", newsDTo.link);
                intent.putExtra("title", newsDTo.title);
                intent.putExtra("author", newsDTo.author);
                intent.putExtra("niceDate", newsDTo.niceDate);
                intent.putExtra("zan", newsDTo.zan);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }//返回 newsDTOList 的大小，用于确定 RecyclerView


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView author;
            TextView niceDate;
            TextView zan;


            MyViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.textView2);
                author = itemView.findViewById(R.id.textView3);
                niceDate = itemView.findViewById(R.id.textView4);
                zan = itemView.findViewById(R.id.textView5);
            }
        }
    }


    static class News {
        Data data;


        static class Data {
            List<Datas> datas;


            static class Datas {
                int zan;
                String author;
                String niceDate;
                String title;

                String link;
            }
        }
    }
}