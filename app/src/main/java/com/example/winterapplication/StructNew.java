package com.example.winterapplication;

import java.util.List;

class WanAndroidTreeData {
    private List<Category> data;
    private int errorCode;
    private String errorMsg;

    static class Category {
        private int id;
        String name;
        private int order;
        private int parentChapterId;
        private boolean userControlSetTop;
        private int visible;
        private List<SubCategory> children;

        static class SubCategory {
            private int id;
            private int courseId;
            private int parentChapterId;
            private int chapterSort;
            private String title;
            int order;
            private boolean userControlSetTop;
            private int visible;
        }
    }
}