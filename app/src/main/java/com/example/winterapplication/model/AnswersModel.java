package com.example.winterapplication.model;

import android.util.Log;

import com.example.winterapplication.news.AnswersNew;
import com.example.winterapplication.presenter.AnswersPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnswersModel {
    // 建议将 API 地址配置在一个单独的配置文件或常量类中
    public static final String API_URL = "https://wanandroid.com/popular/wenda/json";
    private static OkHttpClient okHttpClient;

    // 获取 OkHttpClient 实例，使用单例模式提高复用性
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public void fetchDataFromApi(AnswersPresenter presenter) {
        OkHttpClient client = getOkHttpClient();
        Request request = new Request.Builder()
                .url(API_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        handleResponse(result, presenter);
                    } catch (IOException e) {
                        Log.e("AnswersModel", "读取响应体失败: " + e.getMessage());
                        presenter.onFetchFailed("读取响应体失败: " + e.getMessage());
                    }
                } else {
                    presenter.onFetchFailed("请求失败，状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AnswersModel", "网络请求失败: " + e.getMessage());
                presenter.onFetchFailed("网络请求失败: " + e.getMessage());
            }
        });
    }

    private void handleResponse(String result, AnswersPresenter presenter) {
        Gson gson = new Gson();
        try {
            AnswersNew response = gson.fromJson(result, AnswersNew.class);
            if (response != null && response.data != null) {
                presenter.onFetchSuccess(response.data);
            } else {
                presenter.onFetchFailed("数据解析失败");
            }
        } catch (JsonSyntaxException e) {
            Log.e("AnswersModel", "数据解析异常: " + e.getMessage());
            presenter.onFetchFailed("数据解析异常: " + e.getMessage());
        }
    }
}