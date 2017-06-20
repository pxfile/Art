package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/4.
 */
public class UserIdentifyDatas {


    /**
     * error : false
     * data : [{"id":"498705","name":"","photo":"http://c2c.ig365.cn/data/files/old_shop/gh_722/store_883722/jianbao/20150827/201508271826516102.jpg@400w_400h","state":"1","created":"2015-08-27 18:26","charge_time":"2015-09-21 20:01","admin_id":"15281","kind":"1","charged":"1","charge_num":"20.00","charge_price":"0.00","jb_type":"1","specify_expert_id":"0","note":"  ","report":"请上传陶器藏品，................................................。","charge_edit_type":"1","charge_edit_time":"1442836886","is_public":"0","reply_state":"已鉴定","admin_state":"已取消","type_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/images/type_1.png","type":"极速鉴定","kind_name":"陶瓷","IosType":3,"charge_refund":0,"specify_expert_from":0,"iscomment":0,"timeout":259164,"refund":[{"name":"撤销0.60元","time":"2016-01-13 16:02:46"}]}]
     * message : 5
     */

    private boolean error;
    private String message;
    /**
     * id : 498705
     * name :
     * photo : http://c2c.ig365.cn/data/files/old_shop/gh_722/store_883722/jianbao/20150827/201508271826516102.jpg@400w_400h
     * state : 1
     * created : 2015-08-27 18:26
     * charge_time : 2015-09-21 20:01
     * admin_id : 15281
     * kind : 1
     * charged : 1
     * charge_num : 20.00
     * charge_price : 0.00
     * jb_type : 1
     * specify_expert_id : 0
     * note :
     * report : 请上传陶器藏品，................................................。
     * charge_edit_type : 1
     * charge_edit_time : 1442836886
     * is_public : 0
     * reply_state : 已鉴定
     * admin_state : 已取消
     * type_img : http://jianbao.artxun.com/external/modules/jbapp/templates/images/type_1.png
     * type : 极速鉴定
     * kind_name : 陶瓷
     * IosType : 3
     * charge_refund : 0
     * specify_expert_from : 0
     * iscomment : 0
     * timeout : 259164
     * refund : [{"name":"撤销0.60元","time":"2016-01-13 16:02:46"}]
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
        private String name;
        private String photo;
        private String state;
        private String created;
        private String charge_time;
        private String admin_id;
        private String kind;
        private String charged;
        private String charge_num;
        private String charge_price;
        private String jb_type;
        private String specify_expert_id;
        private String note;
        private String report;
        private String charge_edit_type;
        private String charge_edit_time;
        private String is_public;
        private String reply_state;
        private String admin_state;
        private String type_img;
        private String type;
        private String kind_name;
        private int IosType;
        private int charge_refund;
        private int specify_expert_from;
        private int iscomment;
        private int timeout;
        /**
         * name : 撤销0.60元
         * time : 2016-01-13 16:02:46
         */

        private List<RefundEntity> refund;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public void setCharge_time(String charge_time) {
            this.charge_time = charge_time;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setCharged(String charged) {
            this.charged = charged;
        }

        public void setCharge_num(String charge_num) {
            this.charge_num = charge_num;
        }

        public void setCharge_price(String charge_price) {
            this.charge_price = charge_price;
        }

        public void setJb_type(String jb_type) {
            this.jb_type = jb_type;
        }

        public void setSpecify_expert_id(String specify_expert_id) {
            this.specify_expert_id = specify_expert_id;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public void setCharge_edit_type(String charge_edit_type) {
            this.charge_edit_type = charge_edit_type;
        }

        public void setCharge_edit_time(String charge_edit_time) {
            this.charge_edit_time = charge_edit_time;
        }

        public void setIs_public(String is_public) {
            this.is_public = is_public;
        }

        public void setReply_state(String reply_state) {
            this.reply_state = reply_state;
        }

        public void setAdmin_state(String admin_state) {
            this.admin_state = admin_state;
        }

        public void setType_img(String type_img) {
            this.type_img = type_img;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setKind_name(String kind_name) {
            this.kind_name = kind_name;
        }

        public void setIosType(int IosType) {
            this.IosType = IosType;
        }

        public void setCharge_refund(int charge_refund) {
            this.charge_refund = charge_refund;
        }

        public void setSpecify_expert_from(int specify_expert_from) {
            this.specify_expert_from = specify_expert_from;
        }

        public void setIscomment(int iscomment) {
            this.iscomment = iscomment;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public void setRefund(List<RefundEntity> refund) {
            this.refund = refund;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getState() {
            return state;
        }

        public String getCreated() {
            return created;
        }

        public String getCharge_time() {
            return charge_time;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public String getKind() {
            return kind;
        }

        public String getCharged() {
            return charged;
        }

        public String getCharge_num() {
            return charge_num;
        }

        public String getCharge_price() {
            return charge_price;
        }

        public String getJb_type() {
            return jb_type;
        }

        public String getSpecify_expert_id() {
            return specify_expert_id;
        }

        public String getNote() {
            return note;
        }

        public String getReport() {
            return report;
        }

        public String getCharge_edit_type() {
            return charge_edit_type;
        }

        public String getCharge_edit_time() {
            return charge_edit_time;
        }

        public String getIs_public() {
            return is_public;
        }

        public String getReply_state() {
            return reply_state;
        }

        public String getAdmin_state() {
            return admin_state;
        }

        public String getType_img() {
            return type_img;
        }

        public String getType() {
            return type;
        }

        public String getKind_name() {
            return kind_name;
        }

        public int getIosType() {
            return IosType;
        }

        public int getCharge_refund() {
            return charge_refund;
        }

        public int getSpecify_expert_from() {
            return specify_expert_from;
        }

        public int getIscomment() {
            return iscomment;
        }

        public int getTimeout() {
            return timeout;
        }

        public List<RefundEntity> getRefund() {
            return refund;
        }

        public static class RefundEntity {
            private String name;
            private String time;

            public void setName(String name) {
                this.name = name;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getName() {
                return name;
            }

            public String getTime() {
                return time;
            }
        }
    }
}
