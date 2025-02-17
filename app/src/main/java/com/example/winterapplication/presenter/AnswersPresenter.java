package com.example.winterapplication.presenter;

import android.app.Activity;

import com.example.winterapplication.news.AnswersNew;
import com.example.winterapplication.view.AnswersView;
import com.example.winterapplication.model.AnswersModel;
import java.util.ArrayList;
import java.util.List;

public class AnswersPresenter {
    private AnswersView view;
    private AnswersModel model;
    private List<AnswersNew.Data> data = new ArrayList<>();
    private List<AnswersNew.Data> filteredList = new ArrayList<>();

    public AnswersPresenter(AnswersView view) {
        this.view = view;
        this.model = new AnswersModel();
    }

    public void fetchData() {
        view.showLoading();
        try {
            model.fetchDataFromApi(this);
        } catch (Exception e) {
            // 处理异常，例如通知视图显示错误信息
            view.hideLoading();
            view.showError("数据获取过程中发生异常: " + e.getMessage());
        }
    }

    public void onFetchSuccess(List<AnswersNew.Data> newData) {
        if (newData != null) {
            view.hideLoading();
            data.clear();
            filteredList.clear();
            data.addAll(newData);
            filteredList.addAll(newData);
            // 切换到主线程更新视图
            ((Activity) view).runOnUiThread(() -> view.displayAnswers(filteredList));
        } else {
            view.hideLoading();
            view.showError("获取到的数据为空");
        }
    }

    public void onFetchFailed(String message) {
        view.hideLoading();
        view.showError(message);
    }

    public void refreshData() {
        view.showLoading();
        view.refreshList();
        fetchData();
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(data);
        } else {
            for (AnswersNew.Data item : data) {
                if (item.title.contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        view.displayAnswers(filteredList);
    }
}