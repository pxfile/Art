package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by star on 15/6/4.
 */
public class HomeBannerResponse {
    /**
     * data : {"slider":[{"name":"","id":"491267","type":"order","url":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/e35cef13b6edc7da90a8245429d8dfca/BnancuNDY4MjYxMzE0MTYwMDE1MD8yMSYwMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"0","type":"url","url":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/7ba93973088ada98c1f1de0eb76cd87d/BnankuMDU5NTUxMzE1MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"1147","type":"info","url":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/2a0ad6ad5c6428fb584c9ff860d8c318/BnanIuMzYwNzI1MjE2MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"1200","type":"info","url":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/1c64c1f45fedce8c9f7286677b94d659/BnanYuMDEwNzQ1MDE2MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"1180","type":"info","url":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/fcaa64e38fc5e21c8c4304175acd9beb/==ZwpwLmYyODE5MzI1MTQxMjA0NTAxMjQvMjA0NTAxMj8vYW5iYWppL2Q0Mz8xZV9ydG9zNCM0XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"}],"ad":[{"name":"","id":"1174","type":"info","url":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/ab83314f9059e380cdc94f0e2e6b37c4/==ZwpwLmk5NTQyNTM5ODQxMDA1NTAxMjQvMDA1NTAxMj8vYW5iYWppL2Q0Mz8xZV9ydG9zNCM0XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"490476","type":"order","url":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/e1dcfc3659c977afb90e9d8e82f1f237/BnanEuNDEyNTUzMTE0MTYwMDE1MD8yMSYwMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"175","type":"expert","url":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/8dd19e058aa56cf7a134c69aec871daa/BnanguMjM2NjczMzEyMjUyMDE1MD8yMiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"}]}
     * error : false
     * message :
     */
    private DataEntity data;
    private boolean error;
    private String message;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
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
         * slider : [{"name":"","id":"491267","type":"order","url":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/e35cef13b6edc7da90a8245429d8dfca/BnancuNDY4MjYxMzE0MTYwMDE1MD8yMSYwMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"0","type":"url","url":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/7ba93973088ada98c1f1de0eb76cd87d/BnankuMDU5NTUxMzE1MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"1147","type":"info","url":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/2a0ad6ad5c6428fb584c9ff860d8c318/BnanIuMzYwNzI1MjE2MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"1200","type":"info","url":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/1c64c1f45fedce8c9f7286677b94d659/BnanYuMDEwNzQ1MDE2MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"1180","type":"info","url":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/fcaa64e38fc5e21c8c4304175acd9beb/==ZwpwLmYyODE5MzI1MTQxMjA0NTAxMjQvMjA0NTAxMj8vYW5iYWppL2Q0Mz8xZV9ydG9zNCM0XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"}]
         * ad : [{"name":"","id":"1174","type":"info","url":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/ab83314f9059e380cdc94f0e2e6b37c4/==ZwpwLmk5NTQyNTM5ODQxMDA1NTAxMjQvMDA1NTAxMj8vYW5iYWppL2Q0Mz8xZV9ydG9zNCM0XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"490476","type":"order","url":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/e1dcfc3659c977afb90e9d8e82f1f237/BnanEuNDEyNTUzMTE0MTYwMDE1MD8yMSYwMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"},{"name":"","id":"175","type":"expert","url":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/8dd19e058aa56cf7a134c69aec871daa/BnanguMjM2NjczMzEyMjUyMDE1MD8yMiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg"}]
         */
        private List<SliderEntity> slider;
        private List<AdEntity> ad;

        public void setSlider(List<SliderEntity> slider) {
            this.slider = slider;
        }

        public void setAd(List<AdEntity> ad) {
            this.ad = ad;
        }

        public List<SliderEntity> getSlider() {
            return slider;
        }

        public List<AdEntity> getAd() {
            return ad;
        }

        public class SliderEntity {
            /**
             * name :
             * id : 491267
             * type : order
             * url : http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/e35cef13b6edc7da90a8245429d8dfca/BnancuNDY4MjYxMzE0MTYwMDE1MD8yMSYwMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg
             */
            private String name;
            private String id;
            private String type;
            private String url;

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }

            public String getType() {
                return type;
            }

            public String getUrl() {
                return url;
            }
        }

        public class AdEntity {
            /**
             * name :
             * id : 1174
             * type : info
             * url : http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/ab83314f9059e380cdc94f0e2e6b37c4/==ZwpwLmk5NTQyNTM5ODQxMDA1NTAxMjQvMDA1NTAxMj8vYW5iYWppL2Q0Mz8xZV9ydG9zNCM0XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg
             */
            private String name;
            private String id;
            private String type;
            private String url;

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }

            public String getType() {
                return type;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
