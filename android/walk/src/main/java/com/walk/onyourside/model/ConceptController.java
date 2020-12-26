package com.walk.onyourside.model;

import android.content.Context;

import com.walk.onyourside.interfaces.ConstructionState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConceptController {

    private ConceptStructure conceptStructure;

    private Function currentFunction;

    private Iterator<Instruction> instructionIterator;
    private Iterator<String> pseudocodeIterator;

    private int currentFunctionIndex = 0;

    private ConstructionState constructionState;

    private Instruction currentInstruction;

    public ConceptController(Context context, String concept_name){

        DatabaseManager databaseManager = new DatabaseManager(context, concept_name);
        conceptStructure = databaseManager.DocumentToObject(concept_name);
        currentFunction = conceptStructure.getFunction(currentFunctionIndex);
        instructionIterator = currentFunction.instructions.iterator();
        pseudocodeIterator = currentFunction.pseudocode.iterator();

    }

    public Map<String, String> NextBlockBehaviour(){

        if(currentInstruction.getBlockBehaviour() != null){

            return currentInstruction.getBlockBehaviour();
            //SetBlockBehaviour
        }

        return null;

    }

    public String NextInstruction()
    {
        if (instructionIterator.hasNext()){

            currentInstruction = instructionIterator.next();
            return currentInstruction.instruction;

        }

        //the function call that handles change of function should be here
        //Use a different callback here
        constructionState.OnFunctionFinish();
        return currentFunction.getOutro();
    }

    public String NextBlock() {


        if (pseudocodeIterator.hasNext()){

            return pseudocodeIterator.next();

        }

        return "Complete";
    }


    public int ChangeFunction(int currentFunctionIndex){

        this.currentFunctionIndex = currentFunctionIndex;
        currentFunction = conceptStructure.getFunction(currentFunctionIndex);

        if(currentFunction == null)
            return -1;


        instructionIterator = currentFunction.instructions.iterator();
        pseudocodeIterator = currentFunction.pseudocode.iterator();

        return 0;

    }

    public int getCurrentFunctionIndex(){
        return currentFunctionIndex;
    }

    public String getCurrentFunctionName(){
        return currentFunction.getName();
    }

    public boolean getFunctionSequentialType(){
        return currentFunction.isSequential();
    }

    public void RegisterCallback(ConstructionState constructionState){
        this.constructionState = constructionState;
    }

    public String getCurrentFunctionIntroduction(){
        return currentFunction.getIntroduction();
    }

    public String getCurrentFunctionOutro(){
        return currentFunction.getOutro();
    }

    public List<FunctionForGame> getAllFunctions(){

        List<FunctionForGame> functionsForGame = new ArrayList<>();

        List<Function> functions = conceptStructure.functions;

        for (int i = 0, functionsSize = functions.size(); i < functionsSize; i++) {
            Function function = functions.get(i);

            FunctionForGame functionForGame = new FunctionForGame(function.getName(), function.getIntroduction(), function.getParameters());
            functionsForGame.add(functionForGame);
        }

        return functionsForGame;

    }

    public FunctionForGame getFunctionForGame(int index){

        Function function = conceptStructure.getFunction(index);
        FunctionForGame functionForGame = new FunctionForGame(function.getName(), function.getIntroduction(), function.getParameters());
        return functionForGame;

    }

    public Game getGame(int gameIndex){
        return conceptStructure.getGame(gameIndex);
    }

}
