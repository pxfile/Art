package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by kakaxicm on 2015/12/15.
 */
public class DealRecordReponse {

    /**
     * error : false
     * data : [{"from":"out","time":"2015-10-16","price":"","type_name":"急速鉴定","note":"支付0.00元使用0积分"},{"from":"enter","time":"2015-08-12","price":"0.01","type_name":"百付宝","note":"使用百付宝充值0.01元"},{"from":"enter","time":"2015-08-12","price":"0.01","type_name":"支付宝","note":"使用支付宝充值0.01元"},{"from":"enter","time":"2015-08-12","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-20","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-20","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-20","price":"0.01","type_name":"支付宝","note":"使用支付宝充值0.01元"},{"from":"enter","time":"2015-07-20","price":"0.01","type_name":"百付宝","note":"使用百付宝充值0.01元"},{"from":"enter","time":"2015-07-20","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"支付宝","note":"使用支付宝充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"out","time":"2015-07-17","price":"","type_name":"普通鉴定","note":"支付0.00元使用41积分"},{"from":"enter","time":"2015-07-17","price":"0.9","type_name":"百付宝","note":"使用百付宝充值0.9元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"支付宝","note":"使用支付宝充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"支付宝","note":"使用支付宝充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"支付宝","note":"使用支付宝充值0.01元"},{"from":"out","time":"2015-07-17","price":"","type_name":"普通鉴定","note":"支付0.00元使用50积分"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"百付宝","note":"使用百付宝充值0.01元"},{"from":"enter","time":"2015-07-17","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"enter","time":"2015-07-16","price":"0.1","type_name":"支付宝","note":"使用支付宝充值0.1元"},{"from":"enter","time":"2015-06-19","price":"0.01","type_name":"微信","note":"使用微信充值0.01元"},{"from":"out","time":"2015-06-01","price":"","type_name":"普通鉴定","note":"支付0.00元使用50积分"},{"from":"out","time":"2015-05-04","price":"","type_name":"普通鉴定","note":"支付0.00元使用50积分"}]
     * message :
     */

    private boolean error;
    private String message;
    /**
     * from : out
     * time : 2015-10-16
     * price :
     * type_name : 急速鉴定
     * note : 支付0.00元使用0积分
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
        private String from;
        private String time;
        private String price;
        private String type_name;
        private String note;

        public void setFrom(String from) {
            this.from = from;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getFrom() {
            return from;
        }

        public String getTime() {
            return time;
        }

        public String getPrice() {
            return price;
        }

        public String getType_name() {
            return type_name;
        }

        public String getNote() {
            return note;
        }
    }
}
