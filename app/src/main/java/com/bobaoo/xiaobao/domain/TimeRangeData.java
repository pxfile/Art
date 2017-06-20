package com.bobaoo.xiaobao.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ameng on 2016/3/28.
 */
public class TimeRangeData implements Serializable{
    /**
     * data : [{"taoci":3,"zaxiang":1,"time":"9：00--10：00"},{"taoci":4,"zaxiang":10,"time":"10：00--11：00"},{"taoci":1,"zaxiang":3,"time":"11：00--12：00"},{"taoci":1,"zaxiang":12,"time":"14：00--15：00"},{"taoci":2,"zaxiang":1,"time":"15：00--16：00"}]
     * error : false
     * message :
     */
    public List<DataEntity> data;
    public boolean error;
    public String message;

    public static class DataEntity implements Serializable{
        /**
         * taoci : 3
         * zaxiang : 1
         * time : 9：00--10：00
         */
        public int taoci;
        public int zaxiang;
        public String time;
    }

//    public boolean error;
//    public ArrayList<TimeObj> data;
//    public String message;
//
//    public static class TimeObj implements Serializable{
//        public int taoci;
//        public int zaxiang;
//        public String time;
//    }
}
