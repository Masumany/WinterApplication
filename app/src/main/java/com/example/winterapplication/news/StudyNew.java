package com.example.winterapplication.news;

import java.util.List;

public class StudyNew {
    public List<Data> data;
    private int errorCode;
    private String errorMsg;
    public static class Data{

        private String author;
        private int courseId;
        public String cover;
        public String desc;
        private int id;
        private String lisense;
        public String lisenseLink;
        public String name;
        private int order;
        private int parentChapterId;
        private int type;
        private boolean userControlSetTop;
        private int visible;
    }
}