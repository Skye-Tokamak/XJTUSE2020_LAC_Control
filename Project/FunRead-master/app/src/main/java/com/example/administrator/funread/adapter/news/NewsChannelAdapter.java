package com.example.administrator.funread.adapter.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.news.NewsChannelBean;
import com.example.administrator.funread.interfaces.IOnDragVHListener;
import com.example.administrator.funread.interfaces.IOnItemMoveListener;
import com.example.administrator.funread.module.news.NewsTabLayout;

import java.util.List;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 作者：created by weidiezeng on 2019/8/10 09:41
 * 邮箱：1067875902@qq.com
 * 描述：栏目添加操作界面适配器
 */
public class NewsChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IOnItemMoveListener {
   //我的频道 标题部分
    public  static final int TYPE_MY_CHANNEL_HEADER=0;
    //我的频道
    public static final int TYPE_MY=1;
    //其他频道 标题部分
    public static final  int TYPE_OTHER_CHANNEL_HEADER=2;
    //其他频道
    public static final int TYPE_OTHER=3;

    //我的频道之前的header数量，该demo中 即标题部分 为 1
    private static final int COUNT_PRE_MY_HEADER=1;
    // 其他频道之前的header数量  该demo中 即标题部分 为 COUNT_PRE_MY_HEADER + 1
    private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;

    private static final long ANIM_TIME=360L;
    private static final long SPACE_TIME = 100;
    // touch 点击开始时间
    private long startTime;
    private LayoutInflater mInflater;
    private ItemTouchHelper mItemTouchHelper;

    //是否为编辑模式
    private boolean isEditMode;
    private List<NewsChannelBean>mMyChannelItems,mOtherChannelItems;

    private OnMyChannelItemClickListener mChannelItemClickListener;
    private Handler delayHandler=new Handler();

    public NewsChannelAdapter(Context context,ItemTouchHelper helper,List<NewsChannelBean> mMyChannelItems, List<NewsChannelBean> mOtherChannelItems){

        this.mInflater=LayoutInflater.from(context);
        this.mItemTouchHelper=helper;
        this.mMyChannelItems=mMyChannelItems;
        this.mOtherChannelItems=mOtherChannelItems;
    }

