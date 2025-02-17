package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.R;
import com.example.winterapplication.news.StructNew;
import com.example.winterapplication.model.StructModel;
import com.example.winterapplication.view.StructView;

import java.util.ArrayList;
import java.util.List;

public class StructPresenter {
    private StructView view;
    private StructModel model;
    private List<StructNew.Category.SubCategory> structData = new ArrayList<>();
    private List<StructNew.Category.SubCategory> filteredStructData = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public StructPresenter(StructView view, StructModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {
        view.showLoading();
        model.fetchData(new StructModel.StructCallback() {
            @Override
            public void onSuccess(StructNew structNew) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (structNew != null && structNew.data != null) {
                        structData.clear();
                        filteredStructData.clear();
                        for (StructNew.Category category : structNew.data) {
                            structData.addAll(category.children);
                            filteredStructData.addAll(category.children);
                        }
                        view.displayData(filteredStructData);
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

    public void filter(String text) {
        filteredStructData.clear();
        if (text.isEmpty()) {
            filteredStructData.addAll(structData);
        } else {
            for (StructNew.Category.SubCategory item : structData) {
                if (item.name.contains(text)) {
                    filteredStructData.add(item);
                }
            }
        }
        view.displayData(filteredStructData);
    }

    public void onNavigationItemSelected(int itemId) {
        if (itemId == R.id.navigation_home) {
            view.navigateToMainActivity();
        } else if (itemId == R.id.navigation_structure) {
            // 当前就是 StructActivity，无需处理
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