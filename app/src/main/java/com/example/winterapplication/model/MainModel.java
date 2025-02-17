package com.example.winterapplication.model;

import com.example.winterapplication.activity.MainActivity.MainBannerNew;
import com.example.winterapplication.activity.MainActivity.News;
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

    private final OkHttpClient client = new OkHttpClient();
    // 创建 Gson 实例，用于 JSON 数据的解析
    private final Gson gson = new Gson();


    public void fetchNewsData(final NewsCallback callback) {
        // 创建一个 GET 请求，请求的 URL 为 NEWS_URL
        Request request = new Request.Builder().url(NEWS_URL).get().build();
        // 异步发起网络请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 检查响应是否成功
                if (response.isSuccessful()) {
                    // 获取响应体的字符串内容
                    String result = response.body().string();
                    try {
                        // 将响应结果的字符串解析为 News 对象
                        News news = gson.fromJson(result, News.class);
                        // 通知回调接口数据获取成功，并传递解析后的 News 对象
                        callback.onSuccess(news);
                    } catch (JsonSyntaxException e) {
                        // 若解析失败，通知回调接口解析失败，并传递错误信息
                        callback.onFailure("JSON 解析失败：" + e.getMessage());
                    }
                } else {
                    // 若响应不成功，通知回调接口网络请求失败，并附带状态码信息
                    callback.onFailure("网络请求失败，状态码：" + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 若网络请求失败，通知回调接口网络请求失败，并传递错误信息
                callback.onFailure("网络请求失败：" + e.getMessage());
            }
        });
    }


    public void fetchBannerData(final BannerCallback callback) {
        // 创建一个 GET 请求，请求的 URL 为 BANNER_URL
        Request request = new Request.Builder().url(BANNER_URL).get().build();
        // 异步发起网络请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 检查响应是否成功
                if (response.isSuccessful()) {
                    // 获取响应体的字符串内容
                    String result = response.body().string();
                    try {
                        // 将响应结果的字符串解析为 MainBannerNew 对象
                        MainBannerNew mainBannerNew = gson.fromJson(result, MainBannerNew.class);
                        // 通知回调接口数据获取成功，并传递解析后的 MainBannerNew 对象
                        callback.onSuccess(mainBannerNew);
                    } catch (JsonSyntaxException e) {
                        // 若解析失败，通知回调接口解析失败，并传递错误信息
                        callback.onFailure("JSON 解析失败：" + e.getMessage());
                    }
                } else {
                    // 若响应不成功，通知回调接口网络请求失败，并附带状态码信息
                    callback.onFailure("网络请求失败，状态码：" + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 若网络请求失败，通知回调接口网络请求失败，并传递错误信息
                callback.onFailure("网络请求失败：" + e.getMessage());
            }
        });
    }


    //新闻数据获取结果的回调接口。

    public interface NewsCallback {

        void onSuccess(News news);


        void onFailure(String errorMessage);
    }


    public interface BannerCallback {

        void onSuccess(MainBannerNew mainBannerNew);


        void onFailure(String errorMessage);
    }
}