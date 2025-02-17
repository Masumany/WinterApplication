package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.R;
import com.example.winterapplication.model.StructModel;
import com.example.winterapplication.news.StructNew;
import com.example.winterapplication.view.StructView;

import java.util.ArrayList;
import java.util.List;

public class StructPresenter {

    private final StructView view;

    private final StructModel model;

    private final List<StructNew.Category.SubCategory> structData = new ArrayList<>();

    private final List<StructNew.Category.SubCategory> filteredStructData = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public StructPresenter(StructView view, StructModel model) {
        this.view = view;
        this.model = model;
    }


    public void fetchData() {

        view.showLoading();
        // 调用模型层的方法获取结构化数据
        model.fetchData(new StructModel.StructCallback() {
            @Override
            public void onSuccess(StructNew structNew) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {
                    // 通知视图层隐藏加载状态
                    view.hideLoading();
                    // 检查获取的结构化数据是否有效
                    if (structNew != null && structNew.data != null) {
                        // 清空原始数据列表
                        structData.clear();
                        // 清空过滤后的数据列表
                        filteredStructData.clear();
                        // 遍历每个分类，将其子分类添加到数据列表中
                        for (StructNew.Category category : structNew.data) {
                            structData.addAll(category.children);
                            filteredStructData.addAll(category.children);
                        }
                        // 通知视图层显示过滤后的数据
                        view.displayData(filteredStructData);
                    } else {
                        // 通知视图层显示获取数据失败的错误信息
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

    public void filter(String text) {
        // 清空过滤后的数据列表
        filteredStructData.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始数据添加到过滤后的数据列表
            filteredStructData.addAll(structData);
        } else {
            // 遍历原始数据列表，将名称包含过滤文本的子分类添加到过滤后的数据列表
            for (StructNew.Category.SubCategory item : structData) {
                if (item.name.contains(text)) {
                    filteredStructData.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的数据
        view.displayData(filteredStructData);
    }


    public void onNavigationItemSelected(int itemId) {
        if (itemId == R.id.navigation_home) {

            view.navigateToMainActivity();
        } else if (itemId == R.id.navigation_structure) {

        } else if (itemId == R.id.navigation_director) {

            view.navigateToDirectorActivity();
        } else if (itemId == R.id.navigation_mine) {

            view.navigateToMineActivity();
        }
    }


    public void onStructArticleButtonClick() {
        view.navigateToStructArticle();
    }
}