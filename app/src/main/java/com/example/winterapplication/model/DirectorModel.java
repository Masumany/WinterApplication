package com.example.winterapplication.model;

import android.util.Log;

import com.example.winterapplication.activity.DirectorActivity.DirectorNews;
import com.example.winterapplication.presenter.DirectorPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectorModel {
    public static final String API_URL = "https://www.wanandroid.com/navi/json";
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public void fetchDataFromApi(DirectorPresenter presenter) {
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
                        Log.e("DirectorModel", "读取响应体失败: " + e.getMessage());
                        presenter.onFetchFailed("读取响应体失败: " + e.getMessage());
                    }
                } else {
                    presenter.onFetchFailed("请求失败，状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DirectorModel", "网络请求失败: " + e.getMessage());
                presenter.onFetchFailed("网络请求失败: " + e.getMessage());
            }
        });
    }

    private void handleResponse(String result, DirectorPresenter presenter) {
        Gson gson = new Gson();
        try {
            DirectorNews directorNews = gson.fromJson(result, DirectorNews.class);
            if (directorNews != null && directorNews.getData() != null) {
                List<DirectorNews.Articles> articlesList = new ArrayList<>();
                for (DirectorNews.Data data : directorNews.getData()) {
                    articlesList.addAll(data.getArticles());
                }
                presenter.onFetchSuccess(articlesList);
            } else {
                presenter.onFetchFailed("数据解析失败");
            }
        } catch (JsonSyntaxException e) {
            Log.e("DirectorModel", "数据解析异常: " + e.getMessage());
            presenter.onFetchFailed("数据解析异常: " + e.getMessage());
        }
    }
}