package com.example.todolist

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class RVSwipeGestures(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val completeColor = ContextCompat.getColor(context,R.color.green)
    private val deleteColor = ContextCompat.getColor(context,R.color.red)
    private val completeIcon = R.drawable.ic_task_completed
    private val deleteIcon = R.drawable.ic_delete
    private val iconColor = ContextCompat.getColor(context,R.color.white)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .setActionIconTint(iconColor)
            .addSwipeRightBackgroundColor(completeColor)
            .addSwipeRightActionIcon(completeIcon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}