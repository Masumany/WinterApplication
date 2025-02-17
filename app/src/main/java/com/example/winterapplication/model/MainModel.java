package com.example.winterapplication.model;

import com.example.winterapplication.activity.MainActivity.News;
import com.example.winterapplication.activity.MainActivity.MainBannerNew;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainModel {
    private static final String NEWS_URL = "https://www.wanandroid.com/article/list/1/json";
    private static final String BANNER_URL = "https://www.wanandroid.com/banner/json";
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public void fetchNewsData(final NewsCallback callback) {
        Request request = new Request.Builder()
                .url(NEWS_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        News news = gson.fromJson(result, News.class);
                        callback.onSuccess(news);
                    } catch (JsonSyntaxException e) {
                        callback.onFailure("JSON 解析失败：" + e.getMessage());
                    }
                } else {
                    callback.onFailure("网络请求失败，状态码：" + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("网络请求失败：" + e.getMessage());
            }
        });
    }

    public void fetchBannerData(final BannerCallback callback) {
        Request request = new Request.Builder()
                .url(BANNER_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        MainBannerNew mainBannerNew = gson.fromJson(result, MainBannerNew.class);
                        callback.onSuccess(mainBannerNew);
                    } catch (JsonSyntaxException e) {
                        callback.onFailure("JSON 解析失败：" + e.getMessage());
                    }
                } else {
                    callback.onFailure("网络请求失败，状态码：" + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("网络请求失败：" + e.getMessage());
            }
        });
    }

    public interface NewsCallback {
        void onSuccess(News news);
        void onFailure(String errorMessage);
    }

    public interface BannerCallback {
        void onSuccess(MainBannerNew mainBannerNew);
        void onFailure(String errorMessage);
    }
}