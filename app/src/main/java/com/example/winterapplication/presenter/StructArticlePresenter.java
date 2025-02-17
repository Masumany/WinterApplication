package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.news.StructArticleNew;
import com.example.winterapplication.model.StructArticleModel;
import com.example.winterapplication.view.StructArticleView;

import java.util.ArrayList;
import java.util.List;

public class StructArticlePresenter {
    private StructArticleView view;
    private StructArticleModel model;
    private List<StructArticleNew.Data.Article> articleList = new ArrayList<>();
    private List<StructArticleNew.Data.Article> filteredList = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public StructArticlePresenter(StructArticleView view, StructArticleModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {
        view.showLoading();
        model.fetchData(new StructArticleModel.StructArticleCallback() {
            @Override
            public void onSuccess(StructArticleNew response) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (response != null && response.data != null) {
                        articleList.clear();
                        filteredList.clear();
                        articleList.addAll(response.data.datas);
                        filteredList.addAll(response.data.datas);
                        view.displayArticles(filteredList);
                    } else {
                        view.showError("获取数据失败");
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
        fetchData();
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(articleList);
        } else {
            for (StructArticleNew.Data.Article item : articleList) {
                if (item.title.contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        view.displayArticles(filteredList);
    }

    public void onBackButtonClick() {
        view.navigateToStructActivity();
    }

    public void onItemClick(StructArticleNew.Data.Article article) {
        view.navigateToStructContentActivity(article.link, article.title, article.shareUser, article.niceDate);
    }
}