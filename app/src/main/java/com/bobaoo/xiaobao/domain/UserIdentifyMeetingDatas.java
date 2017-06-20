package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/4.
 */
public class UserIdentifyMeetingDatas {

    /**
     * error : false
     * data : [{"id":"92639","name":"第五十二届古玩鉴定会","expert":"李知宴 宋海洋","kind":"陶瓷、杂项、玉器","time":"2016年04月16日","location":"北京市海淀区北蜂窝三号博宝艺术网","taoci":{"count":0,"number":""},"zaxiang":{"count":1,"number":"0"},"price":300,"apply":"9:00--10:00","stage":"52","timeout":1396710,"send_count":0,"tel":"13141114229","tip":"本次活动结束时间为2016年04月16日下午15点，请确定您能在此时间之前到达活动现场。","end":1460790000}]
     * message :
     */

    private boolean error;
    private String message;
    /**
     * id : 92639
     * name : 第五十二届古玩鉴定会
     * expert : 李知宴 宋海洋
     * kind : 陶瓷、杂项、玉器
     * time : 2016年04月16日
     * location : 北京市海淀区北蜂窝三号博宝艺术网
     * taoci : {"count":0,"number":""}
     * zaxiang : {"count":1,"number":"0"}
     * price : 300
     * apply : 9:00--10:00
     * stage : 52
     * timeout : 1396710
     * send_count : 0
     * tel : 13141114229
     * tip : 本次活动结束时间为2016年04月16日下午15点，请确定您能在此时间之前到达活动现场。
     * end : 1460790000
     */

    private List<DataEntity> data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String id;
        private String name;
        private String expert;
        private String kind;
        private String time;
        private String location;
        /**
         * count : 0
         * number :
         */

        private TaociEntity taoci;
        /**
         * count : 1
         * number : 0
         */

        private ZaxiangEntity zaxiang;
        private int price;
        private String apply;
        private String stage;
        private int timeout;
        private int send_count;
        private String tel;
        private String tip;
        private int end;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExpert() {
            return expert;
        }

        public void setExpert(String expert) {
            this.expert = expert;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public TaociEntity getTaoci() {
            return taoci;
        }

        public void setTaoci(TaociEntity taoci) {
            this.taoci = taoci;
        }

        public ZaxiangEntity getZaxiang() {
            return zaxiang;
        }

        public void setZaxiang(ZaxiangEntity zaxiang) {
            this.zaxiang = zaxiang;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getApply() {
            return apply;
        }

        public void setApply(String apply) {
            this.apply = apply;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getSend_count() {
            return send_count;
        }

        public void setSend_count(int send_count) {
            this.send_count = send_count;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public static class TaociEntity {
            private int count;
            private String number;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }

        public static class ZaxiangEntity {
            private int count;
            private String number;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }
    }
}
