package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.activity.RouteActivity.RouteNew;
import com.example.winterapplication.model.RouteModel;
import com.example.winterapplication.view.RouteView;

import java.util.ArrayList;
import java.util.List;

public class RoutePresenter {
    private RouteView view;
    private RouteModel model;
    private List<RouteNew.Data> routeData = new ArrayList<>();
    private List<RouteNew.Data> filteredRouteData = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public RoutePresenter(RouteView view, RouteModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {
        view.showLoading();
        model.fetchData(new RouteModel.RouteCallback() {
            @Override
            public void onSuccess(RouteNew routeNew) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (routeNew != null && routeNew.data != null) {
                        routeData.clear();
                        filteredRouteData.clear();
                        routeData.addAll(routeNew.data);
                        filteredRouteData.addAll(routeNew.data);
                        view.displayData(filteredRouteData);
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
        filteredRouteData.clear();
        if (text.isEmpty()) {
            filteredRouteData.addAll(routeData);
        } else {
            for (RouteNew.Data item : routeData) {
                if (item.name.contains(text)) {
                    filteredRouteData.add(item);
                }
            }
        }
        view.displayData(filteredRouteData);
    }

    public void onBackButtonClick() {
        view.navigateToMainActivity();
    }
}