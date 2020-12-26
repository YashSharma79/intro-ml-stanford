package com.walk.onyourside.model;

import java.util.List;

public class FunctionForGame {

    String name;
    String introduction;
    List<String> parameters;

    public FunctionForGame(String name, String introduction, List<String> parameters){
        this.name = name;
        this.introduction = introduction;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
