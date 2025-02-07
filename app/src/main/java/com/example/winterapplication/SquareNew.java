package com.example.winterapplication;

import java.util.List;

class SquareNew {
    private  int errorCode;
    private  String errorMsg;
    Data data;

    static class Data{
        public String title;
        private   int curPage;
        List<Datas> datas;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
    }

    static class Datas{
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

        String link;
        String niceDate;
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
        String title;
        private int type;
        private int userId;
        private int visible;
        private int zan;

    }

}
