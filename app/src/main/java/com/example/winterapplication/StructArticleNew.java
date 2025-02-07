package com.example.winterapplication;

import java.util.List;

class WanAndroidArticleResponse {
    Data data;
    private int errorCode;
    private String errorMsg;

    static class Data {
        private int curPage;
        List<Article> datas;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;

        static class Article {
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
            String link;
            String niceDate;
            private String niceShareDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime;
            private int realSuperChapterId;
            private int selfVisible;
            long shareDate;
            String shareUser;
            private int superChapterId;
            private String superChapterName;
            private List<Tag> tags;
            String title;
            private int type;
            private int userId;
            private int visible;
            int zan;

            static class Tag {
                private String name;
                private String url;
            }
        }
    }
}