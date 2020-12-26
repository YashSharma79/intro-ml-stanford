package com.walk.onyourside.concepts;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.walk.onyourside.model.BuildingBlock;
import com.walk.onyourside.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

class Node{

    ImageView node;
    TextView data;
    ImageView pointer;

    int tag;

    public Node(ImageView node){
        this.node = node;
    }
}

public class LinkedList extends Concept{

    private int lastViewId;
    private int lastNodeId;

    private View.OnClickListener nodeListener;
    private View.OnClickListener pointerListener;

    private View.OnLongClickListener pointerTapListener;

    private Node currentNode;
    private View.OnClickListener currentListener;
    private View.OnClickListener markHeadListener;
    private List<Node> nodes;

    private Random random;

    private ImageView dottedRectangle;

    private Rect currentExpectedArea;

    private int nodeWidth;

    private int linkedListSize;

    private String clipboard;

    private List<View> tempViews;
    private ImageView head;

    public LinkedList(Context context, Activity activity, ConstraintLayout layout){

        super(context,activity, layout);

        this.lastViewId = layout.getId();
        this.lastNodeId = lastViewId;


        nodeListener = v -> {

            CreateNode(false, 0, 200);
            buildingBlockCallback.CallbackAfterConcept();

        };

        pointerListener = v -> {

            CreatePointer();
            buildingBlockCallback.CallbackAfterConcept();

        };

        pointerTapListener = v -> {

            TextView t = new TextView(context);
            t.setTextColor(Color.BLACK);
            t.setId(View.generateViewId());
            SetClipboard(v.getContentDescription().toString());
            String address = "Address to " + v.getContentDescription();
            t.setText(address);
            constraintsV = new int[]{ConstraintSet.BOTTOM, v.getId(), ConstraintSet.TOP, 25};
            constraintsH = new int[]{ConstraintSet.LEFT, v.getId(), ConstraintSet.LEFT, 5};
            layoutManager.PlaceInLayout(t, constraintsH, constraintsV);
            t.setTag(v.getTag());
            tempViews.add(t);
            buildingBlockCallback.CallbackAfterConcept();
            return true;
//            AddToActionStack(t, pointerTapListener);

        };

        markHeadListener = view -> {

            view.setOnClickListener(null);

            if(head != null){
                layout.removeView(head);
            }

            //Create the arrow view
            //Place the arrow view over the clicked view
            head = new ImageView(context);

            head.setImageResource(R.drawable.crown);
            head.setId(View.generateViewId());
            head.setTag(view.getTag());
            head.setContentDescription("marker");

            constraintsV = new int[]{ConstraintSet.BOTTOM, view.getId(), ConstraintSet.TOP, 5};
            constraintsH = new int[]{ConstraintSet.LEFT, view.getId(), ConstraintSet.LEFT, 10};
            layoutManager.PlaceInLayout(head, constraintsH, constraintsV);

            AddToActionStack(view, markHeadListener);

            //remove previous head
            buildingBlockCallback.CallbackAfterConcept();


        };

    }

    @Override
    public void InitConstruction(){

        super.InitConstruction();
        int[] drawableIds = new int[]{R.drawable.ic_check_box_outline_blank_black_24dp, R.drawable.ic_arrow_forward_black_24dp};
        buildingBlocksManager.PopulateBuildingBlocks(new View.OnClickListener[]{nodeListener, pointerListener}, drawableIds);
        nodes = new ArrayList<>();
        random = new Random();
        tempViews = new ArrayList<>();
        currentExpectedArea = new Rect( layout.getLeft() + 180, layout.getTop() + 200 , layout.getLeft() + 250, layout.getTop() + 340);
        linkedListSize = 0;

    }


    @Override
    public void InitWYDIWYS(){

        nodes = new ArrayList<>();
        random = new Random();
        currentExpectedArea = new Rect( layout.getLeft() + 180, layout.getTop() + 200 , layout.getLeft() + 250, layout.getTop() + 340);
        linkedListSize = 0;

    }


    private void SetClipboard(String copiedData){
        Toast.makeText(context,"Address Copied", Toast.LENGTH_SHORT).show();
        clipboard = copiedData;
    }

