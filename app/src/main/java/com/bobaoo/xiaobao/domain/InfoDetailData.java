package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by chenming on 2015/6/12.
 */
public class InfoDetailData {
    /**
     * data : {"commCount":0,"collection":"","zx_type":"3","related":[{"zx_img":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/4b675202440e6f28c464bff8ba79812a/BnanMuNjk5MTcyNDE1MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","name":"邮币藏市现暴涨暴跌行情","context":"自去年以来，钱币市场几乎一直处于歇夏状态。近期，低迷...","comment":"2","id":"1203"},{"zx_img":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/a93da795051bf44bdb8fb7f453f90a1b/BnanguNDQ5MjAwNDEwNjUyMDE1MD8yNiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","name":"探秘手串产业链","context":"昔日的地摊货，如今却成了文玩市场的新宠。星月菩提价格...","comment":"0","id":"1201"},{"zx_img":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/71980c9b3fa21db99541c2ff1350b094/BnanEuNzU4MDk0NTE0MjUyMDE1MD8yMiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","name":"后田黄时代如何投资收藏","context":"石帝田黄，天生就有一股皇家富贵之气。前两年，田黄石行...","comment":"1","id":"1196"}],"zx_img":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/97c5db6b0564a4ff7eee1b5964515489/BnanYuNzczMzEzMjE2NjUyMDE1MD8yNiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","addtime":"2015-05-26","name":"古玩市场地摊淘货必备秘籍","context":"hsy","width":"400","share":"在介绍秘籍之前我想先替旧货地摊说两句。  有不少有钱的朋友看不起旧货地摊，认为去那淘货跌份，咱有经济实力，咱去精品店，咱层次多高，谁去那啊！我觉得有这种想法不但是不知情趣，同样也是没有素质。当代收藏大...","comment":[{"iszan":"","lou":"1楼","user_id":"878200","addtime":"2015-06-11","user_name":"秋风","zan":"0","head_img":"http://img401.artxun.com/pictures/3567ebb1d9e607de7ec4548c58c38803/77edc51742faf19f6742be8145c5252e/A=MD8xRiNEOEMzOUVFMERDRUkyNTQxQjcxMDA0Mzg4RTZCNj9EOCQzMzIzNDEwMTAvcHFhcX4vY28uZ2xvcWEuL3ovcDR0aH/thumbnail_92_92.jpg?new_detail_2015-06-15","context":"骗子公司","nikename":"秋风","id":"7354"}],"id":"1202","height":"400"}
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
         * commCount : 0
         * collection :
         * zx_type : 3
         * related : [{"zx_img":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/4b675202440e6f28c464bff8ba79812a/BnanMuNjk5MTcyNDE1MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","name":"邮币藏市现暴涨暴跌行情","context":"自去年以来，钱币市场几乎一直处于歇夏状态。近期，低迷...","comment":"2","id":"1203"},{"zx_img":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/a93da795051bf44bdb8fb7f453f90a1b/BnanguNDQ5MjAwNDEwNjUyMDE1MD8yNiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","name":"探秘手串产业链","context":"昔日的地摊货，如今却成了文玩市场的新宠。星月菩提价格...","comment":"0","id":"1201"},{"zx_img":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/71980c9b3fa21db99541c2ff1350b094/BnanEuNzU4MDk0NTE0MjUyMDE1MD8yMiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","name":"后田黄时代如何投资收藏","context":"石帝田黄，天生就有一股皇家富贵之气。前两年，田黄石行...","comment":"1","id":"1196"}]
         * zx_img : http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/97c5db6b0564a4ff7eee1b5964515489/BnanYuNzczMzEzMjE2NjUyMDE1MD8yNiUyMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg
         * addtime : 2015-05-26
         * name : 古玩市场地摊淘货必备秘籍
         * context : hsy
         * width : 400
         * share : 在介绍秘籍之前我想先替旧货地摊说两句。  有不少有钱的朋友看不起旧货地摊，认为去那淘货跌份，咱有经济实力，咱去精品店，咱层次多高，谁去那啊！我觉得有这种想法不但是不知情趣，同样也是没有素质。当代收藏大...
         * comment : [{"iszan":"","lou":"1楼","user_id":"878200","addtime":"2015-06-11","user_name":"秋风","zan":"0","head_img":"http://img401.artxun.com/pictures/3567ebb1d9e607de7ec4548c58c38803/77edc51742faf19f6742be8145c5252e/A=MD8xRiNEOEMzOUVFMERDRUkyNTQxQjcxMDA0Mzg4RTZCNj9EOCQzMzIzNDEwMTAvcHFhcX4vY28uZ2xvcWEuL3ovcDR0aH/thumbnail_92_92.jpg?new_detail_2015-06-15","context":"骗子公司","nikename":"秋风","id":"7354"}]
         * id : 1202
         * height : 400
         */
        private int commCount;
        private String collection;
        private String zx_type;
        private List<RelatedEntity> related;
        private String zx_img;
        private String addtime;
        private String name;
        private String context;
        private String width;
        private String share;
        private List<CommentEntity> comment;
        private String id;
        private String height;

