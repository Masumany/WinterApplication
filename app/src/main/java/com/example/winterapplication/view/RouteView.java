package com.example.winterapplication.view;

import com.example.winterapplication.activity.RouteActivity.RouteNew;

import java.util.List;

public interface RouteView {
    void showLoading();

    void hideLoading();

    void displayData(List<RouteNew.Data> data);

    void showError(String message);

    void refreshList();

    void filterList(String text);

    void navigateToMainActivity();
}