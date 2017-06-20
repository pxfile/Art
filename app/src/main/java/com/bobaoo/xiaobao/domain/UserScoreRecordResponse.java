package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by kakaxicm on 2015/12/14.
 */
public class UserScoreRecordResponse {

    /**
     * error : false
     * data : [{"id":"133467","state":"0","useid":"513404","uid":"914297","note":"鉴定：513404,获得5个积分","uuid":"","obtain":"鉴定","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"5","is_delete":"0","jb_type":"普通鉴定"},{"id":"133466","state":"1","useid":"513404","uid":"914297","note":"支付：513404，使用积分 50，支付金额 ￥0元","uuid":"","obtain":"支付","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"50","is_delete":"0","jb_type":"普通鉴定"},{"id":"133463","state":"0","useid":"513403","uid":"914297","note":"鉴定：513403,获得5个积分","uuid":"","obtain":"鉴定","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"5","is_delete":"0","jb_type":"普通鉴定"},{"id":"133462","state":"1","useid":"513403","uid":"914297","note":"支付：513403，使用积分 50，支付金额 ￥0元","uuid":"","obtain":"支付","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"50","is_delete":"0","jb_type":"普通鉴定"},{"id":"133460","state":"0","useid":"513402","uid":"914297","note":"鉴定：513402,获得5个积分","uuid":"","obtain":"鉴定","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"5","is_delete":"0","jb_type":"普通鉴定"},{"id":"133459","state":"1","useid":"513402","uid":"914297","note":"支付：513402，使用积分 50，支付金额 ￥0元","uuid":"","obtain":"支付","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"50","is_delete":"0","jb_type":"普通鉴定"},{"id":"133456","state":"0","useid":"513397","uid":"914297","note":"鉴定：513397,获得5个积分","uuid":"","obtain":"鉴定","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"5","is_delete":"0","jb_type":"普通鉴定"},{"id":"133455","state":"1","useid":"513397","uid":"914297","note":"支付：513397，使用积分 50，支付金额 ￥0元","uuid":"","obtain":"支付","addtime":"2015-12-14","unadd":"","type":"普通鉴定","num":"50","is_delete":"0","jb_type":"普通鉴定"}]
     * message :
     */

    private boolean error;
    private String message;
    /**
     * id : 133467
     * state : 0
     * useid : 513404
     * uid : 914297
     * note : 鉴定：513404,获得5个积分
     * uuid :
     * obtain : 鉴定
     * addtime : 2015-12-14
     * unadd :
     * type : 普通鉴定
     * num : 5
     * is_delete : 0
     * jb_type : 普通鉴定
     */

    private List<DataEntity> data;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String state;
        private String useid;
        private String uid;
        private String note;
        private String uuid;
        private String obtain;
        private String addtime;
        private String unadd;
        private String type;
        private String num;
        private String is_delete;
        private String jb_type;

        public void setId(String id) {
            this.id = id;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setUseid(String useid) {
            this.useid = useid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public void setObtain(String obtain) {
            this.obtain = obtain;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setUnadd(String unadd) {
            this.unadd = unadd;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }

        public void setJb_type(String jb_type) {
            this.jb_type = jb_type;
        }

        public String getId() {
            return id;
        }

        public String getState() {
            return state;
        }

        public String getUseid() {
            return useid;
        }

        public String getUid() {
            return uid;
        }

        public String getNote() {
            return note;
        }

        public String getUuid() {
            return uuid;
        }

        public String getObtain() {
            return obtain;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getUnadd() {
            return unadd;
        }

        public String getType() {
            return type;
        }

        public String getNum() {
            return num;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public String getJb_type() {
            return jb_type;
        }
    }
}