        public void setCommCount(int commCount) {
            this.commCount = commCount;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public void setZx_type(String zx_type) {
            this.zx_type = zx_type;
        }

        public void setRelated(List<RelatedEntity> related) {
            this.related = related;
        }

        public void setZx_img(String zx_img) {
            this.zx_img = zx_img;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public void setComment(List<CommentEntity> comment) {
            this.comment = comment;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public int getCommCount() {
            return commCount;
        }

        public String getCollection() {
            return collection;
        }

        public String getZx_type() {
            return zx_type;
        }

        public List<RelatedEntity> getRelated() {
            return related;
        }

        public String getZx_img() {
            return zx_img;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getName() {
            return name;
        }

        public String getContext() {
            return context;
        }

        public String getWidth() {
            return width;
        }

        public String getShare() {
            return share;
        }

        public List<CommentEntity> getComment() {
            return comment;
        }

        public String getId() {
            return id;
        }

        public String getHeight() {
            return height;
        }

        public class RelatedEntity {
            /**
             * zx_img : http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/4b675202440e6f28c464bff8ba79812a/BnanMuNjk5MTcyNDE1MTYxMDE1MD8yMSYxMDE1MD8ybyJhbmlhamkvNzA3Nz80ZV9ydG9zOSc3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg
             * name : 邮币藏市现暴涨暴跌行情
             * context : 自去年以来，钱币市场几乎一直处于歇夏状态。近期，低迷...
             * comment : 2
             * id : 1203
             */
            private String zx_img;
            private String name;
            private String context;
            private String comment;
            private String id;

            public void setZx_img(String zx_img) {
                this.zx_img = zx_img;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getZx_img() {
                return zx_img;
            }

            public String getName() {
                return name;
            }

            public String getContext() {
                return context;
            }

            public String getComment() {
                return comment;
            }

            public String getId() {
                return id;
            }
        }

        public class CommentEntity {
            /**
             * iszan :
             * lou : 1楼
             * user_id : 878200
             * addtime : 2015-06-11
             * user_name : 秋风
             * zan : 0
             * head_img : http://img401.artxun.com/pictures/3567ebb1d9e607de7ec4548c58c38803/77edc51742faf19f6742be8145c5252e/A=MD8xRiNEOEMzOUVFMERDRUkyNTQxQjcxMDA0Mzg4RTZCNj9EOCQzMzIzNDEwMTAvcHFhcX4vY28uZ2xvcWEuL3ovcDR0aH/thumbnail_92_92.jpg?new_detail_2015-06-15
             * context : 骗子公司
             * nikename : 秋风
             * id : 7354
             */
            private String iszan;
            private String lou;
            private String user_id;
            private String addtime;
            private String user_name;
            private String zan;
            private String head_img;
            private String context;
            private String nikename;
            private String id;

            public void setIszan(String iszan) {
                this.iszan = iszan;
            }

            public void setLou(String lou) {
                this.lou = lou;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public void setZan(String zan) {
                this.zan = zan;
            }

            public void setHead_img(String head_img) {
                this.head_img = head_img;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public void setNikename(String nikename) {
                this.nikename = nikename;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIszan() {
                return iszan;
            }

            public String getLou() {
                return lou;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getAddtime() {
                return addtime;
            }

            public String getUser_name() {
                return user_name;
            }

            public String getZan() {
                return zan;
            }

            public String getHead_img() {
                return head_img;
            }

            public String getContext() {
                return context;
            }

            public String getNikename() {
                return nikename;
            }

            public String getId() {
                return id;
            }
        }
    }
}
