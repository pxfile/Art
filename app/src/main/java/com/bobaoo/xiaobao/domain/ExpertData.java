package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by star on 15/6/8.
 */
public class ExpertData {
    /**
     * data : [{"star_level":"特聘","org":"7,2","kind":"杂项","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_41.jpg","name":"杨宝杰","tui":"1","id":"41","isfans":"","honors":"首都博物馆文物鉴定委员会委员"},{"star_level":"特聘","org":"1","kind":"陶瓷","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_7.jpg","name":"陈润民","tui":"1","id":"7","isfans":"","honors":"中国古陶瓷学会理事"},{"star_level":"特聘","org":"1","kind":"书画","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_44.jpg","name":"孔晨","tui":"1","id":"44","isfans":"","honors":"故宫博物院古书画部副研究员"},{"star_level":"特聘","org":"7,1","kind":"玉器","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_14.jpg","name":"张广文","tui":"1","id":"14","isfans":"","honors":"古器物部工艺组组长"},{"star_level":"特聘","org":"1","kind":"杂项","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_48.jpg","name":"宋海洋","tui":"1","id":"48","isfans":"","honors":"故宫博物院副研究员、故宫陈列..."},{"star_level":"特聘","org":"4","kind":"陶瓷","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_36.jpg","name":"张如兰","tui":"1","id":"36","isfans":"","honors":"北京市文物局鉴定组组长、副研..."},{"star_level":"特聘","org":"6","kind":"杂项","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_39.jpg","name":"戴志强","tui":"1","id":"39","isfans":"","honors":"中国钱币博物馆馆长"},{"star_level":"特聘","org":"4","kind":"书画","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_43.jpg","name":"李晨","tui":"1","id":"43","isfans":"","honors":"文物出境鉴定所 副所长、副研..."},{"star_level":"特聘","org":"1,8","kind":"陶瓷","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_2.jpg","name":"叶佩兰","tui":"1","id":"2","isfans":"","honors":"故宫博物院研究馆员"},{"star_level":"特聘","org":"1,8","kind":"杂项","head_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_57.jpg","name":"张荣","tui":"0","id":"57","isfans":"","honors":"北京故宫博物院，任古器物部副..."}]
     * error : false
     * message : 818
     */
    private List<DataEntity> data;
    private boolean error;
    private String message;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public class DataEntity {
        /**
         * star:float 3.5f
         * star_level : 特聘
         * org : 7,2
         * kind : 杂项
         * head_img : http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_41.jpg
         * name : 杨宝杰
         * tui : 1
         * id : 41
         * isfans :
         * star_level_num:6
         * honors : 首都博物馆文物鉴定委员会委员
         *
         * "jbapp_pt": "0",
         * "jbapp_js": "0",
         * "jbapp_sp": "1",
         * "jbapp_yy": "1",
         * "pt_price": "5",
         * "js_price": "20",
         * "sp_price": "1000",
         * "yy_price": "1000",
         * "gzcount": "8805",
         * "jbcount": "990",
         */
        private String star_level;
        private String org;
        private String kind;
        private String head_img;
        private String name;
        private String tui;
        private String id;
        private String isfans;
        private String honors;
        private int star_level_num;

        private String jbapp_pt;
        private String jbapp_js;
        private String jbapp_sp;
        private String jbapp_yy;
        private String pt_price;
        private String js_price;
        private String sp_price;
        private String yy_price;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getJbapp_pt() {
            return jbapp_pt;
        }

        public void setJbapp_pt(String jbapp_pt) {
            this.jbapp_pt = jbapp_pt;
        }

        public String getJbapp_js() {
            return jbapp_js;
        }

        public void setJbapp_js(String jbapp_js) {
            this.jbapp_js = jbapp_js;
        }

        public String getJbapp_sp() {
            return jbapp_sp;
        }

        public void setJbapp_sp(String jbapp_sp) {
            this.jbapp_sp = jbapp_sp;
        }

        public String getJbapp_yy() {
            return jbapp_yy;
        }

        public void setJbapp_yy(String jbapp_yy) {
            this.jbapp_yy = jbapp_yy;
        }

        public String getPt_price() {
            return pt_price;
        }

        public void setPt_price(String pt_price) {
            this.pt_price = pt_price;
        }

        public String getJs_price() {
            return js_price;
        }

        public void setJs_price(String js_price) {
            this.js_price = js_price;
        }

        public String getSp_price() {
            return sp_price;
        }

        public void setSp_price(String sp_price) {
            this.sp_price = sp_price;
        }

        public String getYy_price() {
            return yy_price;
        }

        public void setYy_price(String yy_price) {
            this.yy_price = yy_price;
        }





        public int getStar_level_num() {
            return star_level_num;
        }

        public void setStar_level_num(int star_level_num) {
            this.star_level_num = star_level_num;
        }

        public void setStar_level(String star_level) {
            this.star_level = star_level;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTui(String tui) {
            this.tui = tui;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIsfans(String isfans) {
            this.isfans = isfans;
        }

        public void setHonors(String honors) {
            this.honors = honors;
        }

        public String getStar_level() {
            return star_level;
        }

        public String getOrg() {
            return org;
        }

        public String getKind() {
            return kind;
        }

        public String getHead_img() {
            return head_img;
        }

        public String getName() {
            return name;
        }

        public String getTui() {
            return tui;
        }

        public String getId() {
            return id;
        }

        public String getIsfans() {
            return isfans;
        }

        public String getHonors() {
            return honors;
        }
    }
}
