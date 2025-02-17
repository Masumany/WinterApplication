package com.example.winterapplication.view;
import com.example.winterapplication.news.StructArticleNew;


import java.util.List;

public interface StructArticleView {
    void showLoading();
    void hideLoading();
    void displayArticles(List<StructArticleNew.Data.Article> articles);
    void showError(String message);
    void refreshList();
    void filterArticles(String text);
    void navigateToStructActivity();
    void navigateToStructContentActivity(String link, String title, String author, String niceDate);
}