    public List<NewsChannelBean>getMyChannelItems(){
        return mMyChannelItems;
    }
    public List<NewsChannelBean>getOtherChannelItems(){
        return mOtherChannelItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_MY_CHANNEL_HEADER;
        }else if(position==mMyChannelItems.size()+1){
            return  TYPE_OTHER_CHANNEL_HEADER;
        }else if(position>0&&position<mMyChannelItems.size()+1){
            return TYPE_MY;
        }else {
            return TYPE_OTHER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        final View view;
        switch (i){
            case TYPE_MY_CHANNEL_HEADER:
               view=mInflater.inflate(R.layout.item_channel_my_header,group,false);

               final MyChannelHeaderViewHolder holder=new MyChannelHeaderViewHolder(view);
               holder.tvBtnEdit.setOnClickListener(v->{
                   if(!isEditMode){
                       startEditMode((RecyclerView) group);
                       holder.tvBtnEdit.setText(R.string.finish);

                   }else {
                       cancleEditMode((RecyclerView)group);
                       holder.tvBtnEdit.setText(R.string.edit);
                   }
               });
               return holder;
            case TYPE_MY:
                view=mInflater.inflate(R.layout.item_channel_my,group,false);
                final MyViewHolder myViewHolder=new MyViewHolder(view);
                //点击移动事件
                myViewHolder.mTextView.setOnClickListener(v-> {
                    int position=myViewHolder.getAdapterPosition();
                    if(isEditMode){

                        RecyclerView recyclerView=((RecyclerView)group);
                        //目标位置为隐藏频道的第一行第一个
                        View targetView=recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size()+COUNT_PRE_OTHER_HEADER);
                        //当前位置
                        View currentView=recyclerView.getLayoutManager().findViewByPosition(position);
                        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if(recyclerView.indexOfChild(targetView)>=0){
                            //获取隐藏频道第一行第一个坐标
                            int targetX,targetY;

                            RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
                            //返回一行有多少个
                            int spanCount=((GridLayoutManager)manager).getSpanCount();
                            //我的频道最后一个item在新的一行第一个，那么移动后，我的频道高度将发生变化
                            //则目标位置也要改变
                            if((mMyChannelItems.size()-COUNT_PRE_MY_HEADER)%spanCount==0){
                                View preTargetView=recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size()+COUNT_PRE_OTHER_HEADER-1);
                                targetX=preTargetView.getLeft();
                                targetY=preTargetView.getTop();
                            }else{
                                targetX=targetView.getLeft();
                                targetY=targetView.getTop();
                            }
                            moveMyToOther(myViewHolder);
                            startAnimation(recyclerView,currentView,targetX,targetY);
                        }else {
                            moveMyToOther(myViewHolder);
                        }
                    }else{
                        //
                        mChannelItemClickListener.onItemClick(v,position-COUNT_PRE_MY_HEADER);
                    }
                });
                //长按事件处理
                myViewHolder.mTextView.setOnLongClickListener(v -> {
                    if(!isEditMode){
                        RecyclerView recyclerView=(RecyclerView)group;
                        startEditMode(recyclerView);
                        View view1=group.getChildAt(0);
                        if(view1==((RecyclerView) group).getLayoutManager().findViewByPosition(0)){
                            TextView tvBtnEdit=view1.findViewById(R.id.tv_btn_edit);
                            tvBtnEdit.setText(R.string.finish);
                        }
                    }
                    mItemTouchHelper.startDrag(myViewHolder);//拖动排序
                    return true;
                });
                //点击到长按
                myViewHolder.mTextView.setOnTouchListener((v,event)->{
                    if(isEditMode){
                        switch (MotionEventCompat.getActionMasked(event)){
                            case MotionEvent.ACTION_DOWN:
                                startTime=System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if(System.currentTimeMillis()-startTime>SPACE_TIME){
                                    mItemTouchHelper.startDrag(myViewHolder);
                                 }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                startTime = 0;
                                break;
                        }
                    }
                    return  false;

                });
                return myViewHolder;
            case TYPE_OTHER_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_channel_other_header, group, false);
                return new RecyclerView.ViewHolder(view) {
                };
            case TYPE_OTHER:
                view=mInflater.inflate(R.layout.item_channel_other,group,false);
                final  OtherViewHolder otherViewHolder=new OtherViewHolder(view);
                otherViewHolder.mTextView.setOnClickListener(v->{
                    RecyclerView recyclerView=(RecyclerView)group;
                    RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
                    int currentPosition=otherViewHolder.getAdapterPosition();
                    View currentView=manager.findViewByPosition(currentPosition);
                    View preTargetView=manager.findViewByPosition(mMyChannelItems.size()-1+COUNT_PRE_OTHER_HEADER);
                    // 如果targetView不在屏幕内,则为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                    // 如果在屏幕内,则添加一个位移动画
                    if(recyclerView.indexOfChild(preTargetView)>=0){
                        int targetX = preTargetView.getLeft();
                        int targetY = preTargetView.getTop();

                        int targetPosition = mMyChannelItems.size() - 1 + COUNT_PRE_OTHER_HEADER;

                        GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
                        int spanCount = gridLayoutManager.getSpanCount();
                        // target 在最后一行第一个
                        if ((targetPosition - COUNT_PRE_MY_HEADER) % spanCount == 0) {
                            View targetView = manager.findViewByPosition(targetPosition);
                            targetX = targetView.getLeft();
                            targetY = targetView.getTop();
                        } else {
                            targetX += preTargetView.getWidth();

                            // 最后一个item可见
                            if (gridLayoutManager.findLastVisibleItemPosition() == getItemCount() - 1) {
                                // 最后的item在最后一行第一个位置
                                if ((getItemCount() - 1 - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
                                    // RecyclerView实际高度 > 屏幕高度 && RecyclerView实际高度 < 屏幕高度 + item.height
                                    int firstVisiblePostion = gridLayoutManager.findFirstVisibleItemPosition();
                                    if (firstVisiblePostion == 0) {
                                        // FirstCompletelyVisibleItemPosition == 0 即 内容不满一屏幕 , targetY值不需要变化
                                        // // FirstCompletelyVisibleItemPosition != 0 即 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                                        if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                            int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
                                            targetY += offset;
                                        }
                                    } else { // 在这种情况下 并且 RecyclerView高度变化时(即可见第一个item的 position != 0),
                                        // 移动后, targetY值  + 一个item的高度
                                        targetY += preTargetView.getHeight();
                                    }
                                }
                            } else {
                                System.out.println("current--No");
                            }
                        }

                        // 如果当前位置是otherChannel可见的最后一个
                        // 并且 当前位置不在grid的第一个位置
                        // 并且 目标位置不在grid的第一个位置

                        // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                        // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                        if (currentPosition == gridLayoutManager.findLastVisibleItemPosition()
                                && (currentPosition - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0
                                && (targetPosition - COUNT_PRE_MY_HEADER) % spanCount != 0) {
                            moveOtherToMyWithDelay(otherViewHolder);
                        } else {
                            moveOtherToMy(otherViewHolder);
                        }
                        startAnimation(recyclerView, currentView, targetX, targetY);
                    }
                });
                return otherViewHolder;


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder=(MyViewHolder)holder;
            myViewHolder.mTextView.setText(mMyChannelItems.get(i-COUNT_PRE_MY_HEADER).getChannelName());
            if(isEditMode){
                myViewHolder.imgEdit.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.imgEdit.setVisibility(View.INVISIBLE);
            }
        }else if(holder instanceof OtherViewHolder){
            ((OtherViewHolder) holder).mTextView.setText(mOtherChannelItems.get(i-mMyChannelItems.size()-COUNT_PRE_OTHER_HEADER).getChannelName());

        }else if(holder instanceof MyChannelHeaderViewHolder){
            MyChannelHeaderViewHolder myChannelHeaderViewHolder=(MyChannelHeaderViewHolder)holder;
            if(isEditMode){
                myChannelHeaderViewHolder.tvBtnEdit.setText(R.string.finish);
            }else {
                myChannelHeaderViewHolder.tvBtnEdit.setText(R.string.edit);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mMyChannelItems.size()+mOtherChannelItems.size()+COUNT_PRE_OTHER_HEADER;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        NewsChannelBean item = mMyChannelItems.get(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 开始动画
     * @param recyclerView
     * @param currentView
     * @param targetX
     * @param targetY
     */
    private void startAnimation(RecyclerView recyclerView,final View currentView,float targetX,float targetY){
        final ViewGroup viewGroup=(ViewGroup)recyclerView.getParent();
        final ImageView mirrorView=addMirrorView(viewGroup,recyclerView,currentView);
        Animation animation=getTranslateAnimator(
                targetX-currentView.getLeft(),targetY-currentView.getTop()
        );
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if(currentView.getVisibility()==View.INVISIBLE){
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });

    }

    /**
     * 从我的频道移动到其他频道
     * @param myViewHolder
     */
    private void moveMyToOther(MyViewHolder myViewHolder){
        int position=myViewHolder.getAdapterPosition();
        int startPosition=position-COUNT_PRE_MY_HEADER;
        if(startPosition>mMyChannelItems.size()-1){
            return;
        }
        NewsChannelBean bean=mMyChannelItems.get(startPosition);
        mMyChannelItems.remove(startPosition);
        mOtherChannelItems.add(0,bean);
        //通知任何已登记的观察员，在fromPosition处反映的项目已移至toPosition
        notifyItemMoved(position, mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);

    }

    /**
     * 从其他频道移动到我的频道
     * @param otherViewHolder
     */
    private void moveOtherToMy(OtherViewHolder otherViewHolder){
        final int position=processItemRemoveAdd(otherViewHolder);
        if(position==-1){
            return;
        }
        //通知任何已登记的观察员，在fromPosition处反映的项目已移至toPosition
        notifyItemMoved(position,mMyChannelItems.size()-1+COUNT_PRE_MY_HEADER);
    }

    /**
     * 其他频道 移动到 我的频道 伴随延迟
     * @param otherViewHolder
     */
    private void moveOtherToMyWithDelay(OtherViewHolder otherViewHolder){
        final int position=processItemRemoveAdd(otherViewHolder);
        if(position==-1){
            return;
        }
        delayHandler.postDelayed(() -> notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER), ANIM_TIME);
    }
    private int processItemRemoveAdd(OtherViewHolder otherHolder) {
        int position = otherHolder.getAdapterPosition();

        int startPosition = position - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER;
        if (startPosition > mOtherChannelItems.size() - 1) {
            return -1;
        }
        NewsChannelBean item = mOtherChannelItems.get(startPosition);
        mOtherChannelItems.remove(startPosition);
        mMyChannelItems.add(item);
        return position;
    }
    /**
     * 添加需要移动的镜像view
     * @param parent
     * @param recyclerView
     * @param view
     * @return
     */
    private ImageView addMirrorView(ViewGroup parent,RecyclerView recyclerView,View view){
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，如果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView=new ImageView(recyclerView.getContext());
        Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations=new int[2];
        view.getLocationOnScreen(locations);
        int[] parentLocations=new int[2];
        recyclerView.getLocationOnScreen(parentLocations);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(bitmap.getWidth(),bitmap.getHeight());
        //设置边距
        params.setMargins(locations[0],locations[1]-parentLocations[1],0,0);
        parent.addView(mirrorView,params);
        return mirrorView;
    }

    private TranslateAnimation getTranslateAnimator(float targetX,float targetY){
        //构造函数，当从代码构建翻译动画时使用
        TranslateAnimation translateAnimation=new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,
                Animation.ABSOLUTE,targetX,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.ABSOLUTE,targetY
        );
        //设置持续时间
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }
    /**
     * 开启编辑模式
     * @param parent
     */
    private void startEditMode(RecyclerView parent){
        isEditMode=true;
        int visibleChildCount=parent.getChildCount();
        for(int i=0;i<visibleChildCount;i++){
            View view=parent.getChildAt(i);
            ImageView imgEdit=view.findViewById(R.id.img_edit);
            if(imgEdit!=null){
                imgEdit.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 编辑完成模式
     * @param parent
     */
    private void cancleEditMode(RecyclerView parent){
        isEditMode=false;
        int visibleChildCount=parent.getChildCount();
        for(int i=0;i<visibleChildCount;i++){
            View view=parent.getChildAt(i);
            ImageView imgEdit=view.findViewById(R.id.img_edit);
            if(imgEdit!=null){
                imgEdit.setVisibility(View.INVISIBLE);
            }
        }
    }
    public interface OnMyChannelItemClickListener {
        void onItemClick(View v, int position);

    }
    //接口回调
    public void setOnMyChannelItemClickListener(OnMyChannelItemClickListener listener) {
        this.mChannelItemClickListener = listener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements IOnDragVHListener{

        private TextView mTextView;
        private ImageView imgEdit;

        public MyViewHolder(View view){
            super(view);
            mTextView=view.findViewById(R.id.tv);
            imgEdit=view.findViewById(R.id.img_edit);
        }

        /**
         * item被选中
         */
        @Override
        public void onItemSelected() {

            mTextView.setBackgroundResource(R.color.textColorPrimary);
        }

        /**
         * 取消选中
         */
        @Override
        public void onItemFinish() {
            mTextView.setBackgroundResource(R.color.viewBackground);
        }
    }
    class OtherViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        public OtherViewHolder(View view){
            super(view);
            mTextView=view.findViewById(R.id.tv);
        }
    }
    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvBtnEdit;
        public MyChannelHeaderViewHolder(View view){
            super(view);
            tvBtnEdit=view.findViewById(R.id.tv_btn_edit);
        }
    }
}
