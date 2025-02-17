package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.news.WebNew;
import com.example.winterapplication.model.UsefulWebsiteModel;
import com.example.winterapplication.view.UsefulWebsiteView;

import java.util.ArrayList;
import java.util.List;

public class UsefulWebsitePresenter {
    private UsefulWebsiteView view;
    private UsefulWebsiteModel model;
    private List<WebNew.Data> webData = new ArrayList<>();
    private List<WebNew.Data> filteredWebData = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public UsefulWebsitePresenter(UsefulWebsiteView view, UsefulWebsiteModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {
        view.showLoading();
        model.fetchData(new UsefulWebsiteModel.UsefulWebsiteCallback() {
            @Override
            public void onSuccess(WebNew webNew) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (webNew != null && webNew.data != null) {
                        webData.clear();
                        filteredWebData.clear();
                        webData.addAll(webNew.data);
                        filteredWebData.addAll(webNew.data);
                        view.displayData(filteredWebData);
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
        filteredWebData.clear();
        if (text.isEmpty()) {
            filteredWebData.addAll(webData);
        } else {
            for (WebNew.Data item : webData) {
                if (item.name.contains(text)) {
                    filteredWebData.add(item);
                }
            }
        }
        view.displayData(filteredWebData);
    }

    public void onBackButtonClick() {
        view.navigateToMainActivity();
    }

    public void onItemClick(WebNew.Data data) {
        view.navigateToUsefulWebsiteContent(data.link, data.name);
    }
}