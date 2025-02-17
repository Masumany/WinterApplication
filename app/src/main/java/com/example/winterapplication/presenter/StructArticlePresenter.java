package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.model.StructArticleModel;
import com.example.winterapplication.news.StructArticleNew;
import com.example.winterapplication.view.StructArticleView;

import java.util.ArrayList;
import java.util.List;

public class StructArticlePresenter {

    private final StructArticleView view;

    private final StructArticleModel model;

    private final List<StructArticleNew.Data.Article> articleList = new ArrayList<>();

    private final List<StructArticleNew.Data.Article> filteredList = new ArrayList<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    public StructArticlePresenter(StructArticleView view, StructArticleModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {

        view.showLoading();
        // 调用模型层的方法获取结构化文章数据
        model.fetchData(new StructArticleModel.StructArticleCallback() {
            @Override
            public void onSuccess(StructArticleNew response) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {
                    // 通知视图层隐藏加载状态
                    view.hideLoading();
                    // 检查获取的结构化文章数据是否有效
                    if (response != null && response.data != null) {
                        // 清空原始文章列表
                        articleList.clear();
                        // 清空过滤后的文章列表
                        filteredList.clear();
                        // 将新的文章数据添加到原始文章列表
                        articleList.addAll(response.data.datas);
                        // 将新的文章数据添加到过滤后的文章列表
                        filteredList.addAll(response.data.datas);
                        // 通知视图层显示过滤后的文章列表
                        view.displayArticles(filteredList);
                    } else {

                        view.showError("获取数据失败");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {
                    // 通知视图层隐藏加载状态
                    view.hideLoading();
                    // 通知视图层显示错误信息
                    view.showError(errorMessage);
                });
            }
        });
    }


    public void refreshData() {

        view.showLoading();

        view.refreshList();
        // 重新发起获取结构化文章数据的请求
        fetchData();
    }


    public void filter(String text) {
        // 清空过滤后的文章列表
        filteredList.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始文章列表添加到过滤后的文章列表
            filteredList.addAll(articleList);
        } else {
            // 遍历原始文章列表，将标题包含过滤文本的文章添加到过滤后的文章列表
            for (StructArticleNew.Data.Article item : articleList) {
                if (item.title.contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的文章列表
        view.displayArticles(filteredList);
    }


    public void onBackButtonClick() {
        view.navigateToStructActivity();
    }

    public void onItemClick(StructArticleNew.Data.Article article) {
        view.navigateToStructContentActivity(article.link, article.title, article.shareUser, article.niceDate);
    }
}