package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.activity.MainActivity.News;
import com.example.winterapplication.activity.MainActivity.MainBannerNew;
import com.example.winterapplication.model.MainModel;
import com.example.winterapplication.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter {
    private MainView view;
    private MainModel model;
    private List<News.Data.Datas> newsData = new ArrayList<>();
    private List<News.Data.Datas> filteredNews = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public MainPresenter(MainView view, MainModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchNewsData() {
        view.showLoading();
        model.fetchNewsData(new MainModel.NewsCallback() {
            @Override
            public void onSuccess(News news) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (news != null && news.data != null && news.data.datas != null) {
                        newsData.clear();
                        filteredNews.clear();
                        newsData.addAll(news.data.datas);
                        filteredNews.addAll(news.data.datas);
                        view.displayNews(filteredNews);
                    } else {
                        view.showError("获取新闻数据失败");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    view.showError(errorMessage);
                });
            }
        });
    }

    public void fetchBannerData() {
        view.showLoading();
        model.fetchBannerData(new MainModel.BannerCallback() {
            @Override
            public void onSuccess(MainBannerNew mainBannerNew) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (mainBannerNew != null && mainBannerNew.data != null) {
                        view.displayBanner(mainBannerNew.data);
                    } else {
                        view.showError("获取轮播图数据失败");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    view.showError(errorMessage);
                });
            }
        });
    }

    public void refreshData() {
        view.showLoading();
        view.refreshList();
        fetchNewsData();
        fetchBannerData();
    }

    public void filter(String text) {
        filteredNews.clear();
        if (text.isEmpty()) {
            filteredNews.addAll(newsData);
        } else {
            for (News.Data.Datas item : newsData) {
                if (item.title.contains(text)) {
                    filteredNews.add(item);
                }
            }
        }
        view.displayNews(filteredNews);
    }
}