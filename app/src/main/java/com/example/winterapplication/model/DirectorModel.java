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
        // 获取 OkHttpClient 实例
        OkHttpClient client = getOkHttpClient();
        // 创建一个 GET 请求，请求的 URL 为 API_URL
        Request request = new Request.Builder().url(API_URL).get().build();
        // 异步发起网络请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 检查响应是否成功
                if (response.isSuccessful()) {
                    try {
                        // 获取响应体的字符串内容
                        String result = response.body().string();
                        // 处理响应结果
                        handleResponse(result, presenter);
                    } catch (IOException e) {
                        // 记录读取响应体失败的错误日志
                        Log.e("DirectorModel", "读取响应体失败: " + e.getMessage());
                        // 通知 Presenter 数据获取失败
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
        // 创建 Gson 实例，用于 JSON 数据的解析
        Gson gson = new Gson();
        try {
            // 将响应结果的字符串解析为 DirectorNews 对象
            DirectorNews directorNews = gson.fromJson(result, DirectorNews.class);
            // 检查解析结果是否有效
            if (directorNews != null && directorNews.getData() != null) {
                // 用于存储所有文章的列表
                List<DirectorNews.Articles> articlesList = new ArrayList<>();
                // 遍历 DirectorNews 中的每个 Data 对象
                for (DirectorNews.Data data : directorNews.getData()) {
                    // 将每个 Data 对象中的文章列表添加到 articlesList 中
                    articlesList.addAll(data.getArticles());
                }
                // 通知 Presenter 数据获取成功，并传递解析后的文章列表
                presenter.onFetchSuccess(articlesList);
            } else {
                // 若解析结果无效，通知 Presenter 数据解析失败
                presenter.onFetchFailed("数据解析失败");
            }
        } catch (JsonSyntaxException e) {

            Log.e("DirectorModel", "数据解析异常: " + e.getMessage());

            presenter.onFetchFailed("数据解析异常: " + e.getMessage());
        }
    }
}