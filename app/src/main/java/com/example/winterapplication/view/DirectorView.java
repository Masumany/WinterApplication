package com.example.winterapplication.view;

import com.example.winterapplication.activity.DirectorActivity.DirectorNews;

import java.util.List;

public interface DirectorView {
    void showLoading();

    void hideLoading();

    void displayNews(List<DirectorNews.Articles> data);

    void showError(String message);

    void refreshList();

    void filterList(String text);
}