package com.example.winterapplication.news;

import java.util.List;

public class StructNew {
    public List<Category> data;

    public class Category {
        private String name;
        private int order;
        public List<SubCategory> children;

        public class SubCategory {
            public String name;
            private int id;
            private int courseId;
            private int parentChapterId;
            private int chapterSort;
            String title;
            int order;
            private boolean userControlSetTop;
            private int visible;
        }
    }
}
