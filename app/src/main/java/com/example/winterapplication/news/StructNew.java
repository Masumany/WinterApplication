package com.example.winterapplication.news;

import java.util.List;

public class StructNew {
    public List<Category> data;

    public class Category {
        public List<SubCategory> children;
        private String name;
        private int order;

        public class SubCategory {
            public String name;
            String title;
            int order;
            private int id;
            private int courseId;
            private int parentChapterId;
            private int chapterSort;
            private boolean userControlSetTop;
            private int visible;
        }
    }
}
