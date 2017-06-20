package com.bobaoo.xiaobao.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ameng on 2016/3/30.
 */
public class IdentifySkillData implements Serializable{
    /**
     * data : [{"count":0,"name":"陶瓷","id":"1"},{"count":1,"name":"玉器","id":"2"},{"count":0,"name":"书画","id":"3"},{"count":0,"name":"铜器","id":"4"},{"count":0,"name":"钱币","id":"5"},{"count":0,"name":"木器","id":"6"},{"count":0,"name":"杂项","id":"7"}]
     * error : false
     * message :
     */
    public List<DataEntity> data;
    public boolean error;
    public String message;

    public static class DataEntity implements Serializable{
        /**
         * count : 0
         * name : 陶瓷
         * id : 1
         */
        public int count;
        public String name;
        public String id;
    }
}
