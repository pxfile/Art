package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by star on 16/3/29.
 */
public class IdentifyMeetPayResponse {

    /**
     * error : false
     * data : [{"id":"1","paihao":"","name":"陶瓷"},{"id":"7","paihao":"51075","name":"杂项"}]
     * message :
     */

    private boolean error;
    private String message;
    /**
     * id : 1
     * paihao :
     * name : 陶瓷
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
        private String paihao;
        private String name;
        private String time;
        private String isup;
        private String count;

        public void setIsup(String isup) {
            this.isup = isup;
        }
        public String getIsup() {
            return isup;
        }
        public void setCount(String count) {
            this.count = count;
        }
        public String getCount() {
            return count;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public String getTime() {
            return time;
        }
        public void setId(String time) {
            this.time = time;
        }
        public void setPaihao(String paihao) {
            this.paihao = paihao;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getPaihao() {
            return paihao;
        }

        public String getName() {
            return name;
        }
    }
}