    private void CreateNode(boolean chainToList, int leftMargin, int topMargin ){

        BuildingBlock nodeView = buildingBlocksManager.InstantiateBuildingBlock();

        nodeView.setImageResource(R.drawable.rect_small);
        nodeView.RegisterCallback(this);
        currentNode = new Node(nodeView);
        currentNode.tag = random.nextInt();
        nodeView.setTag(currentNode.tag);


        if (chainToList) {
            nodeView.setDraggable(false);
            constraintsH = new int[]{ConstraintSet.LEFT, getLastViewId(), ConstraintSet.RIGHT, leftMargin};
            constraintsV = new int[]{ConstraintSet.TOP,  getLastNodeId(), ConstraintSet.TOP, topMargin};
        }

        else {

//            SetExpectedArea(nodeView);
            constraintsH = new int[]{ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 350};
            constraintsV = new int[]{ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, topMargin};

        }

//        nodeView.setOnClickListener(buildingBlockTouchListener);
        layoutManager.PlaceInLayout(nodeView, constraintsH, constraintsV );

        nodeWidth =  nodeView.getDrawable().getIntrinsicWidth();
        nodeWidth += 50;

        //left top right bottom
        currentExpectedArea = new Rect( currentExpectedArea.right , currentExpectedArea.top , currentExpectedArea.right + nodeWidth, currentExpectedArea.bottom);

        nodes.add(currentNode);

        nodeView.setOnClickListener(currentListener);
        setLastViewId(nodeView.getId());
        setLastNodeId(nodeView.getId());


        linkedListSize++;
    }

    private void CreatePointer(){

        constraintsH = new int[]{ConstraintSet.LEFT, getLastViewId(), ConstraintSet.RIGHT, 0} ;
        constraintsV = new int[]{ConstraintSet.TOP, getLastViewId(), ConstraintSet.TOP, 30};

        BuildingBlock pointerView = buildingBlocksManager.InstantiateBuildingBlock();
        pointerView.setImageResource(R.drawable.forward_arrow_red);
        currentNode.pointer = pointerView;
        pointerView.setTag(currentNode.tag);
        pointerView.setContentDescription("null node");

        if(nodes.size() > 1)
            nodes.get(nodes.size() - 2).pointer.setContentDescription(String.valueOf(linkedListSize - 1));

        pointerView.setDraggable(false);

        /*pointerView.setOnClickListener(v -> {
            pointerView.setImageResource(R.drawable.forward_arrow_blue);
            buildingBlockCallback.CallbackAfterConcept();
        });*/

        pointerView.setOnLongClickListener(pointerTapListener);

        layoutManager.PlaceInLayout(pointerView, constraintsH, constraintsV);

        setLastViewId(pointerView.getId());
        setLastNodeId(getLastNodeId());

    }

    @Override
    public void PopulateView(String data){

        int nodeId = getLastNodeId();
        TextView nodeData = new TextView(context);
        nodeData.setId(View.generateViewId());
        nodeData.setText(data);
        currentNode.data = nodeData;
        nodeData.setTag(currentNode.tag);
        constraintsH = new int[]{ConstraintSet.LEFT, nodeId, ConstraintSet.LEFT, 20};
        constraintsV = new int[]{ConstraintSet.TOP, nodeId, ConstraintSet.TOP, 25};

        layoutManager.PlaceInLayout( nodeData, constraintsH, constraintsV );
//        constructionState.SetDataInputUI();

    }

    @Override
    public void PreFunction(String functionName){

        super.PreFunction(functionName);
        switch (functionName){
            case "Traverse":
                for(int i = 0; i < 2; i++){

                    CreateNode(true, 0, 0);
                    CreatePointer();
                    PopulateView(String.valueOf(random.nextInt(10000)));

                }

                for (int i = 0; i < nodes.size() - 1; i++) {
                    nodes.get(i).pointer.setImageResource(R.drawable.forward_arrow_small);
                }

                SetNodeBehaviour(null);

                break;

            case "Insert":
                ClearTempViews();
                MarkNode(1);
                break;

            case "Insert Beginning":
                ClearTempViews();
                break;

            case "Delete":
                ClearTempViews();
                MarkNode(2);
                break;

                default:
                    break;
        }

        //reset globals
        //try to remove the globals

    }

