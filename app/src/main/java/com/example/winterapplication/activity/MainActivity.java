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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.example.winterapplication.presenter.MainPresenter;
import com.example.winterapplication.model.MainModel;
import com.example.winterapplication.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private List<News.Data.Datas> newsDTOList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView openDrawerButton;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton returnTop;
    private List<News.Data.Datas> filteredList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private ViewFlipper viewFlipper;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 创建 Presenter 和 Model 实例
        MainModel model = new MainModel();
        presenter = new MainPresenter(this, model);

        // 发起数据请求
        presenter.fetchNewsData();
        presenter.fetchBannerData();
    }

    private void initViews() {
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
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());

        searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.filter(newText);
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
    public void displayNews(List<News.Data.Datas> news) {
        newsDTOList.clear();
        filteredList.clear();
        newsDTOList.addAll(news);
        filteredList.addAll(news);
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayBanner(List<MainBannerNew.Data> banners) {
        viewFlipper.removeAllViews();
        for (int i = 0; i < banners.size(); i++) {
            final MainBannerNew.Data data = banners.get(i);
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
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshList() {
        newsDTOList.clear();
        filteredList.clear();
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        presenter.filter(text);
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

    public static class News {
        public Data data;

        public static class Data {
            public List<Datas> datas;

            public static class Datas {
                int zan;
                String author;
                String niceDate;
                public String title;
                String link;
            }
        }
    }

    public static class MainBannerNew {
        public List<Data> data;

        public static class Data {
            String imagePath;
            String url;
        }
    }
}