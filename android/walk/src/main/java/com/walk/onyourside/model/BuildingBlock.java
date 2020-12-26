package com.walk.onyourside.model;

import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.walk.onyourside.interfaces.Interaction;


public class BuildingBlock extends AppCompatImageView {

    boolean isDraggable;
    int dX,dY;

    private Interaction interaction;
    private Rect expectedArea;

    public BuildingBlock(Context context){
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        super.onTouchEvent(motionEvent);

        final int X = (int) motionEvent.getRawX();
        final int Y = (int) motionEvent.getRawY();


        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) getLayoutParams();
                dX = X - lParams.leftMargin;
                dY = Y - lParams.topMargin;
                break;

            case MotionEvent.ACTION_UP:
//                performClick();

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                /*if (!isDraggable)
                    break;*/

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = X - dX;
                layoutParams.topMargin = Y - dY;
                setLayoutParams(layoutParams);

                /*Rect currentRect = new Rect(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + 100, layoutParams.topMargin + 100);

                if(expectedArea != null) {

                    if (currentRect.intersect(expectedArea)) {

                        isDraggable = false;
                        //Callback to the concept
                        interaction.OnComponentsIntersection();

                    }
                }*/


                break;

        }

        return true;
    }

    // Because we call this from onTouchEvent, this code will be executed for both
    // normal touch events and for when the system calls this using Accessibility
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }


    public void setBuildingBlock() {

        isDraggable = true;
        setId(View.generateViewId());

    }

    public void setDraggable(boolean draggable){
        this.isDraggable = draggable;
    }

    public void setExpectedArea(Rect expectedArea){
        this.expectedArea = expectedArea;
    }

    public void RegisterCallback(Interaction interaction){
        this.interaction = interaction;
    }

}

