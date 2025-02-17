package com.example.winterapplication.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.winterapplication.news.StudyNew;
import com.example.winterapplication.model.StudyModel;
import com.example.winterapplication.view.StudyView;

import java.util.ArrayList;
import java.util.List;

public class StudyPresenter {
    private StudyView view;
    private StudyModel model;
    private List<StudyNew.Data> studyData = new ArrayList<>();
    private List<StudyNew.Data> filteredStudyData = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public StudyPresenter(StudyView view, StudyModel model) {
        this.view = view;
        this.model = model;
    }

    public void fetchData() {
        view.showLoading();
        model.fetchData(new StudyModel.StudyCallback() {
            @Override
            public void onSuccess(StudyNew studyNew) {
                mainHandler.post(() -> {
                    view.hideLoading();
                    if (studyNew != null && studyNew.data != null) {
                        studyData.clear();
                        filteredStudyData.clear();
                        studyData.addAll(studyNew.data);
                        filteredStudyData.addAll(studyNew.data);
                        view.displayData(filteredStudyData);
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
        filteredStudyData.clear();
        if (text.isEmpty()) {
            filteredStudyData.addAll(studyData);
        } else {
            for (StudyNew.Data item : studyData) {
                if (item.name.contains(text)) {
                    filteredStudyData.add(item);
                }
            }
        }
        view.displayData(filteredStudyData);
    }

    public void onBackButtonClick() {
        view.navigateToMineActivity();
    }

    public void onItemClick(StudyNew.Data data) {
        view.navigateToStudyContentActivity(data.lisenseLink, data.name, data.desc);
    }
}