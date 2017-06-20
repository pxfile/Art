package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/15.
 */
public class UserChargeRecordData {


    /**
     * data : {"enter":[{"amount":5,"name":"","id":1,"time":"","gateway":""}],"out":[{"jb_type":"1","charge_type":"3","type_name":"","charge_name":"","charge_other":"","id":"492003","charge_price":"","charge_time":""}]}
     * error : false
     */
    private DataEntity data;
    private boolean error;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DataEntity getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public class DataEntity {
        /**
         * enter : [{"amount":5,"name":"","id":1,"time":"","gateway":""}]
         * out : [{"jb_type":"1","charge_type":"3","type_name":"","charge_name":"","charge_other":"","id":"492003","charge_price":"","charge_time":""}]
         */
        private List<EnterEntity> enter;
        private List<OutEntity> out;

        public void setEnter(List<EnterEntity> enter) {
            this.enter = enter;
        }

        public void setOut(List<OutEntity> out) {
            this.out = out;
        }

        public List<EnterEntity> getEnter() {
            return enter;
        }

        public List<OutEntity> getOut() {
            return out;
        }

        public class EnterEntity {
            /**
             * amount : 5
             * name :
             * id : 1
             * time :
             * gateway :
             */
            private String amount;
            private String name;
            private int id;
            private String time;
            private String gateway;

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setGateway(String gateway) {
                this.gateway = gateway;
            }

            public String getAmount() {
                return amount;
            }

            public String getName() {
                return name;
            }

            public int getId() {
                return id;
            }

            public String getTime() {
                return time;
            }

            public String getGateway() {
                return gateway;
            }
        }

        public class OutEntity {
            /**
             * jb_type : 1
             * charge_type : 3
             * type_name :
             * charge_name :
             * charge_other :
             * id : 492003
             * charge_price :
             * charge_time :
             */
            private String jb_type;
            private String charge_type;
            private String type_name;
            private String charge_name;
            private String charge_other;
            private String id;
            private String charge_price;
            private String charge_time;

            public void setJb_type(String jb_type) {
                this.jb_type = jb_type;
            }

            public void setCharge_type(String charge_type) {
                this.charge_type = charge_type;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public void setCharge_name(String charge_name) {
                this.charge_name = charge_name;
            }

            public void setCharge_other(String charge_other) {
                this.charge_other = charge_other;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setCharge_price(String charge_price) {
                this.charge_price = charge_price;
            }

            public void setCharge_time(String charge_time) {
                this.charge_time = charge_time;
            }

            public String getJb_type() {
                return jb_type;
            }

            public String getCharge_type() {
                return charge_type;
            }

            public String getType_name() {
                return type_name;
            }

            public String getCharge_name() {
                return charge_name;
            }

            public String getCharge_other() {
                return charge_other;
            }

            public String getId() {
                return id;
            }

            public String getCharge_price() {
                return charge_price;
            }

            public String getCharge_time() {
                return charge_time;
            }
        }
    }
}
