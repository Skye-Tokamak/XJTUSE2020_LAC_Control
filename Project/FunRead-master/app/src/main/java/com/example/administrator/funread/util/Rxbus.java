package com.example.administrator.funread.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import android.support.annotation.NonNull;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 作者：created by weidiezeng on 2019/8/7 17:10
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class Rxbus {
    private ConcurrentHashMap<Object,List<Subject>>subjectMapper=new ConcurrentHashMap<>();
    private Rxbus(){

    }
    public static Rxbus getInstance(){
        return Holder.instance;
    }
    @NonNull
    public <T>Observable<T>register(@NonNull Class<T>clz){
        return register(clz.getName());
    }
    @NonNull
    public<T>Observable<T>register(@NonNull Object tag){
        List<io.reactivex.subjects.Subject>subjectList=subjectMapper.get(tag);
        if(null==subjectList){
            subjectList=new ArrayList<>();
            subjectMapper.put(tag,subjectList);
        }
        Subject<T>subject=PublishSubject.create();
        subjectList.add(subject);
        return subject;
    }
    public <T>void unregister(@NonNull Class<T>clz,@NonNull Observable observable){
        unregister(clz.getName(),observable);
    }

    public void unregister(@NonNull Object tag,@NonNull Observable observable){
        List<Subject>subjects=subjectMapper.get(tag);
        if(null!=subjects){
            subjects.remove(observable);
            if(subjects.isEmpty()){
                subjectMapper.remove(tag);
            }
        }
    }

    public void post(@NonNull Object content){

        post(content.getClass().getName(),content);
    }
    public void post(@NonNull Object tag,@NonNull Object content){
        List<Subject>subjects=subjectMapper.get(tag);
        if(!subjects.isEmpty()){
            for(Subject subject:subjects){
                subject.onNext(content);
            }
        }
    }

    private  static class Holder{
        private static Rxbus instance=new Rxbus();
    }
}
