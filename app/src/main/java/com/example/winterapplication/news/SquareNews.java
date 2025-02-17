package com.example.winterapplication.news;

import java.util.List;

public class SquareNews{
    private  int errorCode;
    private  String errorMsg;
    public SquareNews.Data data;

    public static class Data{
        private   int curPage;
        public List<SquareNews.Datas> datas;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
    }

    public static class Datas{
        private boolean adminAdd;
        private String apkLink;
        private int audit;
        private String author;
        private boolean canEdit;
        private int chapterId;
        private String chapterName;
        private  boolean collect;
        private  int courseId;
        private String desc;
        private String descMd;
        private String envelopePic;

        private  boolean fresh;
        private String host;
        private  int id;
        private  boolean isAdminAdd;

        public String link;
        public String niceDate;
        private String niceShareData;
        private String origin;
        private String prefix;
        private String projectLink;
        private long publishTime;
        private int realSuperChapterId;
        private int selfVisible;
        private long shareDate;
        private  String shareUser;
        private int superChapterId;
        private  String superChapterName;
        public String title;
        private int type;
        private int userId;
        private int visible;
        private int zan;

    }


}