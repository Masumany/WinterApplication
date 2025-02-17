package com.example.winterapplication.presenter;

import com.example.winterapplication.view.MoreView;

public class MorePresenter {

    private final MoreView view;


    public MorePresenter(MoreView view) {
        this.view = view;
    }


    public void onBackButtonClick() {
        view.navigateToMineActivity();
    }


    public void onGifClick() {
        view.showToast("期待！！！");
    }
}