    private void ClearTempViews(){
        for(int i = 0; i < tempViews.size(); i++){
            layout.removeView(tempViews.get(i));
        }

    }

    private void MarkNode(int nodeIndex){

        View view = nodes.get(nodeIndex).pointer;
        //Instantiate the arrow over the third position pointer
        BuildingBlock marker = buildingBlocksManager.InstantiateBuildingBlock();
        marker.setImageResource(R.drawable.ic_arrow_downward_some_blue_24dp);
        constraintsV = new int[]{ConstraintSet.BOTTOM, view.getId(), ConstraintSet.TOP, 5};
        constraintsH = new int[]{ConstraintSet.LEFT, view.getId(), ConstraintSet.LEFT, 10};
        layoutManager.PlaceInLayout(marker, constraintsH, constraintsV);
        marker.setTag(view.getTag());
        marker.setContentDescription("marker");
        AddToActionStack(view, buildingBlockMarkListener);

    }


    @Override
    public void OnFunctionFinish(String functionName){

        super.OnFunctionFinish(functionName);

        buildingBlocksManager.DisableConstructionTools();

        //How to map the function name with its power
        //It has to be turned into a power now

        //The function names we should get from JSON and not hardcode here
        switch (functionName){
            case "Create":
                break;

            case "Traverse":
                SetNodeBehaviour(null);
                break;

            default:
                buildingBlocksManager.EnableConstructionTools();

//                constructionState.ChangeFunction();
                break;
        }

    }

    @Override
    public void SetBuildingBlocksBehaviour(Map<String, String> blockBehaviour) {
        super.SetBuildingBlocksBehaviour(blockBehaviour);

        if(blockBehaviour == null){
            currentListener = null;
            SetNodeBehaviour(null);
            return;
        }

        System.out.println("Setting building block listener");

        for(Map.Entry<String, String> block : blockBehaviour.entrySet()){
            System.out.println("Block value" +  block.getValue());

            switch (block.getValue()){

                case "drag":

                    break;

                case "highlight":
                    highlightListener = new HighlightListener(Color.GREEN);
                    SetNodeBehaviour(highlightListener);
                    break;

                case "mark":
                    SetNodeBehaviour(buildingBlockMarkListener);
                    break;

                case "destroy":
                    SetNodeBehaviour(buildingBlockDestroyListener);
                    break;

                case "reset":
                    SetNodeBehaviour(null);
                    break;

                case "currentBlock":
                    HighlightBlockToBeSelected(block.getKey());
                    break;

                case "delete":
                    highlightListener = new HighlightListener(Color.RED);
                    SetNodeBehaviour(highlightListener);
                    break;

                case "visible":
                    constructionState.SetDataInputUI(View.VISIBLE);
                    break;

                case "head":
                    SetNodeBehaviour(markHeadListener);
                    break;

                case "copy":
                    break;

                case "assign":
                    Toast.makeText(context,"Address assigned" + clipboard, Toast.LENGTH_SHORT).show();
                    nodes.get(nodes.size()-1).pointer.setContentDescription(clipboard);
                    break;

                case "space":
                    Toast.makeText(context, clipboard, Toast.LENGTH_SHORT).show();
                    InsertNodeInBetween(Integer.parseInt(clipboard));
                    //Get the node number which has to be moved
                    //Move it by a width of node+pointer
                    break;

                case "functions":
                    buildingBlocksManager.HighlightViewToBeSelected(R.id.functionShelfButton);
                    break;

                case "operators":
                    buildingBlocksManager.HighlightViewToBeSelected(R.id.operatorShelfButton);
                    break;

                default:
                    break;
            }
        }

    }


    @Override
    public void MakeFunctionCall(String functionName){
        switch(functionName){
            case "Create":
                CreateNode(false, 0, 300);
                CreatePointer();
                PopulateView(String.valueOf(random.nextInt(10000)));
                break;

            case "Traverse":
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable(){
                int nodeNumber = 0;
                    @Override
                    public void run(){
                        if(nodeNumber > 0){
                            nodes.get(nodeNumber-1).node.setBackgroundColor(Color.TRANSPARENT);
                        }

                        nodes.get(nodeNumber).node.setBackgroundColor(Color.parseColor("#66bb6a"));
                        nodes.get(nodeNumber).node.setContentDescription("highlight");
                        nodeNumber++;

                        handler.postDelayed(this, 1000);

                        if(nodeNumber >= 2){
                            handler.removeCallbacks(this);
                        }
                    }

                };

                handler.post(runnable);


                break;
        }

        buildingBlockCallback.CallbackAfterConcept();

    }

