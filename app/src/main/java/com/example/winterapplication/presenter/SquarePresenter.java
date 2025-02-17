package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.model.SquareModel;
import com.example.winterapplication.news.SquareNews;
import com.example.winterapplication.view.SquareView;

import java.util.ArrayList;
import java.util.List;

public class SquarePresenter {

    private final SquareView view;

    private final SquareModel model;

    private final List<SquareNews.Datas> squareData = new ArrayList<>();

    private final List<SquareNews.Datas> filteredSquareData = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    public SquarePresenter(SquareView view, SquareModel model) {
        this.view = view;
        this.model = model;
    }


    public void fetchData() {

        view.showLoading();
        // 调用模型层的方法获取广场新闻数据
        model.fetchData(new SquareModel.SquareCallback() {
            @Override
            public void onSuccess(SquareNews squareNews) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {

                    view.hideLoading();
                    // 检查获取的广场新闻数据是否有效
                    if (squareNews != null && squareNews.data != null && squareNews.data.datas != null) {
                        // 清空原始广场新闻数据列表
                        squareData.clear();
                        // 清空过滤后的广场新闻数据列表
                        filteredSquareData.clear();
                        // 将新的广场新闻数据添加到原始数据列表
                        squareData.addAll(squareNews.data.datas);
                        // 将新的广场新闻数据添加到过滤后的数据列表
                        filteredSquareData.addAll(squareNews.data.datas);
                        // 通知视图层显示过滤后的广场新闻数据
                        view.displayData(filteredSquareData);
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
        // 重新发起获取广场新闻数据的请求
        fetchData();
    }


    public void filter(String text) {
        // 清空过滤后的广场新闻数据列表
        filteredSquareData.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始广场新闻数据添加到过滤后的数据列表
            filteredSquareData.addAll(squareData);
        } else {
            // 遍历原始广场新闻数据列表，将标题包含过滤文本的新闻添加到过滤后的数据列表
            for (SquareNews.Datas item : squareData) {
                if (item.title.contains(text)) {
                    filteredSquareData.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的广场新闻数据
        view.displayData(filteredSquareData);
    }


    public void onBackButtonClick() {
        view.navigateToMineActivity();
    }


    public void onItemClick(SquareNews.Datas datas) {
        view.navigateToSquareContentActivity(datas.link, datas.title, datas.niceDate);
    }
}