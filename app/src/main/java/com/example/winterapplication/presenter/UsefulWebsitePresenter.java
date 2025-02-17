package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.model.UsefulWebsiteModel;
import com.example.winterapplication.news.WebNew;
import com.example.winterapplication.view.UsefulWebsiteView;

import java.util.ArrayList;
import java.util.List;

public class UsefulWebsitePresenter {

    private final UsefulWebsiteView view;

    private final UsefulWebsiteModel model;

    private final List<WebNew.Data> webData = new ArrayList<>();

    private final List<WebNew.Data> filteredWebData = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    public UsefulWebsitePresenter(UsefulWebsiteView view, UsefulWebsiteModel model) {
        this.view = view;
        this.model = model;
    }


    public void fetchData() {

        view.showLoading();
        // 调用模型层的方法获取有用网站数据
        model.fetchData(new UsefulWebsiteModel.UsefulWebsiteCallback() {
            @Override
            public void onSuccess(WebNew webNew) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {
                    // 通知视图层隐藏加载状态
                    view.hideLoading();
                    // 检查获取的有用网站数据是否有效
                    if (webNew != null && webNew.data != null) {
                        // 清空原始有用网站数据列表
                        webData.clear();
                        // 清空过滤后的有用网站数据列表
                        filteredWebData.clear();
                        // 将新的有用网站数据添加到原始数据列表
                        webData.addAll(webNew.data);
                        // 将新的有用网站数据添加到过滤后的数据列表
                        filteredWebData.addAll(webNew.data);
                        // 通知视图层显示过滤后的有用网站数据
                        view.displayData(filteredWebData);
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
        // 重新发起获取有用网站数据的请求
        fetchData();
    }


    public void filter(String text) {
        // 清空过滤后的有用网站数据列表
        filteredWebData.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始有用网站数据添加到过滤后的数据列表
            filteredWebData.addAll(webData);
        } else {
            // 遍历原始有用网站数据列表，将名称包含过滤文本的网站添加到过滤后的数据列表
            for (WebNew.Data item : webData) {
                if (item.name.contains(text)) {
                    filteredWebData.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的有用网站数据
        view.displayData(filteredWebData);
    }


    public void onBackButtonClick() {
        view.navigateToMainActivity();
    }

    public void onItemClick(WebNew.Data data) {
        view.navigateToUsefulWebsiteContent(data.link, data.name);
    }
}