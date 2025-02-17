package com.example.winterapplication.news;

import java.util.List;

public class WebNew{
    public List<Data> data;
    private int errorCode;
    private String errorMsg;
    public static class Data{

        private String category;
        private String icon;
        public String link;
        public String name;
        private int id;
        private int order;
        private int visible;
    }
}
