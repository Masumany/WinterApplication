package com.example.winterapplication.view;

import com.example.winterapplication.news.SquareNews;

import java.util.List;

public interface SquareView {
    void showLoading();
    void hideLoading();
    void displayData(List<SquareNews.Datas> data);
    void showError(String message);
    void refreshList();
    void filterList(String text);
    void navigateToMineActivity();
    void navigateToSquareContentActivity(String link, String title, String niceDate);
}