package com.example.winterapplication.presenter;

import android.app.Activity;

import com.example.winterapplication.model.AnswersModel;
import com.example.winterapplication.news.AnswersNew;
import com.example.winterapplication.view.AnswersView;

import java.util.ArrayList;
import java.util.List;

public class AnswersPresenter {

    private final AnswersView view;

    private final AnswersModel model;

    private final List<AnswersNew.Data> data = new ArrayList<>();

    private final List<AnswersNew.Data> filteredList = new ArrayList<>();


    public AnswersPresenter(AnswersView view) {
        this.view = view;
        this.model = new AnswersModel();
    }


    //发起数据获取请求

    public void fetchData() {
        // 显示加载中视图
        view.showLoading();
        try {
            // 调用模型层的方法获取数据
            model.fetchDataFromApi(this);
        } catch (Exception e) {
            // 隐藏加载中视图
            view.hideLoading();
            // 显示错误信息
            view.showError("数据获取过程中发生异常: " + e.getMessage());
        }
    }

    public void onFetchSuccess(List<AnswersNew.Data> newData) {
        if (newData != null) {
            // 隐藏加载中视图
            view.hideLoading();
            // 清空原始数据列表
            data.clear();
            // 清空过滤后的数据列表
            filteredList.clear();
            // 将新数据添加到原始数据列表
            data.addAll(newData);
            // 将新数据添加到过滤后的数据列表
            filteredList.addAll(newData);
            // 切换到主线程更新视图
            ((Activity) view).runOnUiThread(() -> view.displayAnswers(filteredList));
        } else {
            // 隐藏加载中视图
            view.hideLoading();
            // 显示错误信息
            view.showError("获取到的数据为空");
        }
    }


    public void onFetchFailed(String message) {
        // 隐藏加载中视图
        view.hideLoading();
        // 显示错误信息
        view.showError(message);
    }


    public void refreshData() {
        // 显示加载中视图
        view.showLoading();
        // 刷新列表视图
        view.refreshList();
        // 重新发起数据获取请求
        fetchData();
    }


    public void filter(String text) {
        // 清空过滤后的数据列表
        filteredList.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始数据添加到过滤后的数据列表
            filteredList.addAll(data);
        } else {
            // 遍历原始数据列表，将包含过滤文本的项添加到过滤后的数据列表
            for (AnswersNew.Data item : data) {
                if (item.title.contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        // 更新视图显示过滤后的数据
        view.displayAnswers(filteredList);
    }
}