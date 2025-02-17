package com.example.winterapplication.news;

import java.util.List;

public class WebNew {
    public List<Data> data;
    private int errorCode;
    private String errorMsg;

    public static class Data {

        public String link;
        public String name;
        private String category;
        private String icon;
        private int id;
        private int order;
        private int visible;
    }
}
