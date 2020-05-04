package com.example.administrator.funread.bean.news;

import java.util.List;

/**
 * 作者：created by weidiezeng on 2019/8/11 16:53
 * 邮箱：1067875902@qq.com
 * 描述：文章实体类
 */
public class NewsArticleBean {
    /*{
	"reason":"成功的返回",
	"result":{
		"stat":"1",
		"data":[
			{
				"uniquekey":"931a30fec842686f8acb3c19f4f09cba",
				"title":"陌陌主播钰儿宝包长文记录25天回忆，《燃烧吧！少女》比赛收获颇丰",
				"date":"2019-08-14 14:36",
				"category":"娱乐",
				"author_name":"华声娱乐频道",
				"url":"http:\/\/mini.eastday.com\/mobile\/190814143645466.html",
				"thumbnail_pic_s":"http:\/\/07imgmini.eastday.com\/mobile\/20190814\/20190814143645_d602aea51a8d9b2f37987bdfc89e5efe_3_mwpm_03200403.jpg",
				"thumbnail_pic_s02":"http:\/\/07imgmini.eastday.com\/mobile\/20190814\/20190814143645_d602aea51a8d9b2f37987bdfc89e5efe_2_mwpm_03200403.jpg",
				"thumbnail_pic_s03":"http:\/\/07imgmini.eastday.com\/mobile\/20190814\/20190814143645_d602aea51a8d9b2f37987bdfc89e5efe_4_mwpm_03200403.jpg"
			},
			{
				"uniquekey":"90d69f85ff216fcb6da064203a23789f",
				"title":"四位“狐狸精”，图2我忍了，图3我再忍，图4我忍无可忍！",
				"date":"2019-08-14 14:33",
				"category":"娱乐",
				"author_name":"邂逅朦胧",
				"url":"http:\/\/mini.eastday.com\/mobile\/190814143348342.html",
				"thumbnail_pic_s":"http:\/\/03imgmini.eastday.com\/mobile\/20190814\/2019081414_763c9bdd85e34838a0069fb11c4bacbb_7901_mwpm_03200403.jpg",
				"thumbnail_pic_s02":"http:\/\/03imgmini.eastday.com\/mobile\/20190814\/2019081414_3386b726a4724559bae139f0cd931ef8_1972_mwpm_03200403.jpg",
				"thumbnail_pic_s03":"http:\/\/03imgmini.eastday.com\/mobile\/20190814\/2019081414_eeb091b0e59041209448d1ccae143cbc_2758_mwpm_03200403.jpg"
			},
					]
	    },
	    "error_code":0
    }
    *
    * */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean{
        private String stat;
        private List<Databean>data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<Databean> getData() {
            return data;
        }

        public void setData(List<Databean> data) {
            this.data = data;
        }
        public static class Databean{
            private String uniquekey;
            private String title;
            private String date;
            private String category;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }
        }
    }

}
