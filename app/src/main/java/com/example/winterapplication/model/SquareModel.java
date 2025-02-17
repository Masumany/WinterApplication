package com.example.winterapplication.model;

import com.example.winterapplication.news.SquareNews;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SquareModel {

    private static final String API_URL = "https://wanandroid.com/user_article/list/0/json";
    // 创建 OkHttpClient 实例，用于发起网络请求
    private final OkHttpClient client = new OkHttpClient();
    // 创建 Gson 实例，用于 JSON 数据的解析
    private final Gson gson = new Gson();


    public void fetchData(final SquareCallback callback) {
        // 创建一个 GET 请求，请求的 URL 为 API_URL
        Request request = new Request.Builder().url(API_URL).get().build();
        // 异步发起网络请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 检查响应是否成功
                if (response.isSuccessful()) {
                    // 获取响应体的字符串内容
                    String result = response.body().string();
                    // 将响应结果的字符串解析为 SquareNews 对象
                    SquareNews squareNews = gson.fromJson(result, SquareNews.class);
                    // 通知回调接口数据获取成功，并传递解析后的 SquareNews 对象
                    callback.onSuccess(squareNews);
                } else {
                    // 若响应不成功，通知回调接口网络请求失败，并附带状态码信息
                    callback.onFailure("网络请求失败，状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 若网络请求失败，通知回调接口网络请求失败，并传递错误信息
                callback.onFailure("网络请求失败: " + e.getMessage());
            }
        });
    }


    public interface SquareCallback {

        void onSuccess(SquareNews squareNews);


        void onFailure(String errorMessage);
    }
}