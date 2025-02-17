package com.example.winterapplication.model;

import com.example.winterapplication.news.StructNew;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StructModel {
    private static final String API_URL = "https://www.wanandroid.com/tree/json";
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public void fetchData(final StructCallback callback) {
        Request request = new Request.Builder()
                .url(API_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    StructNew structNew = gson.fromJson(result, StructNew.class);
                    callback.onSuccess(structNew);
                } else {
                    callback.onFailure("网络请求失败，状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("网络请求失败: " + e.getMessage());
            }
        });
    }

    public interface StructCallback {
        void onSuccess(StructNew structNew);
        void onFailure(String errorMessage);
    }
}