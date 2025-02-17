package com.example.winterapplication.news;

import java.util.List;

public class StructArticleNew {
    public Data data;
    private int errorCode;
    private String errorMsg;


    public static class Data {
        public List<Article> datas;
        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;


        public static class Article {
            public String link;
            public String niceDate;
            public String shareUser;
            public String title;
            public int zan;
            String author;
            String chapterName;
            private String apkLink;
            private int audit;
            private boolean canEdit;
            private int chapterId;
            private boolean collect;
            private int courseId;
            private String desc;
            private String descMd;
            private String envelopePic;
            private boolean fresh;
            private int id;
            private String niceShareDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime;
            private int realSuperChapterId;
            private int selfVisible;
            private long shareDate;
            private int superChapterId;
            private String superChapterName;
            private List<Tag> tags;
            private int type;
            private int userId;
            private int visible;

            static class Tag {
                private String name;
                private String url;
            }
        }
    }
}
