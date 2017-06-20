package com.bobaoo.xiaobao.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ameng on 2016/3/25.
 */
public class MeetData implements Serializable{
    public boolean error;
    public String message;
    public DetailData data;

    public static class DetailData implements Serializable{
        public String addtime;
        public String banner;
        public String count;
        public String end_time;
        public String id;
        public String kind_name;
        public String location;
        public String m_d;
        public String msg;
        public String price;
        public String scene;
        public String reg_ad;
        public String stage;
        public String stage_name;
        public String start_time;
        public String tel;
        public String time;
        public int timeout;
        public String tip;
        public String to_msg;
        public String year;
        public String contact;
        public ArrayList<Expert> expert;
        public ArrayList<Kind> kind;
        public ArrayList<String> mobile;
        public ArrayList<String> process;
        public ArrayList<String> photo;
    }

    public static class Expert implements Serializable{
        public String head_img;
        public String honors;
        public String id;
        public String intro;
        public String name;
    }

    public static class Kind implements Serializable{
        public String id;
        public String name;
    }
}
