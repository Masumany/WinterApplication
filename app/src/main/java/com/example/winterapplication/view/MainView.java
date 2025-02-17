package com.example.winterapplication.view;

import com.example.winterapplication.activity.MainActivity.News;
import com.example.winterapplication.activity.MainActivity.MainBannerNew;

import java.util.List;

public interface MainView {
    void showLoading();
    void hideLoading();
    void displayNews(List<News.Data.Datas> news);
    void displayBanner(List<MainBannerNew.Data> banners);
    void showError(String message);
    void refreshList();
    void filterList(String text);
}