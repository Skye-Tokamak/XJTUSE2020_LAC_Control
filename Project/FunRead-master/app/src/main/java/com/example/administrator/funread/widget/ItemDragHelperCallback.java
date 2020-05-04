package com.example.administrator.funread.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.administrator.funread.interfaces.IOnDragVHListener;
import com.example.administrator.funread.interfaces.IOnItemMoveListener;

/**
 * 作者：created by weidiezeng on 2019/8/11 09:43
 * 邮箱：1061875902@qq.com
 * 描述：
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
    @Override
    public int getMovementFlags(@NonNull RecyclerView view, @NonNull RecyclerView.ViewHolder holder) {
        int dragFlags;
        RecyclerView.LayoutManager manager=view.getLayoutManager();
        if(manager instanceof GridLayoutManager||manager instanceof StaggeredGridLayoutManager){
            dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

        }else {
            dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        }
        // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView view, @NonNull RecyclerView.ViewHolder holder, @NonNull RecyclerView.ViewHolder holder1) {
        // 不同Type之间不可移动
        if (holder.getItemViewType() != holder1.getItemViewType()) {
            return false;
        }

        if (view.getAdapter() instanceof IOnItemMoveListener) {
            IOnItemMoveListener listener = ((IOnItemMoveListener) view.getAdapter());
            listener.onItemMove(holder.getAdapterPosition(), holder1.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int i) {

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        //不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof IOnDragVHListener) {
                IOnDragVHListener itemViewHolder = (IOnDragVHListener) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof IOnDragVHListener) {
            IOnDragVHListener itemViewHolder = (IOnDragVHListener) viewHolder;
            itemViewHolder.onItemFinish();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // 不支持长按拖拽功能 手动控制
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 不支持滑动功能
        return false;
    }
}
