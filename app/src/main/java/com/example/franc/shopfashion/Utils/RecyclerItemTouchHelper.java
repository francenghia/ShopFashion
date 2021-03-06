package com.example.franc.shopfashion.Utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.franc.shopfashion.Adapter.CartViewHolder;
import com.example.franc.shopfashion.Adapter.FavoriteAdapter;
import com.example.franc.shopfashion.Adapter.FavoriteViewHolder;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null)
            listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FavoriteViewHolder) {
            View view_foreground = ((FavoriteViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().clearView(view_foreground);
        }else if (viewHolder instanceof CartViewHolder) {
            View view_foreground = ((CartViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().clearView(view_foreground);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (viewHolder instanceof FavoriteViewHolder) {
                View view_foreground = ((FavoriteViewHolder) viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(view_foreground);
            }else if (viewHolder instanceof CartViewHolder) {
                View view_foreground = ((CartViewHolder) viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(view_foreground);
            }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof FavoriteViewHolder) {
            View view_foreground = ((FavoriteViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, view_foreground, dX, dY, actionState, isCurrentlyActive);
        }else if (viewHolder instanceof CartViewHolder) {
            View view_foreground = ((CartViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, view_foreground, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof FavoriteViewHolder) {
            View view_foreground = ((FavoriteViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, view_foreground, dX, dY, actionState, isCurrentlyActive);
        }else if (viewHolder instanceof CartViewHolder) {
            View view_foreground = ((CartViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, view_foreground, dX, dY, actionState, isCurrentlyActive);
        }

    }
}
