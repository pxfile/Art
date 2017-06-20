package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by star on 16/3/28.
 */
public class UserIdentifyMeetPayInfo {

    /**
     * error : false
     * data : {"user_id":"874964","price":300,"taoci":0,"zaxiang":1,"money":0.86,"paylist":[{"id":"0","name":"余额支付","note":"用户财务中心余额支付"},{"id":"1","name":"微信支付","note":"银行卡用户快捷支付"},{"id":"2","name":"银行卡支付","note":"银行卡用户快捷支付"}],"is_new_user":0,"payment_type":{"malipay":11,"wxpay":16,"baifubao_wap":0}}
     * message :
     */

    private boolean error;
    /**
     * user_id : 874964
     * price : 300
     * taoci : 0
     * zaxiang : 1
     * money : 0.86
     * paylist : [{"id":"0","name":"余额支付","note":"用户财务中心余额支付"},{"id":"1","name":"微信支付","note":"银行卡用户快捷支付"},{"id":"2","name":"银行卡支付","note":"银行卡用户快捷支付"}]
     * is_new_user : 0
     * payment_type : {"malipay":11,"wxpay":16,"baifubao_wap":0}
     */

    private DataEntity data;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class DataEntity {
        private String user_id;
        private double price;
        private int taoci;
        private int zaxiang;
        private double money;
        private int is_new_user;
        /**
         * malipay : 11
         * wxpay : 16
         * baifubao_wap : 0
         */

        private PaymentTypeEntity payment_type;
        /**
         * id : 0
         * name : 余额支付
         * note : 用户财务中心余额支付
         */

        private List<PaylistEntity> paylist;

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setTaoci(int taoci) {
            this.taoci = taoci;
        }

        public void setZaxiang(int zaxiang) {
            this.zaxiang = zaxiang;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public void setIs_new_user(int is_new_user) {
            this.is_new_user = is_new_user;
        }

        public void setPayment_type(PaymentTypeEntity payment_type) {
            this.payment_type = payment_type;
        }

        public void setPaylist(List<PaylistEntity> paylist) {
            this.paylist = paylist;
        }

        public String getUser_id() {
            return user_id;
        }

        public double getPrice() {
            return price;
        }

        public int getTaoci() {
            return taoci;
        }

        public int getZaxiang() {
            return zaxiang;
        }

        public double getMoney() {
            return money;
        }

        public int getIs_new_user() {
            return is_new_user;
        }

        public PaymentTypeEntity getPayment_type() {
            return payment_type;
        }

        public List<PaylistEntity> getPaylist() {
            return paylist;
        }

        public static class PaymentTypeEntity {
            private int malipay;
            private int wxpay;
            private int baifubao_wap;

            public void setMalipay(int malipay) {
                this.malipay = malipay;
            }

            public void setWxpay(int wxpay) {
                this.wxpay = wxpay;
            }

            public void setBaifubao_wap(int baifubao_wap) {
                this.baifubao_wap = baifubao_wap;
            }

            public int getMalipay() {
                return malipay;
            }

            public int getWxpay() {
                return wxpay;
            }

            public int getBaifubao_wap() {
                return baifubao_wap;
            }
        }

        public static class PaylistEntity {
            private String id;
            private String name;
            private String note;

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getNote() {
                return note;
            }
        }
    }
}
