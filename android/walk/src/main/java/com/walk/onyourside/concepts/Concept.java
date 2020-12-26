package com.walk.onyourside.concepts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;

import com.walk.onyourside.model.BuildingBlock;
import com.walk.onyourside.managers.BuildingBlocksManager;
import com.walk.onyourside.interfaces.ConstructionState;
import com.walk.onyourside.interfaces.Interaction;
import com.walk.onyourside.managers.LayoutManager;
import com.walk.onyourside.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class Action{

    View v;
    View.OnClickListener listener;

    public Action(View v, View.OnClickListener listener){
        this.v = v;
        this.listener = listener;
    }

}

public class Concept implements Interaction {

    Context context;
    LayoutManager layoutManager;

    ConstraintLayout layout;
    BuildingBlocksManager buildingBlocksManager;
    private Stack<Action> actions;

    int[] constraintsH;
    int[] constraintsV;

    public BuildingBlockCallback buildingBlockCallback;

    View.OnClickListener buildingBlockTouchListener;

    View.OnClickListener buildingBlockMarkListener;

    View.OnClickListener buildingBlockDestroyListener;

    HighlightListener highlightListener;

    ConstructionState constructionState;

    int currentMarkedIndex;

    BuildingBlock currentMarker;

    public Concept() {

    }

    public Concept(Context context, Activity activity, ConstraintLayout layout) {

        this.context = context;
        this.layout = layout;
        layoutManager = new LayoutManager(layout);
        buildingBlocksManager = new BuildingBlocksManager(context,activity, layout, layoutManager);

        buildingBlockTouchListener = view -> {

            view.setBackgroundColor(Color.CYAN);
            AddToActionStack(view, buildingBlockTouchListener);
            view.setOnClickListener(null);
            buildingBlockCallback.CallbackAfterConcept();

        };

        buildingBlockMarkListener = view -> {

            //Create the arrow view
            //Place the arrow view over the clicked view
            BuildingBlock marker = buildingBlocksManager.InstantiateBuildingBlock();
            marker.setImageResource(R.drawable.ic_arrow_downward_some_blue_24dp);
            constraintsV = new int[]{ConstraintSet.BOTTOM, view.getId(), ConstraintSet.TOP, 5};
            constraintsH = new int[]{ConstraintSet.LEFT, view.getId(), ConstraintSet.LEFT, 10};
            layoutManager.PlaceInLayout(marker, constraintsH, constraintsV);
            if(view.getTag() != null)
                currentMarkedIndex = (Integer) view.getTag();

            marker.setTag(view.getTag());
            marker.setContentDescription("action");
            AddToActionStack(view, buildingBlockMarkListener);
            view.setOnClickListener(null);
            currentMarker = marker;
            buildingBlockCallback.CallbackAfterConcept();

        };

        buildingBlockDestroyListener = view-> {


            List<View> viewsToRemove = getViewsByTag((int) view.getTag());

            for(int i = 0; i < viewsToRemove.size(); i++){
                View v = viewsToRemove.get(i);
                v.setVisibility(View.INVISIBLE);
                AddToActionStack(v, buildingBlockMarkListener);

            }

            view.setOnClickListener(null);
            buildingBlockCallback.CallbackAfterConcept();

        };


        /*buildingBlockDestroyListener = view-> {

            List<View> viewsToRemove = getViewsByTag((int) view.getTag());

            for(int i = 0; i < viewsToRemove.size(); i++){
                layout.removeView(viewsToRemove.get(i));
            }

        };*/


    }


    public void InitConstruction() {
        actions = new Stack<>();
    }

    public void InitWYDIWYS(){

    }

    @Override
    public void OnComponentsIntersection() {

    }

    public interface BuildingBlockCallback {
        void CallbackAfterConcept();
    }

    public void RegisterCallback(BuildingBlockCallback buildingBlockCallback) {
        this.buildingBlockCallback = buildingBlockCallback;
    }

    public void SetBuildingBlocksBehaviour(Map<String, String> blockBehaviour) {

    }

    public void MakeFunctionCall(String functionName){

    }


    public void PreFunction(String functionName){

        while(!actions.isEmpty()){

            Action action = actions.pop();

            List<View> viewsToRemove = getViewsByTag((int)action.v.getTag());

            for(int i = 0; i < viewsToRemove.size(); i++){

                View v = viewsToRemove.get(i);
                if(v.getContentDescription() == "marker") {
                    layout.removeView(v);
                }

                else if(v.getContentDescription() == "highlight"){
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
            }


        }

    }

    public void OnFunctionFinish(String functionName) {

    }

    public void PopulateView(String data) {


    }

    private List<View> getViewsByTag(int tag){

        List<View> viewsByTag= new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++){
            View childAt = layout.getChildAt(i);

            if(childAt.getTag() != null) {
                int childTag = (int) childAt.getTag();
                if (childTag == tag)
                    viewsByTag.add(childAt);
            }
        }

        return viewsByTag;
    }

    public void RegisterFunctionCallback(ConstructionState constructionState){
        this.constructionState = constructionState;
    }

    void AddToActionStack(View v, View.OnClickListener listener){

        Action action = new Action(v, listener);
        actions.push(action);

    }


    public class HighlightListener implements View.OnClickListener {


        int color;
        public HighlightListener(int color) {
            this.color = color;
        }

        @Override
        public void onClick(View v)
        {
            v.setOnClickListener(null);
            v.setBackgroundColor(color);
            v.setContentDescription("highlight");
            AddToActionStack(v, highlightListener);

            buildingBlockCallback.CallbackAfterConcept();
        }

    }

    public void PreGameLevel(Map<String, String> gameLevelSta){

    }


}

