package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by star on 16/3/30.
 */
public class IdentifyMeetWXPayStatusResponse {

    /**
     * error : false
     * data : {"charged":"1","isPay":"0","timeout":"0","info":[{"id":"1","paihao":"","name":"陶瓷"},{"id":"7","paihao":"51098","name":"杂项"}]}
     * message :
     */

    private boolean error;
    /**
     * charged : 1
     * isPay : 0
     * timeout : 0
     * info : [{"id":"1","paihao":"","name":"陶瓷"},{"id":"7","paihao":"51098","name":"杂项"}]
     */

    private DataEntity data;
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataEntity {
        private String charged;
        private String isPay;
        private String timeout;
        /**
         * id : 1
         * paihao :
         * name : 陶瓷
         */

        private List<InfoEntity> info;

        public String getCharged() {
            return charged;
        }

        public void setCharged(String charged) {
            this.charged = charged;
        }

        public String getIsPay() {
            return isPay;
        }

        public void setIsPay(String isPay) {
            this.isPay = isPay;
        }

        public String getTimeout() {
            return timeout;
        }

        public void setTimeout(String timeout) {
            this.timeout = timeout;
        }

        public List<InfoEntity> getInfo() {
            return info;
        }

        public void setInfo(List<InfoEntity> info) {
            this.info = info;
        }

        public static class InfoEntity {
            private String id;
            private String paihao;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPaihao() {
                return paihao;
            }

            public void setPaihao(String paihao) {
                this.paihao = paihao;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
