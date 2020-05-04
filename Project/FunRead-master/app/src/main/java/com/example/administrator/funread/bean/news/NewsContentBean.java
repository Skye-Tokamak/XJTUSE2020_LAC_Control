package com.example.administrator.funread.bean.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：created by weidiezeng on 2019/8/17 10:19
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class NewsContentBean implements Parcelable {

    private String url;
    private String title;
    private String authorName;
    private String img;
    private String img02;
    private String img03;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }



    public String getImg02() {
        return img02;
    }

    public void setImg02(String img02) {
        this.img02 = img02;
    }

    public String getImg03() {
        return img03;
    }

    public void setImg03(String img03) {
        this.img03 = img03;
    }
    public NewsContentBean(){

    }
    protected NewsContentBean(Parcel in) {
        url=in.readString();
        title=in.readString();
        authorName=in.readString();
        img=in.readString();
        img02=in.readString();
        img03=in.readString();
    }
    public static final Creator<NewsContentBean> CREATOR = new Creator<NewsContentBean>() {
        @Override
        public NewsContentBean createFromParcel(Parcel in) {
            return new NewsContentBean(in);
        }

        @Override
        public NewsContentBean[] newArray(int size) {
            return new NewsContentBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(authorName);
        dest.writeString(img);
        dest.writeString(img02);
        dest.writeString(img03);
    }
}
