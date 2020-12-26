package com.walk.onyourside.managers;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.walk.onyourside.R;
import com.walk.onyourside.model.BuildingBlock;

import java.util.ArrayList;
import java.util.List;

public class BuildingBlocksManager {

    private Context context;
    private ConstraintLayout layout;
    private List<ImageButton> blocks;
    private View.OnClickListener[] listeners;
    private int[] drawableIds;

    private ImageButton highlightedBlock;

    LayoutManager layoutManager;

    private  Activity activity;

    private int previousBackgroundColor;

    public BuildingBlocksManager(Context context, Activity activity, ConstraintLayout layout, LayoutManager layoutManager){
        this.context = context;
        this.layout = layout;
        this.activity = activity;
        this.layoutManager = layoutManager;
    }

    public void PopulateBuildingBlocks(View.OnClickListener[] listeners, int[] drawableIds){

        this.listeners = listeners;
        this.drawableIds = drawableIds;
        blocks = new ArrayList<>();
        Init();

    }

    private void Init(){

        int leftMargin = 30;

        int topMargin = 0;
        View previousView = layout;

        for(int i = 0; i < listeners.length; i++){

            ImageButton im = CreateNewBuildingBlockButton(drawableIds[i], listeners[i]);
            blocks.add(im);

            int[] h = new int[]{ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, leftMargin};
            int[] v = new int[]{ConstraintSet.TOP, previousView.getId(), ConstraintSet.TOP, 90 + topMargin};

            PopulateConstructionTools(blocks.get(i), h, v);
            topMargin = 30;
            previousView = im;

        }

    }


    public ImageButton CreateNewBuildingBlockButton(int resourceId, View.OnClickListener blockListener){

        ImageButton imageButton = new ImageButton(context);
        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
        imageButton.setId(View.generateViewId());
        imageButton.setImageResource(resourceId);
        imageButton.setOnClickListener(blockListener);
        imageButton.setScaleX(0.8f);
        imageButton.setScaleY(0.8f);

        return imageButton;

    }

    public BuildingBlock InstantiateBuildingBlock() {

        final BuildingBlock buildingBlock = new BuildingBlock(context);
        buildingBlock.setBuildingBlock();
        return buildingBlock;

    }

    //Adds a BuildingBlock button in the 'Construction Toolbar'
    public void PopulateConstructionTools(View view, int[] h, int[] v){
        layoutManager.PlaceInLayout(view, h, v);
    }

    public void DisableConstructionTools(){

        for(int i = 0;i < blocks.size(); i++){
            blocks.get(i).setEnabled(false);
        }

    }

    public void EnableConstructionTools(){

        for(int i = 0;i < blocks.size(); i++){
            blocks.get(i).setEnabled(true);
        }

    }

    public void HighlightBlockToBeSelected(int index){

        if(highlightedBlock != null)
            highlightedBlock.setBackgroundColor(Color.LTGRAY);

        highlightedBlock = blocks.get(index);

        highlightedBlock.setBackgroundColor(Color.parseColor("#99d066"));

    }

    public void HighlightViewToBeSelected(int id){

        if(highlightedBlock != null)
            highlightedBlock.setBackgroundColor(Color.LTGRAY);

        activity.findViewById(id).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#99d066")));

        highlightedBlock =  activity.findViewById(id);

    }

}
