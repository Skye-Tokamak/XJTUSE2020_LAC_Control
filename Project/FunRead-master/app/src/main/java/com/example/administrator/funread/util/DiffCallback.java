package com.example.administrator.funread.util;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 作者：created by weidiezeng on 2019/8/16 08:51
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class DiffCallback extends DiffUtil.Callback {
    private final Items mOldItems,mNewItems;
    private DiffCallback(Items oldItems,Items newItems){
        this.mOldItems=oldItems;
        this.mNewItems=newItems;
    }

    /**
     * 在将newDatas 设置给Adapter之前，先调用DiffUtil.calculateDiff()方法，
     *   计算出新老数据集转化的最小更新集，就是DiffUtil.DiffResult对象。
     *   DiffUtil.calculateDiff()方法定义如下：
     *   第一个参数是DiffUtil.Callback对象，
     *   第二个参数代表是否检测Item的移动，改为false算法效率更高，按需设置，我们这里是true。
     * @param oldList
     * @param newList
     * @param adapter
     */
    public static void create(@NonNull Items oldList, @NonNull Items newList, @NonNull MultiTypeAdapter adapter){

        DiffCallback diffCallback=new DiffCallback(oldList,newList);
        DiffUtil.DiffResult result=DiffUtil.calculateDiff(diffCallback,true);
        result.dispatchUpdatesTo(adapter);
    }

    /**
     * @return
     */
    @Override
    public int getOldListSize() {
        return mOldItems!=null ? mOldItems.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewItems != null ? mNewItems.size() : 0;
    }

    /*
     *被DiffUtil调用，用来判断 两个对象是否是相同的Item。
     * 例如，如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
     * 本例判断name字段是否一致，不一定全部是这样！！！！
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return mOldItems.get(i).equals(mNewItems.get(i1));
    }

    /**
     * 被DiffUtil调用，用来检查 两个item是否含有相同的数据
     * 这个方法仅仅在areItemsTheSame()返回true时，才调用。
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return mOldItems.get(i).hashCode()==mNewItems.get(i1).hashCode();
    }
}
