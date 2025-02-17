package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.winterapplication.R;
import com.example.winterapplication.presenter.DirectorPresenter;
import com.example.winterapplication.view.DirectorView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class DirectorActivity extends AppCompatActivity implements DirectorView {

    List<DirectorNews.Articles> newsDtoList = new ArrayList<>();

    List<DirectorNews.Articles> filteredList = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private SearchView searchView;

    private FloatingActionButton returnTop;

    private BottomNavigationView bottomNavigationView;

    private DirectorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_director);


        presenter = new DirectorPresenter(this);


        searchView = findViewById(R.id.searchView);

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 当用户提交搜索时，这里不做处理，返回 false
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 当搜索框文本发生变化时，调用 presenter 的过滤方法
                presenter.filter(newText);
                return true;
            }
        });

        // 初始化 RecyclerView
        mRecyclerView = findViewById(R.id.dt_rv);
        // 设置 RecyclerView 的布局管理器为线性布局
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 创建并设置 RecyclerView 的适配器
        mAdapter = new NewsAdapter(newsDtoList);
        mRecyclerView.setAdapter(mAdapter);

        // 初始化返回顶部的悬浮按钮
        returnTop = findViewById(R.id.fab);
        // 为 RecyclerView 添加滚动监听器
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取 RecyclerView 的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 获取第一个可见项的位置
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 当 RecyclerView 停止滚动时
                    if (firstVisibleItemPosition == 0) {
                        // 如果第一个可见项是列表的第一项，隐藏返回顶部按钮
                        returnTop.hide();
                    } else {
                        // 否则显示返回顶部按钮，并设置点击事件
                        returnTop.show();
                        returnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 点击返回顶部按钮时，将 RecyclerView 滚动到列表顶部
                                recyclerView.scrollToPosition(0);
                            }
                        });
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // 当 RecyclerView 正在滚动时，隐藏返回顶部按钮
                    returnTop.hide();
                }
            }
        });

        // 初始化底部导航栏
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // 设置底部导航栏的菜单项选择监听器
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 根据用户选择的菜单项执行不同的操作
                if (item.getItemId() == R.id.navigation_home) {

                    Intent intent1 = new Intent(DirectorActivity.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                } else if (item.getItemId() == R.id.navigation_structure) {

                    Intent intent2 = new Intent(DirectorActivity.this, StructActivity.class);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navigation_director) {

                    return true;
                } else if (item.getItemId() == R.id.navigation_mine) {

                    Intent intent3 = new Intent(DirectorActivity.this, MineActivity.class);
                    startActivity(intent3);
                    return true;
                }
                return false;
            }
        });
        // 设置底部导航栏当前选中的菜单项为当前活动对应的菜单项
        bottomNavigationView.setSelectedItemId(R.id.navigation_director);

        // 调用 presenter 的获取数据方法
        presenter.fetchData();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void displayNews(List<DirectorNews.Articles> data) {
        // 清空原始新闻数据列表
        newsDtoList.clear();
        // 将新获取的新闻数据添加到原始列表中
        newsDtoList.addAll(data);
        // 清空过滤后的新闻数据列表
        filteredList.clear();
        // 将新获取的新闻数据添加到过滤后的列表中
        filteredList.addAll(data);
        // 通知适配器数据发生变化，更新 RecyclerView
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        // 打印错误信息到日志
        Log.e("DirectorActivity", message);
    }

    @Override
    public void refreshList() {
        // 清空原始新闻数据列表
        newsDtoList.clear();
        // 清空过滤后的新闻数据列表
        filteredList.clear();
        // 通知适配器数据发生变化，更新 RecyclerView
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        // 调用 presenter 的过滤方法
        presenter.filter(text);
    }

    // 封装新闻数据结构
    public static class DirectorNews {

        private List<Data> data;

        public List<Data> getData() {
            return data;
        }


        public static class Data {

            private String name;

            private List<Articles> articles;

            public String getName() {
                return name;
            }

            public List<Articles> getArticles() {
                return articles;
            }
        }


        public static class Articles {

            String niceDate;

            private boolean adminAdd;

            private String apkLink;

            private int audit;

            private String author;

            private boolean canEdit;

            private int chapterId;

            private String chapterName;

            private boolean collect;

            private int courseId;

            private String desc;

            private String descMd;

            private String envelopePic;

            private boolean fresh;

            private String host;

            private int id;
            private boolean isAdminAdd;

            private String link;

            private String niceShareDate;

            private String origin;

            private String prefix;

            private String projectLink;

            private long publishTime;

            private int realSuperChapterId;

            private int selfVisible;

            private long shareDate;

            private String shareUser;

            private int superChapterId;

            private String superChapterName;

            private String title;

            private int type;

            private int userId;

            private int visible;

            private int zan;
            private List<Tag> tags;

            public String getLink() {
                return link;
            }

            public String getTitle() {
                return title;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public int getZan() {
                return zan;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public long getShareDate() {
                return shareDate;
            }

            // 用于存储新闻标签数据
            public static class Tag {
                // 标签 ID
                private int cid;
                // 标签名称
                private String name;

                public int getCid() {
                    return cid;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }

    // 内部类，用于为 RecyclerView 提供数据和视图绑定
    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        // 存储要显示的新闻列表
        private final List<DirectorNews.Articles> articleList;

        public NewsAdapter(List<DirectorNews.Articles> articleList) {
            this.articleList = articleList;
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 加载新闻列表项的布局文件
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_director, parent, false);
            // 创建并返回 NewsViewHolder 实例
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            // 获取当前位置的新闻数据
            DirectorNews.Articles article = filteredList.get(position);

            holder.name.setText(article.getTitle());

            holder.niceDate.setText(article.getLink());
            // 为列表项设置点击事件监听器
            holder.itemView.setOnClickListener(view -> {
                //跳转
                Intent intent = new Intent(DirectorActivity.this, DirectorContentActivity.class);
                // 传递新闻标题和作者到目标活动
                intent.putExtra("link", article.title);
                intent.putExtra("author", article.author);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            // 返回过滤后列表的大小
            return filteredList.size();
        }

        // 缓存列表项中的视图
        class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView niceDate;

            public NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rv2textView2);
                niceDate = itemView.findViewById(R.id.rv2textView4);
            }
        }
    }
}