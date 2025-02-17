package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.activity.RouteActivity.RouteNew;
import com.example.winterapplication.model.RouteModel;
import com.example.winterapplication.view.RouteView;

import java.util.ArrayList;
import java.util.List;

public class RoutePresenter {

    private final RouteView view;

    private final RouteModel model;

    private final List<RouteNew.Data> routeData = new ArrayList<>();

    private final List<RouteNew.Data> filteredRouteData = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    public RoutePresenter(RouteView view, RouteModel model) {
        this.view = view;
        this.model = model;
    }


    public void fetchData() {

        view.showLoading();
        // 调用模型层的方法获取路线数据
        model.fetchData(new RouteModel.RouteCallback() {
            @Override
            public void onSuccess(RouteNew routeNew) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {

                    view.hideLoading();
                    // 检查获取的路线数据是否有效
                    if (routeNew != null && routeNew.data != null) {
                        // 清空原始路线数据列表
                        routeData.clear();
                        // 清空过滤后的路线数据列表
                        filteredRouteData.clear();
                        // 将新的路线数据添加到原始路线数据列表
                        routeData.addAll(routeNew.data);
                        // 将新的路线数据添加到过滤后的路线数据列表
                        filteredRouteData.addAll(routeNew.data);
                        // 通知视图层显示过滤后的路线数据
                        view.displayData(filteredRouteData);
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
        // 重新发起获取路线数据的请求
        fetchData();
    }


    public void filter(String text) {
        // 清空过滤后的路线数据列表
        filteredRouteData.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始路线数据添加到过滤后的路线数据列表
            filteredRouteData.addAll(routeData);
        } else {
            // 遍历原始路线数据列表，将名称包含过滤文本的路线添加到过滤后的路线数据列表
            for (RouteNew.Data item : routeData) {
                if (item.name.contains(text)) {
                    filteredRouteData.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的路线数据
        view.displayData(filteredRouteData);
    }


    public void onBackButtonClick() {
        view.navigateToMainActivity();
    }
}