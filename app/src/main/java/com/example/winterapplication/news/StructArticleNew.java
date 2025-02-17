package com.example.winterapplication.news;

import java.util.List;

public class StructArticleNew {
    public Data data;
    private int errorCode;
    private String errorMsg;


    public static class Data {
        private int curPage;
        public List<Article> datas;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;


        public static class Article {
            private String apkLink;
            private int audit;
            String author;
            private boolean canEdit;
            private int chapterId;
            String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String descMd;
            private String envelopePic;
            private boolean fresh;
            private int id;
            public String link;
            public String niceDate;
            private String niceShareDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime;
            private int realSuperChapterId;
            private int selfVisible;
            private long shareDate;
            public String shareUser;
            private int superChapterId;
            private String superChapterName;
            private List<Tag> tags;
            public String title;
            private int type;
            private int userId;
            private int visible;
            public int zan;


            static class Tag {
                private String name;
                private String url;
            }
        }
    }
}
