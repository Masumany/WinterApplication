package com.example.winterapplication.news;

import java.util.List;

public class SquareNews {
    public SquareNews.Data data;
    private int errorCode;
    private String errorMsg;

    public static class Data {
        public List<SquareNews.Datas> datas;
        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
    }

    public static class Datas {
        public String link;
        public String niceDate;
        public String title;
        private boolean adminAdd;
        private String apkLink;
        private int audit;
        private String author;
        private boolean canEdit;
        private int chapterId;
        private String chapterName;
        private boolean collect;
        private int courseId;
        private String desc;
        private String descMd;
        private String envelopePic;
        private boolean fresh;
        private String host;
        private int id;
        private boolean isAdminAdd;
        private String niceShareData;
        private String origin;
        private String prefix;
        private String projectLink;
        private long publishTime;
        private int realSuperChapterId;
        private int selfVisible;
        private long shareDate;
        private String shareUser;
        private int superChapterId;
        private String superChapterName;
        private int type;
        private int userId;
        private int visible;
        private int zan;

    }


}