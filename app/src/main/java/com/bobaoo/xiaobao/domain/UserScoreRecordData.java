package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/15.
 */
public class UserScoreRecordData {

    /**
     * data : {"get_list":[{"obtain":"评论","obt":"5","uid":"796991","note":"评论：1203获得1个积分","useid":"1203","addtime":"2015-06-12","num":"1","id":"36173","state":"0","type":"3","uuid":"","is_delete":"0"}],"use_list":[{"obtain":"0","uid":"796991","note":"","jb_type":"普通鉴定","useid":"491911","addtime":"2015-06-12","num":"1","id":"36129","state":"1","type":"0","uuid":"","is_delete":"0"}]}
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
         * get_list : [{"obtain":"评论","obt":"5","uid":"796991","note":"评论：1203获得1个积分","useid":"1203","addtime":"2015-06-12","num":"1","id":"36173","state":"0","type":"3","uuid":"","is_delete":"0"}]
         * use_list : [{"obtain":"0","uid":"796991","note":"","jb_type":"普通鉴定","useid":"491911","addtime":"2015-06-12","num":"1","id":"36129","state":"1","type":"0","uuid":"","is_delete":"0"}]
         */
        private List<Get_listEntity> get_list;
        private List<Use_listEntity> use_list;

        public void setGet_list(List<Get_listEntity> get_list) {
            this.get_list = get_list;
        }

        public void setUse_list(List<Use_listEntity> use_list) {
            this.use_list = use_list;
        }

        public List<Get_listEntity> getGet_list() {
            return get_list;
        }

        public List<Use_listEntity> getUse_list() {
            return use_list;
        }

        public class Get_listEntity {
            /**
             * obtain : 评论
             * obt : 5
             * uid : 796991
             * note : 评论：1203获得1个积分
             * useid : 1203
             * addtime : 2015-06-12
             * num : 1
             * id : 36173
             * state : 0
             * type : 3
             * uuid :
             * is_delete : 0
             */
            private String obtain;
            private String obt;
            private String uid;
            private String note;
            private String useid;
            private String addtime;
            private String num;
            private String id;
            private String state;
            private String type;
            private String uuid;
            private String is_delete;

            public void setObtain(String obtain) {
                this.obtain = obtain;
            }

            public void setObt(String obt) {
                this.obt = obt;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public void setUseid(String useid) {
                this.useid = useid;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setState(String state) {
                this.state = state;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public void setIs_delete(String is_delete) {
                this.is_delete = is_delete;
            }

            public String getObtain() {
                return obtain;
            }

            public String getObt() {
                return obt;
            }

            public String getUid() {
                return uid;
            }

            public String getNote() {
                return note;
            }

            public String getUseid() {
                return useid;
            }

            public String getAddtime() {
                return addtime;
            }

            public String getNum() {
                return num;
            }

            public String getId() {
                return id;
            }

            public String getState() {
                return state;
            }

            public String getType() {
                return type;
            }

            public String getUuid() {
                return uuid;
            }

            public String getIs_delete() {
                return is_delete;
            }
        }

        public class Use_listEntity {
            /**
             * obtain : 0
             * uid : 796991
             * note :
             * jb_type : 普通鉴定
             * useid : 491911
             * addtime : 2015-06-12
             * num : 1
             * id : 36129
             * state : 1
             * type : 0
             * uuid :
             * is_delete : 0
             */
            private String obtain;
            private String uid;
            private String note;
            private String jb_type;
            private String useid;
            private String addtime;
            private String num;
            private String id;
            private String state;
            private String type;
            private String uuid;
            private String is_delete;

            public void setObtain(String obtain) {
                this.obtain = obtain;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public void setJb_type(String jb_type) {
                this.jb_type = jb_type;
            }

            public void setUseid(String useid) {
                this.useid = useid;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setState(String state) {
                this.state = state;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public void setIs_delete(String is_delete) {
                this.is_delete = is_delete;
            }

            public String getObtain() {
                return obtain;
            }

            public String getUid() {
                return uid;
            }

            public String getNote() {
                return note;
            }

            public String getJb_type() {
                return jb_type;
            }

            public String getUseid() {
                return useid;
            }

            public String getAddtime() {
                return addtime;
            }

            public String getNum() {
                return num;
            }

            public String getId() {
                return id;
            }

            public String getState() {
                return state;
            }

            public String getType() {
                return type;
            }

            public String getUuid() {
                return uuid;
            }

            public String getIs_delete() {
                return is_delete;
            }
        }
    }
}
