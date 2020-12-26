package com.walk.onyourside.concepts;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.walk.onyourside.model.BuildingBlock;
import com.walk.onyourside.managers.DragDropManager;
import com.walk.onyourside.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

class Element{

    ImageView elementBox;
    TextView number;

}

public class QuickSort extends Concept {

    private int lastViewId;
    private int leftMargin = 150;
    private int topMargin = 80;

    private int numberOfElements;

    private List<ImageView> elements;

    private View.OnClickListener elementListener;
    private View.OnClickListener populateListener;

    private View.OnClickListener incrementListener;

    private Random random;

    public QuickSort(Context context, Activity activity, final ConstraintLayout layout) {

        super(context,activity, layout);

        elements = new ArrayList<>();
        lastViewId = layout.getId();

        numberOfElements = 7;

        random = new Random();
        elementListener = v -> {
            lastViewId = layout.getId();
            leftMargin = 160;
            topMargin = 180;

            int endSide = ConstraintSet.LEFT;

            for (int i = 0; i < numberOfElements; i++) {
                BuildingBlock element = buildingBlocksManager.InstantiateBuildingBlock();
                element.setImageResource(R.drawable.rect);
                element.setOnClickListener(buildingBlockTouchListener);
                int finalI = i;
                element.setOnLongClickListener(v1 -> {

                    ClipData.Item item = new ClipData.Item("Element");
                    element.setTag(finalI);
                    ClipData dragData = new ClipData(String.valueOf(element.getTag()), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                    //instantiate dragShadow builder
                    View.DragShadowBuilder myShadow = new DragDropManager.MyDragShadowBuilder(element);

                    //Starts the drag
                    element.startDrag(dragData, myShadow, null, 0);
                    return true;
                });

                constraintsH = new int[]{ConstraintSet.LEFT, lastViewId, endSide, leftMargin};
                constraintsV = new int[]{ConstraintSet.TOP, lastViewId, ConstraintSet.TOP, topMargin};

                layoutManager.PlaceInLayout(element, constraintsH, constraintsV);
                lastViewId = element.getId();

                leftMargin = 0;
                topMargin = 0;
                elements.add(element);
                endSide = ConstraintSet.RIGHT;

            }

            buildingBlockCallback.CallbackAfterConcept();

        };


        populateListener = v -> {
            for (int i = 0; i < numberOfElements; i++) {
                PopulateElement(elements.get(i).getId(), String.valueOf(random.nextInt(100)));
            }

            buildingBlockCallback.CallbackAfterConcept();

        };

        incrementListener = v -> {
            Toast.makeText(context, "Increment", Toast.LENGTH_SHORT).show();
            final float destination = currentMarker.getPivotX()+ 80f;

            ObjectAnimator animation = ObjectAnimator.ofFloat(currentMarker, "translationX", destination);
            animation.setDuration(1000);
            animation.start();
        };

    }

    @Override
    public void InitConstruction() {

        super.InitConstruction();
        lastViewId = layout.getId();
        View.OnClickListener[] listeners = new View.OnClickListener[]{elementListener, populateListener, incrementListener};

        int[] drawableIds = new int[]{R.drawable.ic_check_box_outline_blank_black_24dp, R.drawable.ic_create_black_24dp, R.drawable.plus_1};
        //Instantiate the building block
        buildingBlocksManager.PopulateBuildingBlocks(listeners, drawableIds);

    }

    @Override
    public void SetBuildingBlocksBehaviour(Map<String, String> blockBehaviour) {
        super.SetBuildingBlocksBehaviour(blockBehaviour);

        if (blockBehaviour == null)
            return;

        for (Map.Entry<String, String> block : blockBehaviour.entrySet()) {

            System.out.println("Block value" + block.getValue());

            switch (block.getValue()) {
                case "mark":
                    SetElementsBehaviour(buildingBlockMarkListener);
                    break;

                case "highlight":
                    highlightListener = new HighlightListener(Color.GREEN);
                    SetElementsBehaviour(highlightListener);
                    break;

                case "swap":
                    Swap();
                    break;

                case "increment":
                    //find out where is the i mark Its in currentMarkedIndex
                    //move it over the next element

                    break;

                    default:
                        break;
            }

        }
    }

    private void SetElementsBehaviour(View.OnClickListener newListener) {

        if (newListener == null)
            return;

        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).setOnClickListener(newListener);
        }

    }

    private void Swap() {

        ImageView container = new ImageView(context);
        container.setId(View.generateViewId());
        container.setImageResource(R.drawable.container2);

        container.setBackgroundColor(Color.GREEN);
        constraintsH = new int[]{ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 100};
        constraintsV = new int[]{ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 100};

        layoutManager.PlaceInLayout(container, constraintsH, constraintsV);
        DragDropManager.SetContext(context);
        DragDropManager.MyDragEventListener myDragEventListener = new DragDropManager.MyDragEventListener();
        container.setOnDragListener(myDragEventListener);

    }


    public void PopulateElement(int elementId, String data) {

        TextView elementData = new TextView(context);
        elementData.setId(View.generateViewId());
        elementData.setText(data);

        constraintsH = new int[]{ConstraintSet.LEFT, elementId, ConstraintSet.LEFT, 10};
        constraintsV = new int[]{ConstraintSet.TOP, elementId, ConstraintSet.TOP, 13};

        layoutManager.PlaceInLayout(elementData, constraintsH, constraintsV);

    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }


}

