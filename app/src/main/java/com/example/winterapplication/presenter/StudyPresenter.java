package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.model.StudyModel;
import com.example.winterapplication.news.StudyNew;
import com.example.winterapplication.view.StudyView;

import java.util.ArrayList;
import java.util.List;

public class StudyPresenter {

    private final StudyView view;

    private final StudyModel model;

    private final List<StudyNew.Data> studyData = new ArrayList<>();

    private final List<StudyNew.Data> filteredStudyData = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public StudyPresenter(StudyView view, StudyModel model) {
        this.view = view;
        this.model = model;
    }


    public void fetchData() {

        view.showLoading();
        // 调用模型层的方法获取学习数据
        model.fetchData(new StudyModel.StudyCallback() {
            @Override
            public void onSuccess(StudyNew studyNew) {
                // 使用 Handler 将任务切换到主线程执行
                mainHandler.post(() -> {
                    // 通知视图层隐藏加载状态
                    view.hideLoading();
                    // 检查获取的学习数据是否有效
                    if (studyNew != null && studyNew.data != null) {
                        // 清空原始学习数据列表
                        studyData.clear();
                        // 清空过滤后的学习数据列表
                        filteredStudyData.clear();
                        // 将新的学习数据添加到原始学习数据列表
                        studyData.addAll(studyNew.data);
                        // 将新的学习数据添加到过滤后的学习数据列表
                        filteredStudyData.addAll(studyNew.data);
                        // 通知视图层显示过滤后的学习数据
                        view.displayData(filteredStudyData);
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

    /**
     * 刷新学习数据
     */
    public void refreshData() {

        view.showLoading();

        view.refreshList();

        fetchData();
    }


    public void filter(String text) {
        // 清空过滤后的学习数据列表
        filteredStudyData.clear();
        if (text.isEmpty()) {
            // 如果过滤文本为空，将原始学习数据添加到过滤后的学习数据列表
            filteredStudyData.addAll(studyData);
        } else {
            // 遍历原始学习数据列表，将名称包含过滤文本的学习数据添加到过滤后的学习数据列表
            for (StudyNew.Data item : studyData) {
                if (item.name.contains(text)) {
                    filteredStudyData.add(item);
                }
            }
        }
        // 通知视图层显示过滤后的学习数据
        view.displayData(filteredStudyData);
    }


    public void onBackButtonClick() {
        view.navigateToMineActivity();
    }

    public void onItemClick(StudyNew.Data data) {
        view.navigateToStudyContentActivity(data.lisenseLink, data.name, data.desc);
    }
}