package com.walk.onyourside.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
class Instruction{

    public Instruction(){

    }

    String instruction;
    Map<String, String> blockBehaviour;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String functionToCall;
    List<String> functionCallParameters;


    public Map<String, String> getBlockBehaviour() {
        if(blockBehaviour != null)
            return blockBehaviour;

        return null;
    }

    public String getFunctionToCall(){
        return functionToCall;
    }

    @Override
    public String toString() {

        Map<String, String> blockBehaviourMap = null;

        StringBuilder sb = new StringBuilder();

        if (getBlockBehaviour() != null) {
            blockBehaviourMap = getBlockBehaviour();

            for (Map.Entry<String, String> entry : blockBehaviourMap.entrySet()) {
                String k = entry.getKey() + "/" + entry.getValue();
                sb.append(k);
            }

        }

        sb.append(functionToCall);

        if(functionCallParameters != null) {
            for (int i = 0; i < functionCallParameters.size(); i++) {
                sb.append(functionCallParameters.get(i));
            }
        }

        return sb.toString();

    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Function{

    String name;
    List<String> parameters;


    String introduction;

    List<Instruction> instructions;
    List<String> pseudocode;

    String outro;

    boolean sequential;

    public Function(){

    }


    public boolean isSequential() {
        return sequential;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParameters(){
        return parameters;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction(){return introduction;}

    public String getOutro(){return outro;}


    //Build a StringBuilder and override toString
    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        String k = String.format("name = %s\nintroduction = %s", name, getIntroduction());
        sb.append(k);


        for(int i = 0; i < instructions.size(); i++){
            k = "\n" + instructions.get(i).instruction + pseudocode.get(i);
            sb.append(k);

            sb.append( instructions.get(i).toString() );

        }

        sb.append("\n");
        sb.append(getIntroduction());

        sb.append(getOutro());

        return sb.toString();
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Game{

    String name;
    Map<String, String> state;

}

public class ConceptStructure {

    List<Function> functions;
    List<Game> games;

    public ConceptStructure(){

    }


    public Function getFunction(int functionIndex){

        Function f = null;

        if(functions.size() > functionIndex)
            f = functions.get(functionIndex);

        return f;

    }

    public Game getGame(int gameIndex){
        Game g = null;

        if(games.size() > gameIndex)
            g = games.get(gameIndex);

        return g;
    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < functions.size(); i++){

           sb.append( functions.get(i).toString() );
        }

        return sb.toString();

    }

}