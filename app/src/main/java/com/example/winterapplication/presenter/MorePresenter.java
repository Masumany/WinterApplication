package com.example.winterapplication.presenter;

import com.example.winterapplication.view.MoreView;

public class MorePresenter {
    private MoreView view;

    public MorePresenter(MoreView view) {
        this.view = view;
    }

    public void onBackButtonClick() {
        view.navigateToMineActivity();
    }

    public void onGifClick() {
        // 这里可以添加 GIF 点击后的业务逻辑，暂时为空，可根据需求补充
        view.showToast("GIF 被点击了");
    }
}