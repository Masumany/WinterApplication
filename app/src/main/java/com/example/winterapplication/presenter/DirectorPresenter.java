package com.example.winterapplication.presenter;

import android.app.Activity;

import com.example.winterapplication.activity.DirectorActivity.DirectorNews;
import com.example.winterapplication.model.DirectorModel;
import com.example.winterapplication.view.DirectorView;

import java.util.ArrayList;
import java.util.List;

public class DirectorPresenter {

    private final DirectorView view;

    private final DirectorModel model;

    private final List<DirectorNews.Articles> data = new ArrayList<>();

    private final List<DirectorNews.Articles> filteredList = new ArrayList<>();

    public DirectorPresenter(DirectorView view) {
        this.view = view;
        this.model = new DirectorModel();
    }


    public void fetchData() {
        // 通知视图层显示加载状态
        view.showLoading();
        // 调用模型层的方法获取数据
        model.fetchDataFromApi(this);
    }


    public void onFetchSuccess(List<DirectorNews.Articles> newData) {
        if (newData != null) {
            // 通知视图层隐藏加载状态
            view.hideLoading();
            // 清空原始数据列表
            data.clear();
            // 清空过滤后的数据列表
            filteredList.clear();
            // 将新数据添加到原始数据列表
            data.addAll(newData);
            // 将新数据添加到过滤后的数据列表
            filteredList.addAll(newData);

            // 由于网络请求是异步的，需要将视图更新操作切换到主线程
            if (view instanceof Activity) {
                ((Activity) view).runOnUiThread(() -> view.displayNews(filteredList));
            } else {
                view.displayNews(filteredList);
            }
        } else {
            // 通知视图层隐藏加载状态
            view.hideLoading();
            // 通知视图层显示数据为空的错误信息
            view.showError("获取到的数据为空");
        }
    }


    public void onFetchFailed(String message) {
        // 通知视图层隐藏加载状态
        view.hideLoading();
        // 通知视图层显示错误信息
        view.showError(message);
    }


    public void filter(String text) {
        // 清空过滤后的数据列表
        filteredList.clear();
        if (android.text.TextUtils.isEmpty(text)) {
            // 如果过滤文本为空，将原始数据添加到过滤后的数据列表
            filteredList.addAll(data);
        } else {
            // 遍历原始数据列表，将标题包含过滤文本的文章添加到过滤后的数据列表
            for (DirectorNews.Articles item : data) {
                if (item.getTitle().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的数据
        view.displayNews(filteredList);
    }
}