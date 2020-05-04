package com.example.administrator.funread.bean.news;

/**
 * 作者：created by weidiezeng on 2019/8/8 11:27
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class NewsChannelBean implements Comparable<NewsChannelBean> {
    private String channelId;
    private String channelName;
    private int isEnable;
    private int position;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(obj==null||getClass()!= obj.getClass())
            return  false;
        NewsChannelBean bean=(NewsChannelBean)obj;
        if(isEnable!=bean.isEnable)
            return false;
        if(position!=bean.position)
            return false;
        if(channelId != null ? !channelId.equals(bean.channelId) : bean.channelId != null)
            return false;
        return  channelName != null ? channelName.equals(bean.channelName) : bean.channelName == null;

    }

    @Override
    public int hashCode() {
        int result = channelId != null ? channelId.hashCode() : 0;
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + isEnable;
        result = 31 * result + position;
        return result;
    }

    @Override
    public int compareTo(NewsChannelBean o) {
        return this.position-o.getPosition();
    }
}
