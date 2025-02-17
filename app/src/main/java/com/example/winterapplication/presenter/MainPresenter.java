package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.activity.MainActivity.MainBannerNew;
import com.example.winterapplication.activity.MainActivity.News;
import com.example.winterapplication.model.MainModel;
import com.example.winterapplication.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter {

    private final MainView view;

    private final MainModel model;

    private final List<News.Data.Datas> newsData = new ArrayList<>();

    private final List<News.Data.Datas> filteredNews = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public MainPresenter(MainView view, MainModel model) {
        this.view = view;
        this.model = model;
    }


    public void fetchNewsData() {

        view.showLoading();
        // 调用模型层的方法获取新闻数据
        model.fetchNewsData(new MainModel.NewsCallback() {
            @Override
            public void onSuccess(News news) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {

                    view.hideLoading();
                    // 检查获取的新闻数据是否有效
                    if (news != null && news.data != null && news.data.datas != null) {
                        // 清空原始新闻数据列表
                        newsData.clear();
                        // 清空过滤后的新闻数据列表
                        filteredNews.clear();
                        // 将新的新闻数据添加到原始新闻数据列表
                        newsData.addAll(news.data.datas);
                        // 将新的新闻数据添加到过滤后的新闻数据列表
                        filteredNews.addAll(news.data.datas);
                        // 通知视图层显示过滤后的新闻数据
                        view.displayNews(filteredNews);
                    } else {
                        // 通知视图层显示获取新闻数据失败的错误信息
                        view.showError("获取新闻数据失败");
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


    public void fetchBannerData() {
        // 通知视图层显示加载状态
        view.showLoading();
        // 调用模型层的方法获取轮播图数据
        model.fetchBannerData(new MainModel.BannerCallback() {
            @Override
            public void onSuccess(MainBannerNew mainBannerNew) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {

                    view.hideLoading();
                    // 检查获取的轮播图数据是否有效
                    if (mainBannerNew != null && mainBannerNew.data != null) {

                        view.displayBanner(mainBannerNew.data);
                    } else {

                        view.showError("获取轮播图数据失败");
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
        // 重新发起获取新闻数据的请求
        fetchNewsData();
        // 重新发起获取轮播图数据的请求
        fetchBannerData();
    }


    public void filter(String text) {
        // 清空过滤后的新闻数据列表
        filteredNews.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始新闻数据添加到过滤后的新闻数据列表
            filteredNews.addAll(newsData);
        } else {
            // 遍历原始新闻数据列表，将标题包含过滤文本的新闻添加到过滤后的新闻数据列表
            for (News.Data.Datas item : newsData) {
                if (item.title.contains(text)) {
                    filteredNews.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的新闻数据
        view.displayNews(filteredNews);
    }
}