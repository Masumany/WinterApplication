package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;


import com.example.winterapplication.news.SquareNews;
import com.example.winterapplication.model.SquareModel;
import com.example.winterapplication.view.SquareView;

import java.util.ArrayList;
import java.util.List;

public class SquarePresenter {
    private SquareView view;
    private SquareModel model;
    private List<SquareNews.Datas> squareData = new ArrayList<>();
    private List<SquareNews.Datas> filteredSquareData = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public SquarePresenter(SquareView view, SquareModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {
        view.showLoading();
        model.fetchData(new SquareModel.SquareCallback() {
            @Override
            public void onSuccess(SquareNews squareNews) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (squareNews != null && squareNews.data != null && squareNews.data.datas != null) {
                        squareData.clear();
                        filteredSquareData.clear();
                        squareData.addAll(squareNews.data.datas);
                        filteredSquareData.addAll(squareNews.data.datas);
                        view.displayData(filteredSquareData);
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
        filteredSquareData.clear();
        if (text.isEmpty()) {
            filteredSquareData.addAll(squareData);
        } else {
            for (SquareNews.Datas item : squareData) {
                if (item.title.contains(text)) {
                    filteredSquareData.add(item);
                }
            }
        }
        view.displayData(filteredSquareData);
    }

    public void onBackButtonClick() {
        view.navigateToMineActivity();
    }

    public void onItemClick(SquareNews.Datas datas) {
        view.navigateToSquareContentActivity(datas.link, datas.title, datas.niceDate);
    }
}