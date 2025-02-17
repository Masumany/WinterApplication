package com.example.winterapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.winterapplication.R;
import com.example.winterapplication.model.MainModel;
import com.example.winterapplication.presenter.MainPresenter;
import com.example.winterapplication.view.MainView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

// MainActivity 继承自 AppCompatActivity 并实现 MainView 接口，是应用的主界面
public class MainActivity extends AppCompatActivity implements MainView {

    private RecyclerView mRecyclerView;

    private MyAdapter mMyAdapter;

    private final List<News.Data.Datas> newsDTOList = new ArrayList<>();

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private ImageView openDrawerButton;

    private SearchView searchView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FloatingActionButton returnTop;

    private final List<News.Data.Datas> filteredList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private ViewFlipper viewFlipper;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        initViews();

        // 创建数据模型实例
        MainModel model = new MainModel();
        // 创建 Presenter 实例，并传入当前视图和数据模型
        presenter = new MainPresenter(this, model);

        // 发起获取新闻数据的请求
        presenter.fetchNewsData();
        // 发起获取轮播图数据的请求
        presenter.fetchBannerData();
    }


    private void initViews() {
        // 获取侧边栏布局视图
        View view = findViewById(R.id.drawer_layout);
        // 获取打开侧边栏的按钮
        openDrawerButton = findViewById(R.id.open_drawer_button);

        // 检查获取的视图是否为 DrawerLayout 类型
        if (view instanceof DrawerLayout) {
            // 若是 DrawerLayout 类型，进行类型转换并赋值
            drawerLayout = (DrawerLayout) view;

            navigationView = findViewById(R.id.nav_view);

            // 为打开侧边栏按钮设置点击事件监听器
            openDrawerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击按钮时，打开侧边栏
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });

            // 为侧边栏导航视图设置菜单项点击事件监听器
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    // 获取点击菜单项的 ID
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_item1) {

                        Intent intent = new Intent(MainActivity.this, UsefulWebsiteActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.nav_item2) {

                        Intent intent = new Intent(MainActivity.this, HotWordsActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.nav_item3) {

                        Intent intent = new Intent(MainActivity.this, AnswersActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.nav_item4) {

                        Intent intent = new Intent(MainActivity.this, SpecialColumnActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.nav_item5) {

                        Intent intent = new Intent(MainActivity.this, RouteActivity.class);
                        startActivity(intent);
                    } else {

                        Toast.makeText(MainActivity.this, "未知菜单项被点击", Toast.LENGTH_SHORT).show();
                    }
                    // 关闭侧边栏
                    drawerLayout.closeDrawers();
                    return true;
                }
            });
        } else {
            // 若获取的视图不是 DrawerLayout 类型，显示提示信息
            Toast.makeText(this, "获取的视图不是 DrawerLayout 类型", Toast.LENGTH_SHORT).show();
        }


        mRecyclerView = findViewById(R.id.recycleview);
        // 创建 RecyclerView 的适配器
        mMyAdapter = new MyAdapter();
        // 为 RecyclerView 设置适配器
        mRecyclerView.setAdapter(mMyAdapter);
        // 为 RecyclerView 设置线性布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        // 为下拉刷新布局设置刷新监听器
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());


        searchView = findViewById(R.id.searchView);
        // 为搜索框设置文本变化监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 提交搜索时，调用 Presenter 的过滤方法
                presenter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 搜索框文本变化时，调用 Presenter 的过滤方法
                presenter.filter(newText);
                return true;
            }
        });


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
                // 获取 RecyclerView 的线性布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 获取第一个可见项的位置
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

                // 当 RecyclerView 停止滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到顶部
                    if (firstVisibleItemPosition == 0) {
                        // 滚动到顶部，隐藏返回顶部按钮
                        returnTop.hide();
                    } else {
                        // 未滚动到顶部，显示返回顶部按钮
                        returnTop.show();
                        // 为返回顶部按钮设置点击事件监听器
                        returnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 点击返回顶部按钮，将 RecyclerView 滚动到顶部
                                recyclerView.scrollToPosition(0);
                            }
                        });
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // 当 RecyclerView 正在拖动时，隐藏返回顶部按钮
                    returnTop.hide();
                }
            }
        });

        // 初始化 ViewFlipper 用于展示轮播图
        viewFlipper = findViewById(R.id.viewFlipper);
    }

    @Override
    public void showLoading() {
        // 显示加载中的刷新动画
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        // 隐藏加载中的刷新动画
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayNews(List<News.Data.Datas> news) {
        // 清空原始新闻数据列表
        newsDTOList.clear();
        // 清空过滤后新闻数据列表
        filteredList.clear();
        // 将新获取的新闻数据添加到原始列表
        newsDTOList.addAll(news);
        // 将新获取的新闻数据添加到过滤后列表
        filteredList.addAll(news);
        // 通知适配器数据发生变化，刷新 RecyclerView
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayBanner(List<MainBannerNew.Data> banners) {
        // 移除 ViewFlipper 中的所有视图
        viewFlipper.removeAllViews();
        // 遍历轮播图数据列表
        for (int i = 0; i < banners.size(); i++) {
            // 获取当前轮播图数据
            final MainBannerNew.Data data = banners.get(i);
            // 创建一个 ImageView 用于显示轮播图
            ImageView imageView = new ImageView(MainActivity.this);
            // 设置 ImageView 的布局参数
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // 设置 ImageView 的缩放类型
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 使用 Glide 加载轮播图图片
            Glide.with(MainActivity.this).load(data.imagePath).into(imageView);

            // 为 ImageView 设置点击事件监听器
            imageView.setOnClickListener(v -> {
                // 点击轮播图时，打开对应的链接
                String url = data.url;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });

            // 将 ImageView 添加到 ViewFlipper 中
            viewFlipper.addView(imageView);
        }
        // 开始 ViewFlipper 的自动翻转
        viewFlipper.startFlipping();
    }

    @Override
    public void showError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        // 清空原始新闻数据列表
        newsDTOList.clear();
        // 清空过滤后新闻数据列表
        filteredList.clear();
        // 通知适配器数据发生变化
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        // 调用 Presenter 的过滤方法
        presenter.filter(text);
    }

    // 新闻数据类
    public static class News {
        public Data data;

        public static class Data {
            public List<Datas> datas;

            public static class Datas {

                public String title;
                int zan;
                String author;
                String niceDate;
                String link;
            }
        }
    }

    // 轮播图数据类
    public static class MainBannerNew {
        public List<Data> data;

        public static class Data {

            String imagePath;

            String url;
        }
    }

    // RecyclerView 的适配器类
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
            // 创建并返回 ViewHolder 实例
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // 获取当前位置的新闻数据
            News.Data.Datas newsDTo = filteredList.get(position);

            holder.title.setText(newsDTo.title);

            holder.author.setText(newsDTo.author);

            holder.niceDate.setText(newsDTo.niceDate);

            holder.zan.setText(String.valueOf(newsDTo.zan));


            holder.itemView.setOnClickListener(v -> {

                Intent intent = new Intent(MainActivity.this, MainContentActivity.class);
                // 传递新闻链接、标题、作者、发布日期和点赞数到详情页
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
            // 返回过滤后新闻数据列表的大小
            return filteredList.size();
        }

        // ViewHolder 类，用于缓存列表项中的视图
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
}