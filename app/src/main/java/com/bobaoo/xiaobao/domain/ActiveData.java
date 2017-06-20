package com.bobaoo.xiaobao.domain;

import java.io.Serializable;

/**
 * Created by Ameng on 2016/3/21.
 */
public class ActiveData implements Serializable{
    public boolean error;
    public String message;
    public DataMessage data;

    public static class DataMessage implements Serializable{
        public String display;
        public String href;
        public String image;
        public String note;
        public String title;
        public int height;
        public int width;
    }

    public ActiveData(boolean error, String message, DataMessage data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }
}
