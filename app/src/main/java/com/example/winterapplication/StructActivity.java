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
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StructActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<StructNew.Category.SubCategory> newsDtoList = new ArrayList<>();
    private TextView BackTextView;

    private SearchView searchView;

    List<StructNew.Category.SubCategory> filteredList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struct);

        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });



        mRecyclerView = findViewById(R.id.struct_rv);
        fetchDataFromApi(); // 发起网络请求

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);//将数据通过适配器绑在 RV 上
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//线性布局

        ImageView imageView = findViewById(R.id.first);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StructActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView1 = findViewById(R.id.director);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StructActivity.this, DirectorActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView2 = findViewById(R.id.mine);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StructActivity.this, MineActivity.class);
                startActivity(intent);
            }
        });
        TextView textView=findViewById(R.id.st_article);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StructActivity.this,StructArticle.class);
                startActivity(intent);
            }
        });
        initView();
        initClick();
    }

    private  void filter(String text){
        filteredList.clear();
        if (TextUtils.isEmpty(text)) {
            filteredList.addAll(newsDtoList);

        }else {
            for(StructNew.Category.SubCategory item:newsDtoList){
                if (item.name.contains(text)){
                    filteredList.add(item);
                }
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    private void fetchDataFromApi() {
        OkHttpClient okHttpClient = new OkHttpClient();//创建一个 OkHttpClient 实例，用于执行网络请求
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/tree/json")
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
                // 可以根据需要添加其他处理逻辑
            }
        });
    }


    private void handleResponse(String result) {
        Gson gson = new Gson();
        StructNew treeData = gson.fromJson(result, StructNew.class);
        if (treeData!= null && treeData.data!= null) {
            for (StructNew.Category category : treeData.data) {
                newsDtoList.addAll(category.children);
                filteredList.addAll(category.children);
            }
            runOnUiThread(() -> mAdapter.notifyDataSetChanged());
        }
    }


    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {//自定义的 RecyclerView 适配器类，用于将 newsDTOList 中的数据绑定到 RecyclerView 的列表项上


        @Override
        public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建 ViewHolder 的方法，使用 getLayoutInflater().inflate 方法将 news_item_layout 布局文件转换为视图，并创建 MyViewHolder 实例
            View view = getLayoutInflater().inflate(R.layout.new_item_struct, parent, false);
            return new Adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StructNew.Category.SubCategory newsDTO = filteredList.get(position);

            holder.name.setText(newsDTO.name);


        }


        @Override
        public int getItemCount() {
            return filteredList.size();
        }//返回 newsDTOList 的大小，用于确定 RecyclerView 列表项的数量


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;


            MyViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rvtextView2);


            }
        }
    }


    private void initClick() {
        if (BackTextView!= null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(StructActivity.this, MainActivity.class);
                // 启动该意图，实现页面跳转到首页面
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("StructActivity", "BackTextView is null");
        }
    }


    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }
}


class StructNew {
    List<Category> data;

    static class Category {
        private String name;
        private int order;
        List<SubCategory> children;

        static class SubCategory {
            public String name;
            private int id;
            private int courseId;
            private int parentChapterId;
            private int chapterSort;
            String title;
            int order;
            private boolean userControlSetTop;
            private int visible;
        }
    }
}