package com.example.administrator.funread.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;

//import com.example.administrator.funread.R;

import com.example.administrator.funread.R;
import com.example.administrator.funread.module.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by weidiezeng on 2019/8/21 11:02
 * 邮箱：1067875902@qq.com
 * 描述：更换图标控件
 */
public class IconListPreference extends ListPreference {

    private List<Drawable> drawableList = new ArrayList<>();

    public IconListPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        //从attrs.xml文件中获取IconListPreference 属性
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attributeSet,R.styleable.IconListPreference,0,0);
        CharSequence[] drawables;
        try {
            //获取iconsDrawable属性值
            drawables=typedArray.getTextArray(R.styleable.IconListPreference_iconsDrawables);
        }finally {
            typedArray.recycle();
        }

        for(CharSequence sequence:drawables){
            int resId=context.getResources().getIdentifier(sequence.toString(),"mipmap",context.getPackageName());
            Drawable drawable=context.getResources().getDrawable(resId);
            drawableList.add(drawable);
        }
        setWidgetLayoutResource(R.layout.item_icon_listpreference_preview);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        String value=getValue();
        int selectIndex=findIndexOfValue(value);
        Drawable drawable=drawableList.get(selectIndex);
        ( (ImageView)view.findViewById(R.id.iv_preview)).setImageDrawable(drawable);
    }

    /**
     * 创建适配器
     * @return
     */
    private ListAdapter createListAdapter() {
        final String selectedValue = getValue();
        int selectedIndex = findIndexOfValue(selectedValue);
        return new IconArrayAdapter(getContext(), R.layout.item_icon_listpreference,
                getEntries(), drawableList, selectedIndex);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {

        //为对话框设置适配器
        builder.setAdapter(createListAdapter(),this);
        builder.setNegativeButton(getContext().getString(R.string.cancel),(dialog, which) -> dialog.dismiss());

        super.onPrepareDialogBuilder(builder);
    }

    private class IconArrayAdapter extends ArrayAdapter<CharSequence>{
        private List<Drawable> list = null;
        private int selectedIndex = 0;

        public IconArrayAdapter(Context context,int textResourceId,CharSequence[] objects,
                                List<Drawable>imgDrawable,int selectedIndex){
            super(context,textResourceId,objects);
            this.list=imgDrawable;
            this.selectedIndex=selectedIndex;
        }

        @NonNull
        @Override
        @SuppressLint("ViewHolder")
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=((BaseActivity)getContext()).getLayoutInflater();
            View view=inflater.inflate(R.layout.item_icon_listpreference,parent,false);

            CheckedTextView textView = view.findViewById(R.id.label);
            textView.setText(getItem(position));
            textView.setChecked(position == selectedIndex);

            ImageView imageView = view.findViewById(R.id.icon);
            imageView.setImageDrawable(list.get(position));
            return view;
        }
    }
}
