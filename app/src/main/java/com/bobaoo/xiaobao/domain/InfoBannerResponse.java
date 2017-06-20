package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 16/1/12.
 */
public class InfoBannerResponse {

    /**
     * error : false
     * data : {"type":"expert","id":"512757","url":"http://c2c.ig365.cn/data/files/old_shop/gh_344/store_1344/jianbao/20160111/201601111207441643.jpg","name":""}
     * message :
     */

    private boolean error;
    /**
     * type : expert
     * id : 512757
     * url : http://c2c.ig365.cn/data/files/old_shop/gh_344/store_1344/jianbao/20160111/201601111207441643.jpg
     * name :
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
        private String type;
        private String id;
        private String url;
        private String name;

        public void setType(String type) {
            this.type = type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }
    }
}