    private void SetNodeBehaviour(View.OnClickListener newListener){

        if(currentListener == newListener)
            return;

//       BuildingBlock.setDraggable(false);
        for(int i = 0; i < nodes.size(); i++){
            nodes.get(i).node.setOnClickListener(newListener);
        }
        currentListener = newListener;

    }

    private void HighlightBlockToBeSelected(String blockToHighlight){

        int index = 0;
        //remove the switch case, use a <String, int> map
        switch (blockToHighlight){
            case "node_icon":
                index = 0;
                break;

            case "pointer_icon":
                index = 1;
                break;
        }

        buildingBlocksManager.HighlightBlockToBeSelected(index);
    }


    private void ResizeLinkedList(int size){

        for(int i = 0; i < size; i++){

            CreateNode(true, 0, 0);
            CreatePointer();
            PopulateView(String.valueOf(random.nextInt(10000)));

        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            nodes.get(i).pointer.setImageResource(R.drawable.forward_arrow_small);
        }

//      constructionState.ChangeFunction();
//      InsertNodeInBetween();

    }

    private void InsertNodeInBetween(int nodeNum){

        ObjectAnimator animation = ObjectAnimator.ofFloat(nodes.get(nodeNum).node, "translationX", nodes.get(nodeNum).node.getPivotX() + 135f);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(nodes.get(nodeNum).pointer, "translationX", nodes.get(nodeNum).pointer.getPivotX() + 165f);
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(nodes.get(nodeNum).data, "translationX", nodes.get(nodeNum).data.getPivotX() + 175f);

        animation.setDuration(1000);
        animation2.setDuration(1000);
        animation3.setDuration(1000);

        animation.start();
        animation2.start();
        animation3.start();

    }

    private void setLastNodeId(int lastNodeId) {
        this.lastNodeId = lastNodeId;
    }

    private void setLastViewId(int lastViewId) {
        this.lastViewId = lastViewId;
    }

    private int getLastNodeId() {
        return lastNodeId;
    }

    private int getLastViewId() {
        return lastViewId;
    }

    private void SetExpectedArea(BuildingBlock b){

        dottedRectangle = new ImageView(context);
        dottedRectangle.setImageResource(R.drawable.dotted_rectangle);
        dottedRectangle.setId(View.generateViewId());

        int[] cH = new int[]{ConstraintSet.LEFT, getLastViewId(), ConstraintSet.RIGHT, 0};
        int[] cV = new int[]{ConstraintSet.TOP, getLastNodeId(), ConstraintSet.TOP, 0};

        layoutManager.PlaceInLayout(dottedRectangle, cH, cV);
        b.setExpectedArea(currentExpectedArea);
        currentExpectedArea = new Rect( currentExpectedArea.right , currentExpectedArea.top , currentExpectedArea.right + currentExpectedArea.width() , currentExpectedArea.top  );

    }

    @Override
    public void OnComponentsIntersection() {

        System.out.println("Component intersection");
        if(dottedRectangle.getParent() != null) {

            ((ViewGroup) dottedRectangle.getParent()).removeView(dottedRectangle);

        }

        //Change pointer colour of previous node
        nodes.get(nodes.size() - 2).pointer.setImageResource(R.drawable.forward_arrow_blue);

    }

    @Override
    public void PreGameLevel(Map<String, String> gameLevelState) {

        super.PreGameLevel(gameLevelState);

        for (Map.Entry<String, String> state : gameLevelState.entrySet()) {
            System.out.println("Game Level" + state.getValue());

            switch (state.getKey()) {
                
                case "size":
                    //Create linked list of size
                    state.getValue();

                    CreateNode(false, 0, 30);
                    CreatePointer();
                    PopulateView(String.valueOf(random.nextInt(10000)));
                    ResizeLinkedList(Integer.parseInt(state.getValue())-1);
                    break;

                default:
                    break;

            }
        }
    }
}
