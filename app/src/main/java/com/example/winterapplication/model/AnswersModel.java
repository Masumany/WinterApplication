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

    public static final String API_URL = "https://wanandroid.com/popular/wenda/json";

    private static OkHttpClient okHttpClient;


    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }


    public void fetchDataFromApi(AnswersPresenter presenter) {
        // 获取 OkHttpClient 实例
        OkHttpClient client = getOkHttpClient();
        // 创建一个 GET 请求
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

                        Log.e("AnswersModel", "读取响应体失败: " + e.getMessage());
                        // 通知 Presenter 数据获取失败
                        presenter.onFetchFailed("读取响应体失败: " + e.getMessage());
                    }
                } else {
                    // 若响应不成功，通知 Presenter 请求失败，并附带状态码信息
                    presenter.onFetchFailed("请求失败，状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("AnswersModel", "网络请求失败: " + e.getMessage());
                // 通知 Presenter 网络请求失败
                presenter.onFetchFailed("网络请求失败: " + e.getMessage());
            }
        });
    }


    private void handleResponse(String result, AnswersPresenter presenter) {
        // 创建 Gson 实例，用于 JSON 数据的解析
        Gson gson = new Gson();
        try {
            // 将响应结果的字符串解析为 AnswersNew 对象
            AnswersNew response = gson.fromJson(result, AnswersNew.class);
            // 检查解析结果是否有效
            if (response != null && response.data != null) {
                // 若解析结果有效，通知 Presenter 数据获取成功，并传递解析后的数据
                presenter.onFetchSuccess(response.data);
            } else {
                // 若解析结果无效，通知 Presenter 数据解析失败
                presenter.onFetchFailed("数据解析失败");
            }
        } catch (JsonSyntaxException e) {

            Log.e("AnswersModel", "数据解析异常: " + e.getMessage());

            presenter.onFetchFailed("数据解析异常: " + e.getMessage());
        }
    }
}