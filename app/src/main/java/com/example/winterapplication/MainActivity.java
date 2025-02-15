package com.example.winterapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView openDrawerButton;

    private SearchView searchView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FloatingActionButton returnTop;

    List<News.Data.Datas> filteredList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取视图
        View view = findViewById(R.id.drawer_layout);
        openDrawerButton = findViewById(R.id.open_drawer_button);
        // 使用 if-else 进行类型检查
        if (view instanceof DrawerLayout) {
            drawerLayout = (DrawerLayout) view;
            navigationView = findViewById(R.id.nav_view);
            openDrawerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打开侧边栏
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            // 设置侧边栏菜单项点击事件
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_item1) {
                        // 处理菜单项1的点击事件
                        Toast.makeText(MainActivity.this, "菜单项1被点击", Toast.LENGTH_SHORT).show();
                    } else if (itemId == R.id.nav_item2) {
                        // 处理菜单项2的点击事件
                        Toast.makeText(MainActivity.this, "菜单项2被点击", Toast.LENGTH_SHORT).show();
                    } else if (itemId == R.id.nav_item3) {
                        // 处理菜单项3的点击事件
                        Toast.makeText(MainActivity.this, "菜单项3被点击", Toast.LENGTH_SHORT).show();
                    } else {
                        // 处理其他未知菜单项的情况
                        Toast.makeText(MainActivity.this, "未知菜单项被点击", Toast.LENGTH_SHORT).show();
                    }
                    // 关闭侧边栏
                    drawerLayout.closeDrawers();
                    return true;
                }
            });
        } else {
            // 若不是 DrawerLayout 类型，给出提示
            Toast.makeText(this, "获取的视图不是 DrawerLayout 类型", Toast.LENGTH_SHORT).show();
        }

        mRecyclerView = findViewById(R.id.recycleview);
        fetchDataFromApi(); // 发起网络请求

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);//将数据通过适配器绑在RV上
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//线性布局

       swipeRefreshLayout=findViewById(R.id.swiperefreshlayout);
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               refreshData();
           }
       });

        searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
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





        returnTop = findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获得recyclerView的线性布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //获取到第一个item的显示的下标  不等于0表示第一个item处于不可见状态 说明列表没有滑动到顶部 显示回到顶部按钮
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
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                    returnTop.hide();
                }
            }
        });

        // 初始化 ViewFlipper
        viewFlipper = findViewById(R.id.viewFlipper);
        fetchBannerData();
    }

    private void refreshData() {
        swipeRefreshLayout.setRefreshing(true);
        new android.os.Handler().postDelayed(() -> {
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            // 清空数据
            runOnUiThread(() -> {
                newsDTOList.clear();
                filteredList.clear();
                mMyAdapter.notifyDataSetChanged();
            });
            // 重新获取数据
            fetchDataFromApi();
        }, 1500);
    }

    private void filter(String text) {
        runOnUiThread(() -> {
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
            // 通知 RecyclerView 数据已更新
            mMyAdapter.notifyDataSetChanged();
        });
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
            News news = gson.fromJson(result, News.class);
            if (news != null && news.data != null && news.data.datas != null) {
                runOnUiThread(() -> {
                    // 清空原有数据
                    newsDTOList.clear();
                    filteredList.clear();
                    // 添加新数据
                    newsDTOList.addAll(news.data.datas);
                    filteredList.addAll(news.data.datas);
                    // 通知 RecyclerView 数据已更新
                    mMyAdapter.notifyDataSetChanged();
                });
            }
        } catch (JsonSyntaxException e) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "JSON 解析失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void fetchBannerData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/banner/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "网络请求失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                parseBannerData(responseData);
            }
        });
    }

    private void parseBannerData(String responseData) {
        Gson gson = new Gson();
        MainBannerNew mainBannerNew = gson.fromJson(responseData, MainBannerNew.class);
        if (mainBannerNew != null && mainBannerNew.data != null) {
            final List<MainBannerNew.Data> datas = mainBannerNew.data;
            runOnUiThread(() -> {
                // 清空原有的视图
                viewFlipper.removeAllViews();
                for (int i = 0; i < datas.size(); i++) {
                    final MainBannerNew.Data data = datas.get(i);
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(MainActivity.this)
                            .load(data.imagePath)
                            .into(imageView);

                    // 设置图片点击事件
                    imageView.setOnClickListener(v -> {
                        String url = data.url;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    });

                    viewFlipper.addView(imageView);
                }
                // 开始自动翻转
                viewFlipper.startFlipping();
            });
        } else {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show());
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
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
        }

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

    static class MainBannerNew {
        List<Data> data;

        static class Data {
            String imagePath;
            String url;
        }
    }


}