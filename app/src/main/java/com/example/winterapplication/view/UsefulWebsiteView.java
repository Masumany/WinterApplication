package com.example.winterapplication.view;

import com.example.winterapplication.news.WebNew;

import java.util.List;

public interface UsefulWebsiteView {
    void showLoading();

    void hideLoading();

    void displayData(List<WebNew.Data> data);

    void showError(String message);

    void refreshList();

    void filterList(String text);

    void navigateToMainActivity();

    void navigateToUsefulWebsiteContent(String link, String name);
}