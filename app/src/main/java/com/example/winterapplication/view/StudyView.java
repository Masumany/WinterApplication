package com.example.winterapplication.view;


import com.example.winterapplication.news.StudyNew;

import java.util.List;

public interface StudyView {
    void showLoading();
    void hideLoading();
    void displayData(List<StudyNew.Data> data);
    void showError(String message);
    void refreshList();
    void filterList(String text);
    void navigateToMineActivity();
    void navigateToStudyContentActivity(String link, String name, String desc);
}