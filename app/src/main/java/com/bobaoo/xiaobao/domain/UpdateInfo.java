package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/7/27.
 */
public class UpdateInfo {

    /**
     * data : {"apkurl":"http://artist.app.artxun.com/download/xiaobao.apk","note":"1.全新的图片上传流程，飞速上传一击实现。rn2.全新的UI展示，界面更佳美观。","appname":"xiaobao","verName":"1.4.2","verCode":28}
     * message :
     * error : false
     */
    private DataEntity data;
    private String message;
    private boolean error;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }

    public class DataEntity {
        /**
         * apkurl : http://artist.app.artxun.com/download/xiaobao.apk
         * note : 1.全新的图片上传流程，飞速上传一击实现。rn2.全新的UI展示，界面更佳美观。
         * appname : xiaobao
         * verName : 1.4.2
         * verCode : 28
         */
        private String apkurl;
        private String note;
        private String appname;
        private String verName;
        private int verCode;

        public void setApkurl(String apkurl) {
            this.apkurl = apkurl;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public void setVerName(String verName) {
            this.verName = verName;
        }

        public void setVerCode(int verCode) {
            this.verCode = verCode;
        }

        public String getApkurl() {
            return apkurl;
        }

        public String getNote() {
            return note;
        }

        public String getAppname() {
            return appname;
        }

        public String getVerName() {
            return verName;
        }

        public int getVerCode() {
            return verCode;
        }
    }
}
