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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.winterapplication.presenter.DirectorPresenter;
import com.example.winterapplication.view.DirectorView;

import java.util.ArrayList;
import java.util.List;

public class DirectorActivity extends AppCompatActivity implements DirectorView {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    List<DirectorNews.Articles> newsDtoList = new ArrayList<>();
    private SearchView searchView;
    private FloatingActionButton returnTop;
    List<DirectorNews.Articles> filteredList = new ArrayList<>();
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.filter(newText);
                return true;
            }
        });

        mRecyclerView = findViewById(R.id.dt_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsAdapter(newsDtoList);
        mRecyclerView.setAdapter(mAdapter);

        returnTop = findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (firstVisibleItemPosition == 0) {
                        returnTop.hide();
                    } else {
                        returnTop.show();
                        returnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(0);
                            }
                        });
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    returnTop.hide();
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        bottomNavigationView.setSelectedItemId(R.id.navigation_director);

        presenter.fetchData();
    }

    @Override
    public void showLoading() {
        // 可以在这里显示加载进度条等
    }

    @Override
    public void hideLoading() {
        // 可以在这里隐藏加载进度条等
    }

    @Override
    public void displayNews(List<DirectorNews.Articles> data) {
        newsDtoList.clear();
        newsDtoList.addAll(data);
        filteredList.clear();
        filteredList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Log.e("DirectorActivity", message);
    }

    @Override
    public void refreshList() {
        newsDtoList.clear();
        filteredList.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterList(String text) {
        presenter.filter(text);
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        private List<DirectorNews.Articles> articleList;

        public NewsAdapter(List<DirectorNews.Articles> articleList) {
            this.articleList = articleList;
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_director, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            DirectorNews.Articles article = filteredList.get(position);
            holder.name.setText(article.getTitle());
            holder.niceDate.setText(article.getLink());
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(DirectorActivity.this, DirectorContentActivity.class);
                intent.putExtra("link", article.title);
                intent.putExtra("author", article.author);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

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
            String niceDate;
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

            public static class Tag {
                private int cid;
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
}