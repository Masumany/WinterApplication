package com.example.winterapplication.presenter;

import android.app.Activity;
import com.example.winterapplication.activity.DirectorActivity.DirectorNews;
import com.example.winterapplication.model.DirectorModel;
import com.example.winterapplication.view.DirectorView;

import java.util.ArrayList;
import java.util.List;

public class DirectorPresenter {
    private DirectorView view;
    private DirectorModel model;
    private List<DirectorNews.Articles> data = new ArrayList<>();
    private List<DirectorNews.Articles> filteredList = new ArrayList<>();

    public DirectorPresenter(DirectorView view) {
        this.view = view;
        this.model = new DirectorModel();
    }

    public void fetchData() {
        view.showLoading();
        model.fetchDataFromApi(this);
    }

    public void onFetchSuccess(List<DirectorNews.Articles> newData) {
        if (newData != null) {
            view.hideLoading();
            data.clear();
            filteredList.clear();
            data.addAll(newData);
            filteredList.addAll(newData);

            // 将视图更新操作切换到主线程
            if (view instanceof Activity) {
                ((Activity) view).runOnUiThread(() -> view.displayNews(filteredList));
            } else {
                view.displayNews(filteredList);
            }
        } else {
            view.hideLoading();
            view.showError("获取到的数据为空");
        }
    }

    public void onFetchFailed(String message) {
        view.hideLoading();
        view.showError(message);
    }

    public void filter(String text) {
        filteredList.clear();
        if (android.text.TextUtils.isEmpty(text)) {
            filteredList.addAll(data);
        } else {
            for (DirectorNews.Articles item : data) {
                if (item.getTitle().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        view.displayNews(filteredList);
    }
}