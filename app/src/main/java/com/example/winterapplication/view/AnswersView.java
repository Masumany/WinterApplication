package com.example.winterapplication.view;

import com.example.winterapplication.news.AnswersNew;

import java.util.List;

public interface AnswersView {
    void showLoading();

    void hideLoading();

    void displayAnswers(List<AnswersNew.Data> data);

    void showError(String message);

    void refreshList();

    void filterList(String text);
}