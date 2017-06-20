package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by chenming on 2015/6/5.
 */
public class UserIdentifyPayInfo {

    /**
     * data : {"money":0.11,"integral":{"amount":2.6,"integral":"26","name":"优先使用积分支付（现有积分26，可抵2.6元）","id":"4"},"goods":{"jb_type":"1","user_id":"796991","id":"491479","charge_price":"20.00"},"paylist":[{"note":"用户财务中心余额支付","name":"余额支付","id":"0"},{"note":"银行卡用户快捷支付","name":"微信支付","id":"1"},{"note":"银行卡用户快捷支付","name":"银行卡支付","id":"2"}]}
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
         * money : 0.11
         * integral : {"amount":2.6,"integral":"26","name":"优先使用积分支付（现有积分26，可抵2.6元）","id":"4"}
         * goods : {"jb_type":"1","user_id":"796991","id":"491479","charge_price":"20.00"}
         * paylist : [{"note":"用户财务中心余额支付","name":"余额支付","id":"0"},{"note":"银行卡用户快捷支付","name":"微信支付","id":"1"},{"note":"银行卡用户快捷支付","name":"银行卡支付","id":"2"}]
         * payment_type : {"MALIPAY": 16,"WXPAY": 45,"BAIFUBAO-WAP": 15}
         * is_new_user : "0"
         */
        private double money;
        private IntegralEntity integral;
        private GoodsEntity goods;
        private List<PaylistEntity> paylist;
        private int is_new_user;
        private Payment_typeEntity payment_type;
        public Payment_typeEntity getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(Payment_typeEntity payment_type) {
            this.payment_type = payment_type;
        }



        public int getIs_new_user() {
            return is_new_user;
        }

        public void setIs_new_user(int is_new_user) {
            this.is_new_user = is_new_user;
        }


        public void setMoney(double money) {
            this.money = money;
        }

        public void setIntegral(IntegralEntity integral) {
            this.integral = integral;
        }

        public void setGoods(GoodsEntity goods) {
            this.goods = goods;
        }

        public void setPaylist(List<PaylistEntity> paylist) {
            this.paylist = paylist;
        }

        public double getMoney() {
            return money;
        }

        public IntegralEntity getIntegral() {
            return integral;
        }

        public GoodsEntity getGoods() {
            return goods;
        }

        public List<PaylistEntity> getPaylist() {
            return paylist;
        }

        public class IntegralEntity {
            /**
             * amount : 2.6
             * integral : 26
             * name : 优先使用积分支付（现有积分26，可抵2.6元）
             * id : 4
             */
            private double amount;
            private String integral;
            private String name;
            private String id;

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getAmount() {
                return amount;
            }

            public String getIntegral() {
                return integral;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }
        }

        public class GoodsEntity {
            /**
             * jb_type : 1
             * user_id : 796991
             * id : 491479
             * charge_price : 20.00
             */
            private String jb_type;
            private String user_id;
            private String id;
            private String charge_price;

            public void setJb_type(String jb_type) {
                this.jb_type = jb_type;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setCharge_price(String charge_price) {
                this.charge_price = charge_price;
            }

            public String getJb_type() {
                return jb_type;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getId() {
                return id;
            }

            public String getCharge_price() {
                return charge_price;
            }
        }

        public class PaylistEntity {
            /**
             * note : 用户财务中心余额支付
             * name : 余额支付
             * id : 0
             */
            private String note;
            private String name;
            private String id;

            public void setNote(String note) {
                this.note = note;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNote() {
                return note;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }
        }

        public class Payment_typeEntity {
            /**
             * "MALIPAY": 16,
             * "WXPAY": 45,
             * "BAIFUBAO-WAP": 15
             */
            private int MALIPAY;
            private int WXPAY;
            private int BAIFUBAO_WAP;

            public int getWXPAY() {
                return WXPAY;
            }

            public void setWXPAY(int WXPAY) {
                this.WXPAY = WXPAY;
            }

            public int getMALIPAY() {
                return MALIPAY;
            }

            public void setMALIPAY(int MALIPAY) {
                this.MALIPAY = MALIPAY;
            }

            public int getBAIFUBAO_WAP() {
                return BAIFUBAO_WAP;
            }

            public void setBAIFUBAO_WAP(int BAIFUBAO_WAP) {
                this.BAIFUBAO_WAP = BAIFUBAO_WAP;
            }
        }
    }
}
