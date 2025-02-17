package com.example.winterapplication.view;


import com.example.winterapplication.news.StructNew;

import java.util.List;

public interface StructView {
    void showLoading();

    void hideLoading();

    void displayData(List<StructNew.Category.SubCategory> data);

    void showError(String message);

    void refreshList();

    void filterList(String text);

    void navigateToMainActivity();

    void navigateToDirectorActivity();

    void navigateToMineActivity();

    void navigateToStructArticle();
}